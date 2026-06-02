package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.entity.FoodUpload;
import com.foodrecommend.backend.entity.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

public record FoodUploadResponse(
        Long uploadId,
        String originalFileName,
        String s3Key,
        String regionName,
        String memo,
        FoodCategory category,
        Float topConfidence,
        LocalDateTime createdAt,
        List<LabelResponse> labels,
        List<RestaurantResponse> recommendations,
        List<PlaceResponse> places
) {
    public static FoodUploadResponse from(
            FoodUpload upload,
            List<Restaurant> restaurants,
            List<PlaceResponse> places
    ) {
        return new FoodUploadResponse(
                upload.getId(),
                upload.getOriginalFileName(),
                upload.getS3Key(),
                upload.getRegionName(),
                upload.getMemo(),
                upload.getCategory(),
                upload.getTopConfidence(),
                upload.getCreatedAt(),
                upload.getLabels().stream()
                        .map(LabelResponse::from)
                        .toList(),
                restaurants.stream()
                        .map(RestaurantResponse::from)
                        .toList(),
                places
        );
    }

    public static FoodUploadResponse from(FoodUpload upload, List<Restaurant> restaurants) {
        return from(upload, restaurants, List.of());
    }
}