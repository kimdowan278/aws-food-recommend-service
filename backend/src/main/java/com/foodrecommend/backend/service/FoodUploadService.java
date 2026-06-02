package com.foodrecommend.backend.service;

import com.foodrecommend.backend.dto.DetectedLabel;
import com.foodrecommend.backend.dto.FoodClassificationResult;
import com.foodrecommend.backend.dto.FoodUploadResponse;
import com.foodrecommend.backend.dto.PlaceResponse;
import com.foodrecommend.backend.entity.FoodUpload;
import com.foodrecommend.backend.repository.FoodUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodUploadService {

    private final S3Service s3Service;
    private final RekognitionService rekognitionService;
    private final FoodClassificationService foodClassificationService;
    private final PlaceSearchKeywordService placeSearchKeywordService;
    private final PlaceSearchService placeSearchService;
    private final FoodUploadRepository foodUploadRepository;

    @Transactional
    public FoodUploadResponse uploadAndRecommend(
            MultipartFile file,
            String regionName,
            String x,
            String y,
            Integer radius,
            String memo
    ) {
        String s3Key = s3Service.uploadImage(file);

        List<DetectedLabel> detectedLabels = rekognitionService.detectLabels(s3Key);

        FoodClassificationResult classification =
                foodClassificationService.classify(detectedLabels, memo);

        boolean hasLocation = x != null && !x.isBlank() && y != null && !y.isBlank();

        String searchKeyword = hasLocation
                ? placeSearchKeywordService.buildLocationSearchKeyword(
                classification.category(),
                classification.foodType()
        )
                : placeSearchKeywordService.buildSearchKeyword(
                classification.category(),
                classification.foodType(),
                regionName
        );

        Float topConfidence = detectedLabels.isEmpty()
                ? null
                : detectedLabels.get(0).confidence();

        FoodUpload foodUpload = FoodUpload.create(
                file.getOriginalFilename(),
                s3Key,
                regionName,
                x,
                y,
                radius,
                memo,
                classification.category(),
                classification.foodType(),
                searchKeyword,
                topConfidence
        );

        for (DetectedLabel label : detectedLabels) {
            foodUpload.addLabel(label.name(), label.confidence());
        }

        FoodUpload savedUpload = foodUploadRepository.save(foodUpload);

        List<PlaceResponse> places = placeSearchService.searchPlaces(
                searchKeyword,
                x,
                y,
                radius
        );

        return FoodUploadResponse.from(savedUpload, places);
    }

    @Transactional(readOnly = true)
    public List<FoodUploadResponse> findRecentUploads() {
        return foodUploadRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(FoodUploadResponse::from)
                .toList();
    }
}