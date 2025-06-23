package com.readforce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.PointDto.SaveChallengePoint;
import com.readforce.entity.LiteratureQuiz;
import com.readforce.entity.NewsQuiz;
import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.exception.ChallengeException;
import com.readforce.service.PointService;
import com.readforce.service.QuizService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor	
@Validated
public class ChallengeController {
	
	private final QuizService quiz_service;
	private final PointService point_service;

	// 도전 문제 가져오기(랜덤)(뉴스(영어, 일본어, 한국어), 문학(소설, 동화))
	@GetMapping("/get-challenge-quiz-by-classification-and-type-or-language")
	public ResponseEntity<?> getChallengeQuizByClassificationAndTypeOrLanguage(
			@RequestParam("classification")
			@NotBlank(message = MessageCode.CLASSIFICATION_NOT_BLANK)
			@ValidEnum(enumClass = Classification.class, message = MessageCode.CLASSIFICATION_PATTERN_INVALID)
			String classification,
			@RequestParam("type")
			@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
			String type,
			@RequestParam("language")
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language
	){
		
		// 뉴스 도전 문제 가져오기
		if(Classification.NEWS.toString().equals(classification) && !language.isBlank()) {
			
			List<NewsQuiz> news_quiz_list = quiz_service.getChallengeQuizWithNewsQuiz(type, language);
			
			return ResponseEntity.status(HttpStatus.OK).body(news_quiz_list);
			
		}
		
		// 문학 도전 문제 가져오기
		if(Classification.LITERATURE.toString().equals(classification) && !type.isBlank()) {
			
			List<LiteratureQuiz> literature_quiz_list = quiz_service.getChallengeQuizWithLiteratureQuiz();
			
			return ResponseEntity.status(HttpStatus.OK).body(literature_quiz_list);
			
		}
		
		throw new ChallengeException(MessageCode.GET_CHALLENGE_QUIZ_FAIL);
		
	}
	
	// 도전 점수 저장하기
	@PatchMapping("/update-challenge-point")
	public ResponseEntity<Map<String, String>> updateChallengePoint(
			@Valid
			@RequestBody SaveChallengePoint save_challenge_point,
			@AuthenticationPrincipal UserDetails user_details
	){
		
		String email = user_details.getUsername();
		
		point_service.updateChallengePoint(email, save_challenge_point);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CHALLENGE_POINT_UPDATE_SUCCESS
		));
		
	}
	

	
}
