package com.foodrecommend.backend.controller;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;
import com.foodrecommend.backend.dto.PlaceResponse;
import com.foodrecommend.backend.service.PlaceSearchKeywordService;
import com.foodrecommend.backend.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceSearchKeywordService placeSearchKeywordService;
    private final PlaceSearchService placeSearchService;

    @GetMapping
    public List<PlaceResponse> searchPlaces(
            @RequestParam(defaultValue = "ETC") FoodCategory category,
            @RequestParam(defaultValue = "ETC") FoodType foodType,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y,
            @RequestParam(defaultValue = "3000") Integer radius
    ) {
        boolean hasLocation = x != null && !x.isBlank() && y != null && !y.isBlank();

        String searchKeyword = hasLocation
                ? placeSearchKeywordService.buildLocationSearchKeyword(category, foodType)
                : placeSearchKeywordService.buildSearchKeyword(category, foodType, regionName);

        return placeSearchService.searchPlaces(
                searchKeyword,
                x,
                y,
                radius
        );
    }
}