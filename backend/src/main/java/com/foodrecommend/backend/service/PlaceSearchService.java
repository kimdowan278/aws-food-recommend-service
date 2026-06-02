package com.foodrecommend.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodrecommend.backend.domain.FoodCategory;
import com.foodrecommend.backend.dto.PlaceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceSearchService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestClient restClient = RestClient.create();

    @Value("${app.kakao.rest-api-key:}")
    private String kakaoRestApiKey;

    public List<PlaceResponse> searchPlaces(FoodCategory category, String regionName, String memo) {
        if (!StringUtils.hasText(kakaoRestApiKey)) {
            System.out.println("KAKAO KEY EXISTS = false");
            return List.of();
        }

        String query = buildSearchQuery(category, regionName, memo);


        try {
            String body = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("dapi.kakao.com")
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", query)
                            .queryParam("size", 5)
                            .build()
                    )
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey.trim())
                    .retrieve()
                    .body(String.class);


            JsonNode root = objectMapper.readTree(body);
            JsonNode documents = root.path("documents");

            List<PlaceResponse> places = new ArrayList<>();

            if (documents.isArray()) {
                for (JsonNode document : documents) {
                    places.add(new PlaceResponse(
                            document.path("place_name").asText(),
                            document.path("category_name").asText(),
                            document.path("address_name").asText(),
                            document.path("road_address_name").asText(),
                            document.path("phone").asText(),
                            document.path("place_url").asText(),
                            document.path("x").asText(),
                            document.path("y").asText()
                    ));
                }
            }

            return places;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private String buildSearchQuery(FoodCategory category, String regionName, String memo) {
        String region = StringUtils.hasText(regionName) ? regionName.trim() : "";
        String foodKeyword = extractFoodKeyword(category, memo);

        if (StringUtils.hasText(region)) {
            return region + " " + foodKeyword;
        }

        return foodKeyword;
    }

    private String extractFoodKeyword(FoodCategory category, String memo) {
        if (StringUtils.hasText(memo)) {
            String lowerMemo = memo.toLowerCase();

            if (lowerMemo.contains("떡볶이")) {
                return "떡볶이 맛집";
            }
            if (lowerMemo.contains("라멘")) {
                return "라멘 맛집";
            }
            if (lowerMemo.contains("초밥") || lowerMemo.contains("스시")) {
                return "초밥 맛집";
            }
            if (lowerMemo.contains("카페") || lowerMemo.contains("커피")) {
                return "카페";
            }
            if (lowerMemo.contains("파스타")) {
                return "파스타 맛집";
            }
            if (lowerMemo.contains("햄버거") || lowerMemo.contains("버거")) {
                return "버거 맛집";
            }
        }

        return switch (category) {
            case KOREAN -> "한식 맛집";
            case JAPANESE -> "일식 맛집";
            case CHINESE -> "중식 맛집";
            case WESTERN -> "양식 맛집";
            case DESSERT -> "디저트 카페";
            case CAFE -> "카페";
            case FAST_FOOD -> "패스트푸드";
            case ETC -> "맛집";
        };
    }
}