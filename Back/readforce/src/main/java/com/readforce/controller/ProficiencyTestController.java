package com.readforce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.NewsDto.ProficiencyTestItem;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.service.NewsService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/proficiency_test")
@RequiredArgsConstructor	
@Validated
public class ProficiencyTestController {
	
	private final NewsService news_service;

	// 테스트 지문, 문제 가져오기(뉴스(영어, 일본어, 한국어), 초급1문제, 중급2문제, 고급2문제, 랜덤)
	@GetMapping("/get-proficiency-test-quiz-list")
	public ResponseEntity<List<ProficiencyTestItem>> getProficiencyTestQuizMap(
			@RequestParam("language")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language
	){
		
		// 난이도에 해당하는 테스트 문제 리스트 가져오기
		List<ProficiencyTestItem> proficiency_test_quiz_list = news_service.getProficiencyTestQuizMap(language);
		
		return ResponseEntity.status(HttpStatus.OK).body(proficiency_test_quiz_list);
	}
	
	
	
}
