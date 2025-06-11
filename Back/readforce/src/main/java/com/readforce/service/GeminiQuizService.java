package com.readforce.service;

import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiQuizService {

    @Value("${gemini.key}")
    private String geminiApiKey;

    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

    @PostConstruct
    public void logKey() {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalStateException("❌ Gemini API 키가 설정되지 않았습니다.");
        }
        System.out.println("✅ Gemini API Key Loaded");
    }

    public Map<String, Object> generateQuiz(String articleContent) {
        RestTemplate restTemplate = new RestTemplate();

        String prompt = String.format("""
            다음 뉴스 기사를 바탕으로 초등학생도 풀 수 있는 객관식 문해력 퀴즈 문제 1개를 JSON 형식으로 만들어줘.
            JSON 형식은 다음과 같아야 해:
            {
              "question": "문제 내용",
              "options": ["보기1", "보기2", "보기3", "보기4"],
              "answer": "정답 보기"
            }

            뉴스 기사 내용:
            %s
        """, articleContent);

        Map<String, Object> payload = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", prompt))
            ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(geminiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_URL, request, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
            String text = parts.get(0).get("text");

            return new ObjectMapper().readValue(text, Map.class);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에서 디버깅 가능
            throw new RuntimeException("Gemini API 오류: " + e.getMessage(), e);
        }
    }
}
