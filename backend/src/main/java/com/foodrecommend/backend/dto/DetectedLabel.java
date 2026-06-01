package com.foodrecommend.backend.dto;

public record DetectedLabel(
        String name,
        Float confidence
) {
}