package com.foodrecommend.backend.service;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;
import com.foodrecommend.backend.dto.FoodPreferenceResponse;
import com.foodrecommend.backend.dto.FoodTypeCountResponse;
import com.foodrecommend.backend.dto.PlaceResponse;
import com.foodrecommend.backend.entity.FoodUpload;
import com.foodrecommend.backend.repository.FoodUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodPreferenceService {

    private final FoodUploadRepository foodUploadRepository;
    private final PlaceSearchKeywordService placeSearchKeywordService;
    private final PlaceSearchService placeSearchService;

    @Transactional(readOnly = true)
    public FoodPreferenceResponse recommendByPreference(
            String regionName,
            String x,
            String y,
            Integer radius
    ) {
        List<FoodUpload> uploads = foodUploadRepository.findAll();

        List<FoodUpload> validUploads = uploads.stream()
                .filter(upload -> upload.getFoodType() != null)
                .filter(upload -> upload.getFoodType() != FoodType.ETC)
                .toList();

        if (validUploads.isEmpty()) {
            return new FoodPreferenceResponse(
                    (long) uploads.size(),
                    FoodCategory.ETC,
                    FoodType.ETC,
                    0L,
                    null,
                    List.of(),
                    List.of()
            );
        }

        Map<FoodType, Long> countMap = validUploads.stream()
                .collect(Collectors.groupingBy(
                        FoodUpload::getFoodType,
                        Collectors.counting()
                ));

        FoodType favoriteFoodType = countMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(FoodType.ETC);

        Long favoriteCount = countMap.getOrDefault(favoriteFoodType, 0L);

        FoodCategory favoriteCategory = validUploads.stream()
                .filter(upload -> upload.getFoodType() == favoriteFoodType)
                .map(FoodUpload::getCategory)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(FoodCategory.ETC);

        boolean hasLocation = StringUtils.hasText(x) && StringUtils.hasText(y);

        String searchKeyword = hasLocation
                ? placeSearchKeywordService.buildLocationSearchKeyword(favoriteCategory, favoriteFoodType)
                : placeSearchKeywordService.buildSearchKeyword(favoriteCategory, favoriteFoodType, regionName);

        List<PlaceResponse> places = placeSearchService.searchPlaces(
                searchKeyword,
                x,
                y,
                radius
        );

        List<FoodTypeCountResponse> foodTypeCounts = countMap.entrySet().stream()
                .sorted(Map.Entry.<FoodType, Long>comparingByValue(Comparator.reverseOrder()))
                .map(entry -> new FoodTypeCountResponse(entry.getKey(), entry.getValue()))
                .toList();

        return new FoodPreferenceResponse(
                (long) uploads.size(),
                favoriteCategory,
                favoriteFoodType,
                favoriteCount,
                searchKeyword,
                foodTypeCounts,
                places
        );
    }
}