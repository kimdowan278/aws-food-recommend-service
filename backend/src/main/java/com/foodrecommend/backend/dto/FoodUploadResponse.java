package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;
import com.foodrecommend.backend.entity.FoodUpload;

import java.time.LocalDateTime;
import java.util.List;

public record FoodUploadResponse(
        Long uploadId,
        String originalFileName,
        String s3Key,
        String regionName,
        String x,
        String y,
        Integer radius,
        String memo,
        FoodCategory category,
        FoodType foodType,
        String searchKeyword,
        Float topConfidence,
        LocalDateTime createdAt,
        List<LabelResponse> labels,
        List<PlaceResponse> places
) {
    public static FoodUploadResponse from(
            FoodUpload upload,
            List<PlaceResponse> places
    ) {
        return new FoodUploadResponse(
                upload.getId(),
                upload.getOriginalFileName(),
                upload.getS3Key(),
                upload.getRegionName(),
                upload.getX(),
                upload.getY(),
                upload.getRadius(),
                upload.getMemo(),
                upload.getCategory(),
                upload.getFoodType(),
                upload.getSearchKeyword(),
                upload.getTopConfidence(),
                upload.getCreatedAt(),
                upload.getLabels().stream()
                        .map(LabelResponse::from)
                        .toList(),
                places
        );
    }

    public static FoodUploadResponse from(FoodUpload upload) {
        return from(upload, List.of());
    }
}