package com.foodrecommend.backend.dto;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.entity.Restaurant;

public record RestaurantResponse(
        Long id,
        String name,
        FoodCategory category,
        String address,
        String description,
        Double score
) {
    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getScore()
        );
    }
}