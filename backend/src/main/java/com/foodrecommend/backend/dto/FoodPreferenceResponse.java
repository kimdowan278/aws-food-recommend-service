package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;

import java.util.List;

public record FoodPreferenceResponse(
        Long totalUploadCount,
        FoodCategory favoriteCategory,
        FoodType favoriteFoodType,
        Long favoriteFoodTypeCount,
        String searchKeyword,
        List<FoodTypeCountResponse> foodTypeCounts,
        List<PlaceResponse> places
) {
}