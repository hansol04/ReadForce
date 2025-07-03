package com.readforce.controller;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
import com.readforce.dto.LiteratureDto.GetLiteratureQuiz;
import com.readforce.dto.LiteratureDto.SaveMemberSolvedLiteratureQuiz;
import com.readforce.enums.Level;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.service.LiteratureService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/literature")
@RequiredArgsConstructor
@Validated
@Slf4j
public class LiteratureController {
	
	private final LiteratureService literature_service;

	// 타입에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@GetMapping("/get-literature-paragraph-list-by-type")
	public ResponseEntity<List<GetLiteratureParagraph>> getLiteratureParagraphListByType(
			@RequestParam("type")
			@NotBlank(message = MessageCode.LITERATURE_TYPE_NOT_BLANK)
			@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
			String type,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
			String order_by
	){
		
		// 문학 문단 리스트 가져오기
		List<GetLiteratureParagraph> literature_paragraph_list = literature_service.getLiteratureParagraphListByType(type, order_by);
		
		return ResponseEntity.status(HttpStatus.OK).body(literature_paragraph_list);
		
	}
	
	
	// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@GetMapping("/get-literature-paragraph-list-by-type-and-level")
	public ResponseEntity<List<GetLiteratureParagraph>> getLiteratureParagraphListByTypeAndLevel(
			@RequestParam("type")
			@NotBlank(message = MessageCode.LITERATURE_TYPE_NOT_BLANK)
			@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
			String type,
			@RequestParam("level")
			@NotBlank(message = MessageCode.LITERATURE_LEVEL_NOT_BLANK)
			@ValidEnum(enumClass = Level.class, message = MessageCode.LEVEL_PATTERN_INVALID)
			String level,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
			String order_by
			
	){
		
		// 문학 문단 리스트 가져오기
		List<GetLiteratureParagraph> literature_paragraph_list = literature_service.getLiteratureParagraphListByTypeAndLevel(type, level, order_by);
		
		return ResponseEntity.status(HttpStatus.OK).body(literature_paragraph_list);

	}
	
	// 타입과 난이도, 카테고리에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@GetMapping("/get-literature-paragraph-list-by-type-and-level-and-category")
	public ResponseEntity<List<GetLiteratureParagraph>> getLiteratureParagraphListByTypeAndLevelAndCategory(
			@RequestParam("type")
			@NotBlank(message = MessageCode.LITERATURE_TYPE_NOT_BLANK)
			@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
			String type,
			@RequestParam("level")
			@NotBlank(message = MessageCode.LITERATURE_LEVEL_NOT_BLANK)
			@ValidEnum(enumClass = Level.class, message = MessageCode.LEVEL_PATTERN_INVALID)
			String level,
			@RequestParam("category")
			@NotBlank(message = MessageCode.LITERATURE_CATEGORY_NOT_BLANK)
			@ValidEnum(enumClass = LiteratureRelate.category.class, message = MessageCode.LITERATURE_CATEGORY_PATTERN_INVALID)
			String category,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
			String order_by
	){

		log.warn("type : " + type + " levle : " +  level + " category : " + category + " order_by : " + order_by);

		// 문학 문단 리스트 가져오기
		List<GetLiteratureParagraph> literature_paragraph_list = literature_service.getLiteratureParagraphListByTypeAndLevelAndCategory(type, level, category, order_by);
		
		return ResponseEntity.status(HttpStatus.OK).body(literature_paragraph_list);
		
	}
	
	
	// 문학 문제 가져오기
	@GetMapping("/get-literature-quiz-object")
	public ResponseEntity<GetLiteratureQuiz> getLiteratureQuizObject(
			@RequestParam("literature_paragraph_no")
			@NotNull(message = MessageCode.LITERATURE_PARAGRAPH_NO_NOT_NULL)
			Long literature_paragraph_no,
			@RequestParam("literature_no")
			@NotNull(message = MessageCode.LITERATURE_NO_NOT_NULL)
			Long literature_no
	){
		
		// 문학 문제 가져오기
		GetLiteratureQuiz get_literature_quiz = literature_service.getLiteratureQuizObject(literature_paragraph_no, literature_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(get_literature_quiz);
		
	}
	
	// 사용자가 풀은 문학 문제 저장하기
	@PostMapping("/save-member-solved-literature-quiz")
	public ResponseEntity<Map<String, String>> saveMemberSolvedLiteratureQuiz(
		@Valid @RequestBody SaveMemberSolvedLiteratureQuiz save_member_solved_literature_quiz,
		@AuthenticationPrincipal UserDetails user_details
	){
		
		String email = user_details.getUsername();
		
		// 문학 문제 저장
		literature_service.saveMemberSolvedLiteratureQuiz(save_member_solved_literature_quiz, email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SAVE_MEMBER_SOLVED_LITERATURE_QUIZ));
		
	}
	
	// 사용자가 풀은 문학 문제 삭제하기
	@DeleteMapping("/delete-member-solved-literature-quiz")
	public ResponseEntity<Map<String, String>> deleteMemberSolvedLiteratureQuiz(
			@RequestParam("literature_quiz_no")
			@NotNull(message = MessageCode.LITERATURE_QUIZ_NO_NOT_NULL)
			Long literature_quiz_no,
			@AuthenticationPrincipal UserDetails user_details
	){
		
		String email = user_details.getUsername();
		
		// 사용자가 풀은 문학 문제 삭제하기
		literature_service.deleteMemberSolvedLiteratureQuiz(email, literature_quiz_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LITERATURE_QUIZ_ATTEMPT_SUCCESS
		));
		
	}
	
	
	
	
}
