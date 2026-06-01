package com.foodrecommend.backend.entity;

import com.foodrecommend.backend.domain.FoodCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    private String address;

    @Column(length = 1000)
    private String description;

    private Double score;

    private Restaurant(String name, FoodCategory category, String address,
                       String description, Double score) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.description = description;
        this.score = score;
    }

    public static Restaurant create(String name, FoodCategory category, String address,
                                    String description, Double score) {
        return new Restaurant(name, category, address, description, score);
    }
}