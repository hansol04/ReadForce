package com.readforce.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class GeminiArticleRewriteService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String rewriteArticle(String title, String description, String content, String language) {
        String prompt = String.format("""
            아래 뉴스 정보를 기반으로 %s로 작성된 뉴스 기사 형식의 본문을 생성해주세요:
            - 원문을 복사하지 말고 자연스럽게 재작성
            - 너무 짧지 않게 500자 이상
            - 기사 스타일 유지

            제목: %s
            설명: %s
            요약 내용: %s
            """, language, title, description, content);

        return callGeminiApi(prompt);
    }

    private String callGeminiApi(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
                .fromHttpUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent")
                .queryParam("key", apiKey)
                .toUriString();

        JSONObject requestBody = new JSONObject();
        JSONArray partsArray = new JSONArray().put(new JSONObject().put("text", prompt));
        JSONArray contentsArray = new JSONArray().put(new JSONObject().put("parts", partsArray));
        requestBody.put("contents", contentsArray);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject body = new JSONObject(response.getBody());
            JSONArray candidates = body.optJSONArray("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
                if (content != null) {
                    JSONArray parts = content.optJSONArray("parts");
                    if (parts != null && !parts.isEmpty()) {
                        return parts.getJSONObject(0).optString("text", "");
                    }
                }
            }
            throw new RuntimeException("Gemini 응답 파싱 실패");
        } else {
            throw new RuntimeException("Gemini 호출 실패: " + response.getStatusCode());
        }
    }
}
