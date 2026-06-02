package com.foodrecommend.backend.service;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PlaceSearchKeywordService {

    public String buildSearchKeyword(FoodCategory category, FoodType foodType, String regionName) {
        String region = StringUtils.hasText(regionName) ? regionName.trim() : "";
        String foodKeyword = toFoodKeyword(category, foodType);

        if (StringUtils.hasText(region)) {
            return region + " " + foodKeyword;
        }

        return foodKeyword;
    }

    private String toFoodKeyword(FoodCategory category, FoodType foodType) {
        if (foodType != null && foodType != FoodType.ETC) {
            return switch (foodType) {
                case TTEOKBOKKI -> "떡볶이 맛집";
                case KIMBAP -> "김밥 맛집";
                case RAMYEON -> "라면 맛집";
                case GUKBAP -> "국밥 맛집";
                case JJIGAE -> "찌개 맛집";
                case BBQ -> "고기집";
                case SAMGYEOPSAL -> "삼겹살 맛집";
                case CHICKEN -> "치킨 맛집";
                case JOKBAL -> "족발 맛집";
                case BOSSAM -> "보쌈 맛집";

                case SUSHI -> "초밥 맛집";
                case RAMEN -> "라멘 맛집";
                case UDON -> "우동 맛집";
                case DONBURI -> "덮밥 맛집";
                case TEMPURA -> "튀김 맛집";
                case TONKATSU -> "돈카츠 맛집";

                case JJAJANGMYEON -> "짜장면 맛집";
                case JJAMPPONG -> "짬뽕 맛집";
                case TANGSUYUK -> "탕수육 맛집";
                case MALATANG -> "마라탕 맛집";
                case DIMSUM -> "딤섬 맛집";

                case PIZZA -> "피자 맛집";
                case PASTA -> "파스타 맛집";
                case STEAK -> "스테이크 맛집";
                case SALAD -> "샐러드 맛집";
                case SANDWICH -> "샌드위치 맛집";
                case BURGER -> "버거 맛집";

                case CAKE -> "케이크 맛집";
                case BREAD -> "베이커리";
                case ICE_CREAM -> "아이스크림";
                case WAFFLE -> "와플 맛집";
                case COFFEE -> "카페";
                case TEA -> "티룸";

                case ETC -> "맛집";
            };
        }

        return switch (category) {
            case KOREAN -> "한식 맛집";
            case JAPANESE -> "일식 맛집";
            case CHINESE -> "중식 맛집";
            case WESTERN -> "양식 맛집";
            case DESSERT -> "디저트 카페";
            case CAFE -> "카페";
            case FAST_FOOD -> "패스트푸드";
            case ETC -> "맛집";
        };
    }
}