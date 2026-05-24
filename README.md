# aws-food-recommend-service
Cloud-based food recommendation web service using AWS Rekognition

# AWS Rekognition 기반 음식 사진 맛집 추천 서비스

사용자가 음식 사진을 업로드하면 AWS Rekognition이 이미지를 분석하여 음식 종류 및 관련 태그를 추출하고, 이를 기반으로 맛집 추천 결과를 제공하는 클라우드 기반 웹 서비스 프로젝트입니다.

---

## 프로젝트 소개 및 목표

기존 맛집 검색 서비스는 사용자가 직접 음식명이나 지역명을 입력해야 하지만, 본 프로젝트는 음식 사진만 업로드해도 AI 기반 이미지 분석을 통해 음식 카테고리를 자동 인식하고 관련 맛집 정보를 추천합니다. 또한 업로드된 음식 사진과 분석 결과를 저장하여 개인 음식 기록 관리 기능도 제공합니다.

AWS 클라우드 서비스와 AI 이미지 분석 기능을 활용하여 실제 서비스 형태의 클라우드 기반 웹 애플리케이션을 구현하는 것을 목표로 합니다.

---

## 주요 기능

- 음식 사진 업로드
- AWS Rekognition 기반 이미지 분석
- 음식 카테고리 자동 분류
- 음식 카테고리 기반 맛집 추천
- 개인 음식 업로드 기록 저장
- 추천 결과 웹페이지 출력

---

## 시스템 구조

```text
사용자 이미지 업로드
↓
EC2 웹 서버(Flask)
↓
S3 이미지 저장
↓
Lambda 자동 실행
↓
Rekognition 이미지 분석
↓
음식 카테고리 변환
↓
RDS 저장 및 추천 결과 생성
↓
웹페이지 출력

---
```

##디렉토리 구조

frontend/  : 웹 프론트엔드 페이지
backend/   : Flask 백엔드 서버
aws/       : Lambda 및 AWS 관련 코드
docker/    : Docker 배포 환경
docs/      : 제안서 및 보고서

---

##팀 역할분담

김도완-Frontend/UI 개발
박영주-Backend 서버 및 추천 로직 개발
임나빈-AWS 클라우드 환경 구축 및 Docker 배포
