package com.foodrecommend.backend.repository;

import com.foodrecommend.backend.entity.FoodUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodUploadRepository extends JpaRepository<FoodUpload, Long> {

    List<FoodUpload> findTop20ByOrderByCreatedAtDesc();
}