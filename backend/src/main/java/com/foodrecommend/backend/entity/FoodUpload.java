package com.foodrecommend.backend.entity;

import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.domain.FoodType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    @Column(nullable = false)
    private String s3Key;

    private String regionName;

    private String x;

    private String y;

    private Integer radius;

    @Column(length = 1000)
    private String memo;

    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    private String searchKeyword;

    private Float topConfidence;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "foodUpload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodLabel> labels = new ArrayList<>();

    private FoodUpload(String originalFileName, String s3Key, String regionName,
                       String x, String y, Integer radius,
                       String memo, FoodCategory category, FoodType foodType,
                       String searchKeyword, Float topConfidence) {
        this.originalFileName = originalFileName;
        this.s3Key = s3Key;
        this.regionName = regionName;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.memo = memo;
        this.category = category;
        this.foodType = foodType;
        this.searchKeyword = searchKeyword;
        this.topConfidence = topConfidence;
        this.createdAt = LocalDateTime.now();
    }

    public static FoodUpload create(String originalFileName, String s3Key, String regionName,
                                    String x, String y, Integer radius,
                                    String memo, FoodCategory category, FoodType foodType,
                                    String searchKeyword, Float topConfidence) {
        return new FoodUpload(originalFileName, s3Key, regionName, x, y, radius,
                memo, category, foodType, searchKeyword, topConfidence);
    }

    public void addLabel(String labelName, Float confidence) {
        FoodLabel foodLabel = FoodLabel.create(this, labelName, confidence);
        labels.add(foodLabel);
    }
}