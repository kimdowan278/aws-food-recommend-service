package com.foodrecommend.backend.service;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.dto.DetectedLabel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FoodCategoryMapper {

    private static final Map<FoodCategory, Set<String>> KEYWORDS = Map.of(
            FoodCategory.KOREAN, Set.of(
                    "kimchi", "rice", "soup", "stew", "barbecue", "meat", "pork", "beef"
            ),
            FoodCategory.JAPANESE, Set.of(
                    "sushi", "ramen", "noodle", "sashimi", "udon", "tempura"
            ),
            FoodCategory.CHINESE, Set.of(
                    "noodle", "dumpling", "fried rice", "chinese food"
            ),
            FoodCategory.WESTERN, Set.of(
                    "pizza", "pasta", "steak", "salad", "bread"
            ),
            FoodCategory.DESSERT, Set.of(
                    "cake", "dessert", "ice cream", "cookie", "chocolate", "pastry"
            ),
            FoodCategory.CAFE, Set.of(
                    "coffee", "cafe", "latte", "tea", "drink", "beverage"
            ),
            FoodCategory.FAST_FOOD, Set.of(
                    "hamburger", "burger", "french fries", "sandwich", "hot dog"
            )
    );

    public FoodCategory mapToCategory(List<DetectedLabel> labels) {
        for (DetectedLabel label : labels) {
            String labelName = label.name().toLowerCase();

            for (Map.Entry<FoodCategory, Set<String>> entry : KEYWORDS.entrySet()) {
                boolean matched = entry.getValue().stream()
                        .anyMatch(labelName::contains);

                if (matched) {
                    return entry.getKey();
                }
            }
        }

        return FoodCategory.ETC;
    }
}