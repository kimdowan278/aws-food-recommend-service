package com.foodrecommend.backend.service;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;
import com.foodrecommend.backend.dto.DetectedLabel;
import com.foodrecommend.backend.dto.FoodClassificationResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FoodClassificationService {

    private static final Map<FoodType, Set<String>> FOOD_TYPE_KEYWORDS = Map.ofEntries(
            Map.entry(FoodType.TTEOKBOKKI, Set.of("떡볶이", "tteokbokki", "rice cake", "ketchup", "spicy", "sauce", "red sauce")),
            Map.entry(FoodType.KIMBAP, Set.of("김밥", "kimbap", "gimbap", "seaweed", "rice roll")),
            Map.entry(FoodType.RAMYEON, Set.of("라면", "ramyeon", "instant noodle")),
            Map.entry(FoodType.GUKBAP, Set.of("국밥", "soup", "rice soup")),
            Map.entry(FoodType.JJIGAE, Set.of("찌개", "stew", "jjigae")),
            Map.entry(FoodType.BBQ, Set.of("고기", "barbecue", "bbq", "grill", "grilled meat")),
            Map.entry(FoodType.SAMGYEOPSAL, Set.of("삼겹살", "pork belly", "pork")),
            Map.entry(FoodType.CHICKEN, Set.of("치킨", "chicken", "fried chicken", "wing", "drumstick")),
            Map.entry(FoodType.JOKBAL, Set.of("족발", "pork feet")),
            Map.entry(FoodType.BOSSAM, Set.of("보쌈", "boiled pork")),

            Map.entry(FoodType.SUSHI, Set.of("초밥", "스시", "sushi", "sashimi", "raw fish")),
            Map.entry(FoodType.RAMEN, Set.of("라멘", "ramen", "japanese noodle")),
            Map.entry(FoodType.UDON, Set.of("우동", "udon")),
            Map.entry(FoodType.DONBURI, Set.of("덮밥", "donburi", "rice bowl")),
            Map.entry(FoodType.TEMPURA, Set.of("튀김", "tempura", "fried shrimp")),
            Map.entry(FoodType.TONKATSU, Set.of("돈까스", "돈카츠", "tonkatsu", "pork cutlet")),

            Map.entry(FoodType.JJAJANGMYEON, Set.of("짜장면", "jajangmyeon", "black bean noodle")),
            Map.entry(FoodType.JJAMPPONG, Set.of("짬뽕", "jjamppong", "spicy seafood noodle")),
            Map.entry(FoodType.TANGSUYUK, Set.of("탕수육", "sweet and sour pork", "fried pork")),
            Map.entry(FoodType.MALATANG, Set.of("마라탕", "malatang", "hot pot", "spicy soup")),
            Map.entry(FoodType.DIMSUM, Set.of("딤섬", "dimsum", "dumpling")),

            Map.entry(FoodType.PIZZA, Set.of("피자", "pizza", "pepperoni")),
            Map.entry(FoodType.PASTA, Set.of("파스타", "pasta", "spaghetti")),
            Map.entry(FoodType.STEAK, Set.of("스테이크", "steak", "beef")),
            Map.entry(FoodType.SALAD, Set.of("샐러드", "salad", "vegetable")),
            Map.entry(FoodType.SANDWICH, Set.of("샌드위치", "sandwich", "toast")),
            Map.entry(FoodType.BURGER, Set.of("햄버거", "버거", "burger", "hamburger", "cheeseburger")),

            Map.entry(FoodType.CAKE, Set.of("케이크", "cake", "dessert")),
            Map.entry(FoodType.BREAD, Set.of("빵", "bread", "bakery", "pastry", "croissant")),
            Map.entry(FoodType.ICE_CREAM, Set.of("아이스크림", "ice cream", "gelato")),
            Map.entry(FoodType.WAFFLE, Set.of("와플", "waffle")),
            Map.entry(FoodType.COFFEE, Set.of("커피", "coffee", "latte", "americano", "espresso")),
            Map.entry(FoodType.TEA, Set.of("차", "tea", "milk tea"))
    );

    public FoodClassificationResult classify(List<DetectedLabel> labels, String memo) {
        FoodType foodType = detectFoodType(labels, memo);
        FoodCategory category = mapCategory(foodType, labels);

        return new FoodClassificationResult(category, foodType);
    }

    private FoodType detectFoodType(List<DetectedLabel> labels, String memo) {
        if (StringUtils.hasText(memo)) {
            String lowerMemo = memo.toLowerCase();

            for (Map.Entry<FoodType, Set<String>> entry : FOOD_TYPE_KEYWORDS.entrySet()) {
                boolean matched = entry.getValue().stream()
                        .anyMatch(lowerMemo::contains);

                if (matched) {
                    return entry.getKey();
                }
            }
        }

        for (DetectedLabel label : labels) {
            String labelName = label.name().toLowerCase();

            for (Map.Entry<FoodType, Set<String>> entry : FOOD_TYPE_KEYWORDS.entrySet()) {
                boolean matched = entry.getValue().stream()
                        .anyMatch(labelName::contains);

                if (matched) {
                    return entry.getKey();
                }
            }
        }

        return FoodType.ETC;
    }

    private FoodCategory mapCategory(FoodType foodType, List<DetectedLabel> labels) {
        return switch (foodType) {
            case TTEOKBOKKI, KIMBAP, RAMYEON, GUKBAP, JJIGAE, BBQ, SAMGYEOPSAL, CHICKEN, JOKBAL, BOSSAM
                    -> FoodCategory.KOREAN;

            case SUSHI, RAMEN, UDON, DONBURI, TEMPURA, TONKATSU
                    -> FoodCategory.JAPANESE;

            case JJAJANGMYEON, JJAMPPONG, TANGSUYUK, MALATANG, DIMSUM
                    -> FoodCategory.CHINESE;

            case PIZZA, PASTA, STEAK, SALAD, SANDWICH
                    -> FoodCategory.WESTERN;

            case BURGER
                    -> FoodCategory.FAST_FOOD;

            case CAKE, BREAD, ICE_CREAM, WAFFLE
                    -> FoodCategory.DESSERT;

            case COFFEE, TEA
                    -> FoodCategory.CAFE;

            case ETC
                    -> detectBroadCategory(labels);
        };
    }

    private FoodCategory detectBroadCategory(List<DetectedLabel> labels) {
        for (DetectedLabel label : labels) {
            String name = label.name().toLowerCase();

            if (name.contains("coffee") || name.contains("cafe") || name.contains("drink")) {
                return FoodCategory.CAFE;
            }

            if (name.contains("cake") || name.contains("dessert") || name.contains("ice cream")) {
                return FoodCategory.DESSERT;
            }

            if (name.contains("burger") || name.contains("french fries") || name.contains("fast food")) {
                return FoodCategory.FAST_FOOD;
            }

            if (name.contains("pizza") || name.contains("pasta") || name.contains("steak")) {
                return FoodCategory.WESTERN;
            }

            if (name.contains("sushi") || name.contains("ramen") || name.contains("udon")) {
                return FoodCategory.JAPANESE;
            }

            if (name.contains("dumpling") || name.contains("chinese food")) {
                return FoodCategory.CHINESE;
            }

            if (name.contains("food") || name.contains("meal") || name.contains("dish")) {
                return FoodCategory.ETC;
            }
        }

        return FoodCategory.ETC;
    }
}