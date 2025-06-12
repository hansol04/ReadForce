package com.readforce.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class NewsScraperService {

    public String scrapeArticleContent(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(5000)
                    .get();

            Elements articleBody;

            // 도메인 기반 조건부 selector 적용
            if (url.contains("naver.com")) {
                articleBody = doc.select("div#articleBodyContents");
            } else if (url.contains("bbc.com")) {
                articleBody = doc.select("div.story-body, div.ssrcss-uf6wea-RichTextComponentWrapper");
            } else if (url.contains("chosun.com")) {
                articleBody = doc.select("div#news_body_id");
            } else {
                // 기본 selector fallback
                articleBody = doc.select("article, div.article, div.content, div.news_body");
            }

            if (!articleBody.isEmpty()) {
                return articleBody.text().trim();
            } else {
                return "❗ 본문을 찾을 수 없습니다.";
            }

        } catch (Exception e) {
            return "🚨 크롤링 실패: " + e.getMessage();
        }
    }
}
