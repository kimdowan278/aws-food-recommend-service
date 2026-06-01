package com.foodrecommend.backend.config;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.entity.Restaurant;
import com.foodrecommend.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public void run(String... args) {
        saveIfNotExists("부산 돼지국밥집", FoodCategory.KOREAN, "부산", "부산 지역 한식 추천 맛집", 4.6);
        saveIfNotExists("서울 김치찌개", FoodCategory.KOREAN, "서울", "따뜻한 찌개류 한식 맛집", 4.4);
        saveIfNotExists("해운대 스시", FoodCategory.JAPANESE, "부산", "초밥과 사시미 중심의 일식 맛집", 4.5);
        saveIfNotExists("서면 라멘", FoodCategory.JAPANESE, "부산", "진한 국물의 라멘 맛집", 4.3);
        saveIfNotExists("중식당 홍", FoodCategory.CHINESE, "부산", "면류와 볶음밥 중심 중식 맛집", 4.2);
        saveIfNotExists("파스타 하우스", FoodCategory.WESTERN, "서울", "파스타와 스테이크 중심 양식 맛집", 4.5);
        saveIfNotExists("디저트 카페", FoodCategory.DESSERT, "부산", "케이크와 디저트 추천 카페", 4.7);
        saveIfNotExists("브런치 카페", FoodCategory.CAFE, "부산", "커피와 브런치 메뉴가 있는 카페", 4.4);
        saveIfNotExists("버거 스팟", FoodCategory.FAST_FOOD, "서울", "수제버거와 감자튀김 맛집", 4.3);
    }

    private void saveIfNotExists(String name, FoodCategory category, String address,
                                 String description, Double score) {
        if (!restaurantRepository.existsByName(name)) {
            Restaurant restaurant = Restaurant.create(name, category, address, description, score);
            restaurantRepository.save(restaurant);
        }
    }
}