package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantCreateRequest(
        @NotBlank String name,
        @NotNull FoodCategory category,
        @NotBlank String address,
        String description,
        Double score
) {
}