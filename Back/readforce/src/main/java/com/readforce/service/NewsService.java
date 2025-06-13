package com.readforce.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.readforce.dto.NewsDto.TransformArticle;
import com.readforce.enums.MessageCode;
import com.readforce.exception.NewsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
	
	@Value("${news.api.user-agent}")
	private String news_api_user_agent;
	
	@Value("${news.api.timeout}")
	private int news_api_timeout;
	
	@Value("${news.api.key}")
	private String news_api_key;
	
	@Value("${news.api.korean-url}")
	private String news_api_korean_url;
	
	@Value("${news.api.japanese-url}")
	private String news_api_japanese_url;
	
	@Value("${news.api.english-url}")
	private String news_api_english_url;
	
	private final RestTemplate rest_template;
	private final GeminiService gemini_service;
	
	// 뉴스 재구성
	public String transformArticle(TransformArticle transform_article) {
		
		// 프롬프트 작성
		String prompt = writePrompt(transform_article);
		
		// 뉴스 재구성 제미나이 요청
		String transformed_article = gemini_service.callGeminiApi(prompt);
		
		return transformed_article;
		
	}
	
	// 프롬프트 작성
	public String writePrompt(TransformArticle transform_article) {
		
		// 프롬프트 작성
		String prompt = String.format(
		"""
				아래 뉴스 정보를 기반으로 %s로 작성된 뉴스 기사 형식의 본문을 생성해주세요:
				- 원문을 복사하지 말고 자연스럽게 재생성
				- 너무 짧지 않게 500자 이상
				- 기사 스타일 유지
				
				제목: %s
				설명: %s
				요약 내용: %s
				
				
		"""
		, transform_article.getLanguage()
		, transform_article.getTitle()
		, transform_article.getDescription()
		, transform_article.getContent());
		
		return prompt;
		
	}

	// 뉴스 기사 스크랩
	public String getScrapedArticle(String url) {

		try {
			
			Document document = Jsoup.connect(url)
					.userAgent(news_api_user_agent)
					.timeout(news_api_timeout)
					.get();
			
			Elements article_body;
			
			if(url.contains("naver.com")) {
				
				article_body = document.select("div#articleBodyContents");
				
			} else if(url.contains("bbc.com")) {
				
				article_body = document.select("div.story-body, div.ssrcss-uf6wea-RichTextComponentWrapper");
				
			} else if(url.contains("chosun.com")) {
				
				article_body = document.select("div#news_body_id");
				
			} else {
				
				// 기본 selector fallback
				article_body = document.select("article, div.article, div.content, div.news_body");
				
			}
			
			if(!article_body.isEmpty()) {
				
				return article_body.text().trim();
				
			} else {
				
				throw new NewsException(MessageCode.NEWS_ARTICLE_GET_FAIL_FROM_API);
				
			}
			
		} catch(Exception exception) {
			
			throw new NewsException(MessageCode.NEWS_ARTICLE_GET_FAIL_FROM_API);
			
		}
		
	}

	// 언어별 뉴스 기사 가져오기
	public ResponseEntity<String> getNewsByLanguage(String language, int page, int size) {

		String api_url;
		
		switch(language) {
			
			case "kr":
				api_url = String.format(
						news_api_korean_url,
						page,
						news_api_key						
				);
				break;
				
			case "jp":
				api_url = String.format(
						news_api_japanese_url,
						page,
						news_api_key						
				);
				break;
				
			case "en":
				api_url = String.format(
						news_api_english_url,
						language,
						page,
						news_api_key						
				);
				break;
				
			default:
				throw new NewsException(MessageCode.NEWS_LANGUAGE_PATTERN_INVALID);
		
		}
		
		// 뉴스 요청
		try {
			
			ResponseEntity<String> response = rest_template.getForEntity(api_url, String.class);
			return response;
					
		} catch(Exception exception) {
			
			throw new NewsException(MessageCode.NEWS_ARTICLE_GET_FAIL_FROM_API);
			
		}

	}
	
}
