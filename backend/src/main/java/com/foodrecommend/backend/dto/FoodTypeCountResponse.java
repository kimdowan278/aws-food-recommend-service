package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodType;

public record FoodTypeCountResponse(
        FoodType foodType,
        Long count
) {
}