package com.readforce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    @Value("${newsapi.key}")
    private String newsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<?> getNewsByCountry(
        @RequestParam(name = "country") String country,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        String apiUrl;

        if (country.equals("kr")) {
            apiUrl = String.format(
                "https://newsapi.org/v2/everything?q=한국&language=ko&pageSize=5&page=%d&apiKey=%s",
                page, newsApiKey
            );
        }else if (country.equals("jp")) {
            apiUrl = String.format(
                    "https://newsapi.org/v2/everything?q=Japan&language=en&pageSize=5&page=%d&sortBy=publishedAt&apiKey=%s",
                    page, newsApiKey
                );
            } else {
            apiUrl = String.format(
                "https://newsapi.org/v2/top-headlines?country=%s&pageSize=5&page=%d&apiKey=%s",
                country, page, newsApiKey
            );
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("NewsAPI 요청 실패: " + e.getMessage());
        }
    }




    @PostConstruct
    public void printApiKey() {
        System.out.println("🔐 Loaded NEWS_API_KEY = " + newsApiKey);
    }
}
