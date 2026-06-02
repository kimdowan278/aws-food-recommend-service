package com.foodrecommend.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodrecommend.backend.dto.PlaceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceSearchService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestClient restClient = RestClient.create();

    @Value("${app.kakao.rest-api-key:}")
    private String kakaoRestApiKey;

    public List<PlaceResponse> searchPlaces(String searchKeyword) {
        return searchPlaces(searchKeyword, null, null, null);
    }

    public List<PlaceResponse> searchPlaces(String searchKeyword, String x, String y, Integer radius) {
        if (!StringUtils.hasText(kakaoRestApiKey)) {
            return List.of();
        }

        if (!StringUtils.hasText(searchKeyword)) {
            return List.of();
        }

        boolean hasLocation = StringUtils.hasText(x) && StringUtils.hasText(y);
        int searchRadius = normalizeRadius(radius);

        try {
            String body = restClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder
                                .scheme("https")
                                .host("dapi.kakao.com")
                                .path("/v2/local/search/keyword.json")
                                .queryParam("query", searchKeyword)
                                .queryParam("size", 15);

                        if (hasLocation) {
                            builder.queryParam("x", x.trim())
                                    .queryParam("y", y.trim())
                                    .queryParam("radius", searchRadius)
                                    .queryParam("sort", "distance");
                        }

                        return builder.build();
                    })
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey.trim())
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(body);
            JsonNode documents = root.path("documents");

            List<PlaceResponse> places = new ArrayList<>();

            if (documents.isArray()) {
                for (JsonNode document : documents) {
                    String placeName = document.path("place_name").asText();
                    String categoryName = document.path("category_name").asText();

                    if (!isRelevantPlace(searchKeyword, placeName, categoryName)) {
                        continue;
                    }

                    places.add(new PlaceResponse(
                            placeName,
                            categoryName,
                            document.path("address_name").asText(),
                            document.path("road_address_name").asText(),
                            document.path("phone").asText(),
                            document.path("place_url").asText(),
                            document.path("x").asText(),
                            document.path("y").asText(),
                            document.path("distance").asText()
                    ));
                }
            }

            return places.stream()
                    .limit(5)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private int normalizeRadius(Integer radius) {
        if (radius == null) {
            return 3000;
        }

        if (radius < 0) {
            return 0;
        }

        return Math.min(radius, 20000);
    }

    private boolean isRelevantPlace(String searchKeyword, String placeName, String categoryName) {
        if (!StringUtils.hasText(searchKeyword)) {
            return true;
        }

        String keyword = searchKeyword.replace("맛집", "").trim();
        String target = (placeName + " " + categoryName).toLowerCase();

        if (!StringUtils.hasText(keyword)) {
            return true;
        }

        if (keyword.contains("떡볶이")) {
            return target.contains("떡볶이") || target.contains("분식");
        }

        if (keyword.contains("김밥")) {
            return target.contains("김밥") || target.contains("분식");
        }

        if (keyword.contains("라면")) {
            return target.contains("라면") || target.contains("분식");
        }

        if (keyword.contains("라멘")) {
            return target.contains("라멘") || target.contains("일식");
        }

        if (keyword.contains("초밥")) {
            return target.contains("초밥") || target.contains("스시") || target.contains("일식");
        }

        if (keyword.contains("돈카츠") || keyword.contains("돈까스")) {
            return target.contains("돈카츠") || target.contains("돈까스") || target.contains("일식");
        }

        if (keyword.contains("치킨")) {
            return target.contains("치킨");
        }

        if (keyword.contains("피자")) {
            return target.contains("피자");
        }

        if (keyword.contains("파스타")) {
            return target.contains("파스타") || target.contains("양식");
        }

        if (keyword.contains("버거")) {
            return target.contains("버거") || target.contains("햄버거");
        }

        if (keyword.contains("카페") || keyword.contains("커피")) {
            return target.contains("카페") || target.contains("커피");
        }

        if (keyword.contains("베이커리") || keyword.contains("빵")) {
            return target.contains("베이커리") || target.contains("빵") || target.contains("제과");
        }

        return target.contains(keyword.toLowerCase());
    }
}