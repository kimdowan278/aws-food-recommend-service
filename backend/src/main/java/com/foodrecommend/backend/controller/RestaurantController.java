package com.foodrecommend.backend.controller;

import com.foodrecommend.backend.dto.RestaurantCreateRequest;
import com.foodrecommend.backend.dto.RestaurantResponse;
import com.foodrecommend.backend.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public RestaurantResponse createRestaurant(@Valid @RequestBody RestaurantCreateRequest request) {
        return RestaurantResponse.from(restaurantService.create(request));
    }

    @GetMapping
    public List<RestaurantResponse> findAllRestaurants() {
        return restaurantService.findAll()
                .stream()
                .map(RestaurantResponse::from)
                .toList();
    }
}