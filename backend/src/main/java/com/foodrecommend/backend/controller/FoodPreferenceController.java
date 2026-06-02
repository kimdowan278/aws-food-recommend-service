package com.foodrecommend.backend.controller;

import com.foodrecommend.backend.dto.FoodPreferenceResponse;
import com.foodrecommend.backend.service.FoodPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food/preference")
public class FoodPreferenceController {

    private final FoodPreferenceService foodPreferenceService;

    @GetMapping("/recommend")
    public FoodPreferenceResponse recommendByPreference(
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y,
            @RequestParam(defaultValue = "3000") Integer radius
    ) {
        return foodPreferenceService.recommendByPreference(
                regionName,
                x,
                y,
                radius
        );
    }
}