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
import com.readforce.service.NewsService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor	
@Validated
public class NewsController {

	private final NewsService news_service;
	
	// 나라에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@GetMapping("/get-news-list-by-language")
	public ResponseEntity<List<GetNews>> getNewsListByLanguage(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@Pattern(regexp = "^(kr|jp|en)$", message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language
	){
		
		// 뉴스 기사 리스트(내림차순) 가져오기
		List<GetNews> news_list = news_service.getNewsListByLanguage(language);
		
		return ResponseEntity.status(HttpStatus.OK).body(news_list);
		
	}
	
	// 나라와 난이도에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@GetMapping("/get-news-list-by-language-and-level")
	public ResponseEntity<List<GetNews>> getNewsListByLanguageAndLevel(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@Pattern(regexp = "^(한국어|일본어|영어)$", message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("level")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LEVEL_NOT_BLANK)
			@Pattern(regexp = "^(초급|중급|고급)$", message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String level
	){
		
		// 뉴스 기사 리스트(내림차순) 가져오기
		List<GetNews> news_list = news_service.getNewsListByLanguageAndLevel(language, level);
		
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
