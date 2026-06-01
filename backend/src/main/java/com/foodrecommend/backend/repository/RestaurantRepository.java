package com.foodrecommend.backend.repository;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByName(String name);

    List<Restaurant> findTop5ByCategoryOrderByScoreDesc(FoodCategory category);

    List<Restaurant> findTop5ByCategoryAndAddressContainingOrderByScoreDesc(
            FoodCategory category,
            String address
    );

    List<Restaurant> findTop5ByOrderByScoreDesc();
}