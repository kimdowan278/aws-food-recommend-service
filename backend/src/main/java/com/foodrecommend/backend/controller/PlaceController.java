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
            @RequestParam(required = false) String regionName
    ) {
        String searchKeyword = placeSearchKeywordService.buildSearchKeyword(
                category,
                foodType,
                regionName
        );

        return placeSearchService.searchPlaces(searchKeyword);
    }
}