package com.foodrecommend.backend.dto;

public record PlaceResponse(
        String name,
        String categoryName,
        String addressName,
        String roadAddressName,
        String phone,
        String placeUrl,
        String x,
        String y
) {
}