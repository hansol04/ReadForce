package com.readforce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.NewsDto.GetNewsPassage;
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

//	private final NewsService news_service;
//
//	// 나라에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
//	@GetMapping("/get-news-passage-list-by-country")
//	public ResponseEntity<List<GetNewsPassage>> getNewsPassageListByCountry(
//			@RequestParam("country")
//			@NotBlank(message = MessageCode.NEWS_ARTICLE_COUNTRY_NOT_BLANK)
//			@Pattern(regexp = "^(kr|jp|uk|us)$", message = MessageCode.NEWS_ARTICLE_COUNTRY_PATTERN_INVALID)
//			String country
//	){
//
//		// 뉴스 기사 리스트(내림차순) 가져오기
//		List<GetNewsPassage> news_passage_list = news_service.getNewsPassageListByCountry(country);
//
//		return ResponseEntity.status(HttpStatus.OK).body(news_passage_list);
//
//	}
//
//	// 나라와 난이도에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
//	@GetMapping("/get-news-passage-list-by-country-and-level")
//	public ResponseEntity<List<GetNewsPassage>> getNewsPassagelistByCountryAndLevel(
//			@RequestParam("country")
//			@NotBlank(message = MessageCode.NEWS_ARTICLE_COUNTRY_NOT_BLANK)
//			@Pattern(regexp = "^(kr|jp|uk|us)$", message = MessageCode.NEWS_ARTICLE_COUNTRY_PATTERN_INVALID)
//			String country,
//			@RequestParam("level")
//			@NotBlank(message = MessageCode.NEWS_ARTICLE_LEVEL_NOT_BLANK)
//			@Pattern(regexp = "^(초급|중급|고급)$", message = MessageCode.NEWS_ARTICLE_LEVEL_PATTERN_INVALID)
//			String level
//	){
//
//		// 뉴스 기사 리스트(내림차순) 가져오기
//		List<GetNewsPassage> news_passage_list = news_service.getNewsPassagelistByCountryAndLevel(country, level);
//
//		return ResponseEntity.status(HttpStatus.OK).body(news_passage_list);
//
//	}
//
//	// 뉴스 기사 문제 가져오기
//	@GetMapping("/get-news-quiz-object")
//	public ResponseEntity<GetNewsQuiz> getNewsQuizObject(
//			@RequestParam("news_passage_no")
//			@NotNull(message = MessageCode.NEWS_PASSAGE_NO_NOT_NULL)
//			Long news_passage_no
//	){
//
//		// 뉴스 기사 문제 가져오기
//		GetNewsQuiz news_quiz_object = news_service.getNewsQuizObject(news_passage_no);
//
//		return ResponseEntity.status(HttpStatus.OK).body(news_quiz_object);
//
//	}

}