package com.foodrecommend.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String labelName;

    private Float confidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_upload_id")
    private FoodUpload foodUpload;

    private FoodLabel(FoodUpload foodUpload, String labelName, Float confidence) {
        this.foodUpload = foodUpload;
        this.labelName = labelName;
        this.confidence = confidence;
    }

    public static FoodLabel create(FoodUpload foodUpload, String labelName, Float confidence) {
        return new FoodLabel(foodUpload, labelName, confidence);
    }
}