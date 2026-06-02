package com.foodrecommend.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoPlaceResponse(
        List<Document> documents
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Document(
            @JsonProperty("place_name")
            String placeName,

            @JsonProperty("category_name")
            String categoryName,

            @JsonProperty("address_name")
            String addressName,

            @JsonProperty("road_address_name")
            String roadAddressName,

            String phone,

            @JsonProperty("place_url")
            String placeUrl,

            String x,
            String y
    ) {
        public PlaceResponse toPlaceResponse() {
            return new PlaceResponse(
                    placeName,
                    categoryName,
                    addressName,
                    roadAddressName,
                    phone,
                    placeUrl,
                    x,
                    y
            );
        }
    }
}