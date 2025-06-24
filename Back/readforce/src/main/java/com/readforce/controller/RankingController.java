package com.readforce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.PointDto.PointRanking;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.service.RankingService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RankingController {

	private final RankingService ranking_service;
	
	// 뉴스 랭킹 반환
	@GetMapping("/get-news-ranking")
	public ResponseEntity<List<PointRanking>> getNewsRanking(
			@RequestParam(value = "language", required = false)
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language
	){
			
		List<PointRanking> ranking_list = ranking_service.getNewsRanking(language);

		return ResponseEntity.status(HttpStatus.OK).body(ranking_list);
		
	}
	
	// 문학 랭킹 반환
	@GetMapping("/get-literature-ranking")
	public ResponseEntity<List<PointRanking>> getLiteratureRanking(
			@RequestParam(value = "type", required = false)
			@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
			String type
	){
			
		List<PointRanking> ranking_list = ranking_service.getLiteratureRanking(type);

		return ResponseEntity.status(HttpStatus.OK).body(ranking_list);
		
	}
	
}
