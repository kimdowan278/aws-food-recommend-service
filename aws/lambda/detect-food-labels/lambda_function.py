"""
S3 이미지 업로드 시 Rekognition을 통해 음식 라벨을 추출하고 DB에 저장하는 람다 함수.

[Architecture]
- Trigger: S3 ObjectCreated (prefix: uploads/)
- Action: AWS Rekognition DetectLabels API 호출
- DB Target: food_recommend.food_label
- Idempotency: 동일 업로드 ID(s3_key 기준)에 대해 기존 라벨 DELETE 후 다중 INSERT 수행
"""

import json
import logging
import os
import time
from typing import Any
from urllib.parse import unquote_plus

import boto3
import pymysql
from pymysql.cursors import DictCursor

logger = logging.getLogger()
logger.setLevel(logging.INFO)

rekognition = boto3.client("rekognition")

MAX_LABELS = 10
MIN_CONFIDENCE = 60.0
UPLOAD_PREFIX = "uploads/"
DB_LOOKUP_RETRIES = int(os.environ.get("DB_LOOKUP_RETRIES", "5"))
DB_LOOKUP_INTERVAL_SEC = float(os.environ.get("DB_LOOKUP_INTERVAL_SEC", "2"))


def handler(event: dict[str, Any], context: Any) -> dict[str, Any]:
    logger.info("event=%s", json.dumps(event))

    record = event["Records"][0]
    bucket = record["s3"]["bucket"]["name"]
    key = unquote_plus(record["s3"]["object"]["key"])

    if not key.startswith(UPLOAD_PREFIX):
        logger.info("skip non-upload key: %s", key)
        return {"status": "skipped", "reason": "prefix mismatch", "key": key}

    labels = detect_labels(bucket, key)
    upload_id = find_food_upload_id(key)
    inserted = save_labels(upload_id, labels)

    result = {
        "status": "ok",
        "bucket": bucket,
        "key": key,
        "food_upload_id": upload_id,
        "label_count": len(labels),
        "inserted_count": inserted,
    }
    logger.info("result=%s", result)
    return result


def detect_labels(bucket: str, key: str) -> list[dict[str, Any]]:
    response = rekognition.detect_labels(
        Image={"S3Object": {"Bucket": bucket, "Name": key}},
        MaxLabels=MAX_LABELS,
        MinConfidence=MIN_CONFIDENCE,
    )
    return response.get("Labels", [])


def find_food_upload_id(s3_key: str) -> int:
    last_error: Exception | None = None

    for attempt in range(1, DB_LOOKUP_RETRIES + 1):
        try:
            with db_connection() as conn:
                with conn.cursor() as cursor:
                    cursor.execute(
                        "SELECT id FROM food_upload WHERE s3_key = %s",
                        (s3_key,),
                    )
                    row = cursor.fetchone()
                    if row:
                        upload_id = int(row["id"])
                        logger.info("found food_upload id=%s for key=%s", upload_id, s3_key)
                        return upload_id
        except Exception as exc:
            last_error = exc
            logger.warning("db lookup failed attempt=%s error=%s", attempt, exc)

        if attempt < DB_LOOKUP_RETRIES:
            logger.info(
                "food_upload not found yet (attempt %s/%s), retry in %ss: %s",
                attempt,
                DB_LOOKUP_RETRIES,
                DB_LOOKUP_INTERVAL_SEC,
                s3_key,
            )
            time.sleep(DB_LOOKUP_INTERVAL_SEC)

    message = (
        f"food_upload row not found for s3_key={s3_key}. "
        "RDS에 row를 먼저 넣었는지 확인하세요."
    )
    if last_error:
        raise RuntimeError(message) from last_error
    raise RuntimeError(message)


def save_labels(upload_id: int, labels: list[dict[str, Any]]) -> int:
    if not labels:
        logger.warning("no labels detected for food_upload_id=%s", upload_id)
        return 0

    with db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute(
                "DELETE FROM food_label WHERE food_upload_id = %s",
                (upload_id,),
            )
            cursor.executemany(
                """
                INSERT INTO food_label (label_name, confidence, food_upload_id)
                VALUES (%s, %s, %s)
                """,
                [
                    (label["Name"], float(label["Confidence"]), upload_id)
                    for label in labels
                ],
            )
        conn.commit()

    return len(labels)


def db_connection() -> pymysql.connections.Connection:
    return pymysql.connect(
        host=os.environ["DB_HOST"],
        port=int(os.environ.get("DB_PORT", "3306")),
        user=os.environ["DB_USERNAME"],
        password=os.environ["DB_PASSWORD"],
        database=os.environ.get("DB_NAME", "food_recommend"),
        charset="utf8mb4",
        cursorclass=DictCursor,
        connect_timeout=10,
        read_timeout=10,
        write_timeout=10,
    )
