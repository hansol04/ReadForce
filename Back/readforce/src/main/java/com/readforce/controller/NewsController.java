package com.readforce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.NewsDto.GetNews;
import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.enums.News;
import com.readforce.service.NewsService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor	
@Validated
public class NewsController {

	private final NewsService news_service;
	
	// 언어에 해당하는 뉴스기사 가져오기(내림차순/오름차순)
	@GetMapping("/get-news-list-by-language")
	public ResponseEntity<List<GetNews>> getNewsListByLanguage(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@ValidEnum(enumClass = News.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = News.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
			String order_by
	){
		
		// 뉴스 기사 리스트 가져오기
		List<GetNews> news_list = news_service.getNewsListByLanguage(language, order_by);
		
		return ResponseEntity.status(HttpStatus.OK).body(news_list);
		
	}
	
	// 언어와 난이도에 해당하는 뉴스기사 가져오기(내림차순/오름차순)
	@GetMapping("/get-news-list-by-language-and-level")
	public ResponseEntity<List<GetNews>> getNewsListByLanguageAndLevel(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@ValidEnum(enumClass = News.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("level")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LEVEL_NOT_BLANK)
			@ValidEnum(enumClass = News.Level.class, message = MessageCode.NEWS_ARTICLE_LEVEL_PATTERN_INVALID)
			String level,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = News.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
			String order_by
	){
		
		// 뉴스 기사 리스트 가져오기
		List<GetNews> news_list = news_service.getNewsListByLanguageAndLevel(language, level, order_by);
		
		return ResponseEntity.status(HttpStatus.OK).body(news_list); 
		
	}
	
	// 언어와 난이도, 카테고리에 해당하는 뉴스기사 가져오기(내림차순/오름차순)
	@GetMapping("/get-news-list-by-language-and-level-and-category")
	public ResponseEntity<List<GetNews>> getNewsListByLanguageAndLevelAndCategory(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@ValidEnum(enumClass = News.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("level")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LEVEL_NOT_BLANK)
			@ValidEnum(enumClass = News.Level.class, message = MessageCode.NEWS_ARTICLE_LEVEL_PATTERN_INVALID)
			String level,
			@RequestParam("category")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_CATEGORY_NOT_BLANK)
			@ValidEnum(enumClass = News.Category.class, message = MessageCode.NEWS_ARTICLE_CATEGORY_INVALID)
			String category,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = News.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
			String order_by
	){
		
		// 뉴스 기사 리스트 가져오기
		List<GetNews> news_list = news_service.getNewsListByLanguageAndLevelAndCategory(language, level, category, order_by);

		return ResponseEntity.status(HttpStatus.OK).body(news_list);
		
	}
	
	
	// 뉴스 기사 문제 가져오기
	@GetMapping("/get-news-quiz-object")
	public ResponseEntity<GetNewsQuiz> getNewsQuizObject(
			@RequestParam("news_no")
			@NotNull(message = MessageCode.NEWS_NO_NOT_NULL)
			Long news_no
	){
		
		// 뉴스 기사 문제 가져오기
		GetNewsQuiz news_quiz = news_service.getNewsQuizObject(news_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(news_quiz);
		
	}
	
}
