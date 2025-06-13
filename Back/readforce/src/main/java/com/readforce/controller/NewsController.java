package com.readforce.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.NewsDto.TransformArticle;
import com.readforce.enums.MessageCode;
import com.readforce.service.NewsService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {
	
	private final NewsService news_service;
	
	
	
	// 뉴스 재구성
	@PostMapping("/transform-article")
	public ResponseEntity<Map<String, String>> transformArticle(@RequestBody TransformArticle transform_article){
		
		String transformed_article = news_service.transformArticle(transform_article);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				
				MessageCode.MESSAGE_CODE, MessageCode.NEWS_TRANSFORM_SUCCESS,
				"TRANSFORMED_ARTICLE", transformed_article
				
		));
		
	}
	
	// 뉴스 기사 스크랩
	@GetMapping("/get-scraped-article")
	public ResponseEntity<Map<String, String>> getScrapedArticle(
			@RequestParam("url")
			@NotBlank(message = MessageCode.NEWS_API_URL_NOT_BLANK)
			String url
	){
		
		String scraped_article = news_service.getScrapedArticle(url);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.NEWS_SCRAPE_SUCCESS,
				"SCRAPED_ARTICLE", scraped_article
		));
		
	}
	
	// 언어별 뉴스 기사 불러오기
	@GetMapping("/get-news-by-language")
	public ResponseEntity<String> getNewsByLanguage(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_LANGUAGE_NOT_BLANK)
			String language,
			@RequestParam("page")
			@NotNull(message = MessageCode.NEWS_PAGE_NOT_NULL)
			int page,
			@RequestParam("size")
			@NotNull(message = MessageCode.NEWS_SIZE_NOT_NULL)
			int size
	){
		
		ResponseEntity<String> response = news_service.getNewsByLanguage(language, page, size);
		
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
		
	}
	
	
	
	
	
	
}
