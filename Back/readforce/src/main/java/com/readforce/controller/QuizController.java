package com.readforce.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.readforce.service.GeminiQuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final GeminiQuizService geminiQuizService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQuiz(@RequestBody Map<String, String> body) {
        String article = body.get("article");
        if (article == null || article.isBlank()) {
            return ResponseEntity.badRequest().body("기사 내용이 필요합니다.");
        }

        try {
            Map<String, Object> quiz = geminiQuizService.generateQuiz(article);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("퀴즈 생성 실패: " + e.getMessage());
        }
    }
}
