package com.foodrecommend.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "AWS Food Recommendation Spring Backend Running";
    }

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}