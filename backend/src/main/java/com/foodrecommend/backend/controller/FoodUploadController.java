package com.foodrecommend.backend.controller;

import com.foodrecommend.backend.dto.FoodUploadResponse;
import com.foodrecommend.backend.service.FoodUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodUploadController {

    private final FoodUploadService foodUploadService;

    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FoodUploadResponse uploadFoodImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String memo
    ) {
        return foodUploadService.uploadAndRecommend(file, regionName, memo);
    }

    @GetMapping("/uploads")
    public List<FoodUploadResponse> findRecentUploads() {
        return foodUploadService.findRecentUploads();
    }
}