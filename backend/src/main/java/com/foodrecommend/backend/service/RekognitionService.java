package com.foodrecommend.backend.service;

import com.foodrecommend.backend.dto.DetectedLabel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RekognitionService {

    private final RekognitionClient rekognitionClient;

    @Value("${app.aws.s3.bucket}")
    private String bucketName;

    public List<DetectedLabel> detectLabels(String s3Key) {
        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(Image.builder()
                        .s3Object(S3Object.builder()
                                .bucket(bucketName)
                                .name(s3Key)
                                .build())
                        .build())
                .maxLabels(10)
                .minConfidence(60F)
                .build();

        return rekognitionClient.detectLabels(request)
                .labels()
                .stream()
                .map(label -> new DetectedLabel(label.name(), label.confidence()))
                .toList();
    }
}