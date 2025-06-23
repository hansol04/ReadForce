package com.readforce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.service.RankingService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RankingController {

	private final RankingService ranking_service;
	
	// 랭킹 반환
	@GetMapping("/get-ranking-by-classification-and-type-or-language")
	public ResponseEntity<?> getRankingByClassificationAndTypeOrLanguage(
			@RequestParam("classification")
			@NotBlank(message = MessageCode.CLASSIFICATION_NOT_NULL)
			@ValidEnum(enumClass = Classification.class, message = MessageCode.CLASSIFICATION_PATTERN_INVALID)
			String classification,
			@RequestParam("type")
			@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
			String type,
			@RequestParam("language")
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,			
			@AuthenticationPrincipal UserDetails user_details
	){
		
		List<?> ranking_list = ranking_service.getRankingByClassificationAndTypeOrLanguage(classification, type, language);

		return ResponseEntity.status(HttpStatus.OK).body(ranking_list);
		
	}
	
}
