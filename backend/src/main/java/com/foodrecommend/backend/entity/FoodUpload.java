package com.foodrecommend.backend.entity;

import com.foodrecommend.backend.domain.FoodCategory;
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

    @Column(length = 1000)
    private String memo;

    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    private Float topConfidence;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "foodUpload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodLabel> labels = new ArrayList<>();

    private FoodUpload(String originalFileName, String s3Key, String regionName,
                       String memo, FoodCategory category, Float topConfidence) {
        this.originalFileName = originalFileName;
        this.s3Key = s3Key;
        this.regionName = regionName;
        this.memo = memo;
        this.category = category;
        this.topConfidence = topConfidence;
        this.createdAt = LocalDateTime.now();
    }

    public static FoodUpload create(String originalFileName, String s3Key, String regionName,
                                    String memo, FoodCategory category, Float topConfidence) {
        return new FoodUpload(originalFileName, s3Key, regionName, memo, category, topConfidence);
    }

    public void addLabel(String labelName, Float confidence) {
        FoodLabel foodLabel = FoodLabel.create(this, labelName, confidence);
        labels.add(foodLabel);
    }
}