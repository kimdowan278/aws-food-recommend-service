package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;

public record FoodClassificationResult(
        FoodCategory category,
        FoodType foodType
) {
}