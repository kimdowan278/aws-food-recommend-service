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
                                .queryParam("size", 5);

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
                    places.add(new PlaceResponse(
                            document.path("place_name").asText(),
                            document.path("category_name").asText(),
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

            return places;
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
}