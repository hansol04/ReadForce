package com.readforce.service;

import com.readforce.dto.QuizDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class GeminiQuizService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public QuizDto generateQuiz(String article, String language) {
        String prompt;

        switch (language.toLowerCase()) {
            case "english":
            	prompt = """
            	Based on the following article, create exactly one multiple-choice quiz question in English.

            	Format:
            	Question: [Your question here]
            	1. [Option A]
            	2. [Option B]
            	3. [Option C]
            	4. [Option D]
            	Answer: [A/B/C/D]
            	Explanation: [Short explanation]

            	Only one question. Do not generate more than one.

            	Article:
            	%s
            	""".formatted(article);


                break;
            case "japanese":
            	prompt = """
            	以下の記事に基づいて、日本語でクイズを1問だけ作成してください。

            	形式：
            	問題: [質問文]
            	1. [選択肢A]
            	2. [選択肢B]
            	3. [選択肢C]
            	4. [選択肢D]
            	正解: [A/B/C/D]
            	解説: [理由を説明]

            	記事:
            	%s
            	""".formatted(article);


                break;
            default:
                prompt = """
                    다음 기사를 바탕으로 퀴즈를 하나 만들어주세요. 형식은 아래와 같아야 합니다:

                    문제: [질문 내용]
                    1. [보기1]
                    2. [보기2]
                    3. [보기3]
                    4. [보기4]
                    정답: [A/B/C/D]
                    해설: [정답에 대한 설명]

                    기사:
                    %s
                """.formatted(article);
        }

        try {
            String geminiResponse = callGeminiApi(prompt);
            log.info("📩 Gemini 응답: {}", geminiResponse);
            return parseQuizResponse(geminiResponse);
        } catch (Exception e) {
            log.error("Gemini API 처리 중 오류 발생", e);
            throw new RuntimeException("퀴즈 생성 실패: " + e.getMessage());
        }
    }

    private String callGeminiApi(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
                .fromHttpUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent")
                .queryParam("key", apiKey)
                .toUriString();

        JSONObject requestBody = new JSONObject();
        JSONArray partsArray = new JSONArray();
        partsArray.put(new JSONObject().put("text", prompt));

        JSONArray contentsArray = new JSONArray();
        contentsArray.put(new JSONObject().put("parts", partsArray));
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
            throw new RuntimeException("Gemini API 호출 실패: " + response.getStatusCode());
        }
    }

    private QuizDto parseQuizResponse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("응답이 비어있습니다.");
        }

        text = text.replace("\r\n", "\n").trim();

        String qKey = getKeyword(text, List.of("문제:", "Question:", "問題:"));
        String aKey = getKeyword(text, List.of("정답:", "Answer:", "正解:"));
        String eKey = getKeyword(text, List.of("해설:", "Explanation:", "解説:"));

        int qStart = text.indexOf(qKey);
        int aStart = text.indexOf(aKey);
        int eStart = text.indexOf(eKey);

        if (qStart == -1 || aStart == -1 || eStart == -1) {
            throw new IllegalArgumentException("응답 형식 오류: 문제, 정답, 해설 태그가 누락됨");
        }

        String questionBlock = text.substring(qStart + qKey.length(), aStart).trim();

        // ✅ 여기를 수정 — 마크다운, 공백 제거
        String answerRaw = text.substring(aStart + aKey.length(), eStart).trim();
        String answerLetter = answerRaw.replaceAll("[^A-Da-d1-4]", "").toUpperCase();

        // 숫자 정답 → 문자 변환
        if (answerLetter.matches("[1-4]")) {
            answerLetter = switch (answerLetter) {
                case "1" -> "A";
                case "2" -> "B";
                case "3" -> "C";
                case "4" -> "D";
                default -> answerLetter;
            };
        }

        String explanation = text.substring(eStart + eKey.length()).trim();

        List<String> choices = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+\\.\\s(.+)");
        Matcher matcher = pattern.matcher(questionBlock);

        while (matcher.find()) {
            choices.add(matcher.group(1).trim());
        }

        String questionLine = questionBlock.split("\n")[0].trim();

        int answerIndex = switch (answerLetter) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> throw new IllegalArgumentException("정답 형식 오류: A~D 이외 문자 → " + answerLetter);
        };

        if (answerIndex >= choices.size()) {
            throw new IllegalArgumentException("정답 인덱스가 보기 개수를 초과합니다.");
        }

        return new QuizDto(
                questionLine,
                choices,
                choices.get(answerIndex),
                explanation
        );
    }

    private String getKeyword(String text, List<String> candidates) {
        for (String key : candidates) {
            if (text.contains(key)) return key;
        }
        return "";
    }
}
