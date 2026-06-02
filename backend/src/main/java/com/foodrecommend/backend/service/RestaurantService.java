package com.foodrecommend.backend.service;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.dto.RestaurantCreateRequest;
import com.foodrecommend.backend.entity.Restaurant;
import com.foodrecommend.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant create(RestaurantCreateRequest request) {
        Restaurant restaurant = Restaurant.create(
                request.name(),
                request.category(),
                request.address(),
                request.description(),
                request.score() == null ? 0.0 : request.score()
        );

        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> recommend(FoodCategory category, String regionName) {
        if (category == FoodCategory.ETC) {
            if (StringUtils.hasText(regionName)) {
                List<Restaurant> regionRestaurants =
                        restaurantRepository.findTop5ByAddressContainingOrderByScoreDesc(regionName);

                if (!regionRestaurants.isEmpty()) {
                    return regionRestaurants;
                }
            }

            return restaurantRepository.findTop5ByOrderByScoreDesc();
        }

        if (StringUtils.hasText(regionName)) {
            List<Restaurant> regionRestaurants =
                    restaurantRepository.findTop5ByCategoryAndAddressContainingOrderByScoreDesc(
                            category,
                            regionName
                    );

            if (!regionRestaurants.isEmpty()) {
                return regionRestaurants;
            }
        }

        return restaurantRepository.findTop5ByCategoryOrderByScoreDesc(category);
    }
}