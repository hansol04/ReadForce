package com.readforce.controller;

import com.readforce.service.GeminiArticleRewriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsRewriteController {

    private final GeminiArticleRewriteService articleRewriteService;

    @PostMapping("/transform")
    public Map<String, String> transformArticle(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String description = payload.get("description");
        String content = payload.get("content");
        String language = payload.getOrDefault("language", "한국어"); // 기본값: 한국어

        String rewritten = articleRewriteService.rewriteArticle(title, description, content, language);
        return Map.of("rewritten", rewritten);
    }

}
