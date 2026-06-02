package com.foodrecommend.backend.controller;

import com.foodrecommend.backend.dto.FoodUploadResponse;
import com.foodrecommend.backend.service.FoodUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food/uploads")
public class FoodUploadController {

    private final FoodUploadService foodUploadService;

    @PostMapping
    public FoodUploadResponse uploadFoodImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y,
            @RequestParam(defaultValue = "3000") Integer radius,
            @RequestParam(required = false) String memo
    ) {
        return foodUploadService.uploadAndRecommend(
                file,
                regionName,
                x,
                y,
                radius,
                memo
        );
    }

    @GetMapping
    public List<FoodUploadResponse> getRecentUploads() {
        return foodUploadService.findRecentUploads();
    }
}