package com.readforce.controller;

import java.util.List;
import java.util.Map;

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

import com.readforce.dto.NewsDto.GetNews;
import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.dto.NewsDto.SaveMemberSolvedNewsQuiz;
import com.readforce.enums.Level;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.service.NewsService;
import com.readforce.validation.ValidEnum;

import jakarta.validation.Valid;
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
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
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
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("level")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LEVEL_NOT_BLANK)
			@ValidEnum(enumClass = Level.class, message = MessageCode.NEWS_ARTICLE_LEVEL_PATTERN_INVALID)
			String level,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
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
			@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID)
			String language,
			@RequestParam("level")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_LEVEL_NOT_BLANK)
			@ValidEnum(enumClass = Level.class, message = MessageCode.NEWS_ARTICLE_LEVEL_PATTERN_INVALID)
			String level,
			@RequestParam("category")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_CATEGORY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.Category.class, message = MessageCode.NEWS_ARTICLE_CATEGORY_INVALID)
			String category,
			@RequestParam("order_by")
			@NotBlank(message = MessageCode.NEWS_ARTICLE_ORDER_BY_NOT_BLANK)
			@ValidEnum(enumClass = NewsRelate.OrderBy.class, message = MessageCode.NEWS_ARTICLE_ORDER_BY_INVALID)
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
	
	// 사용자가 풀은 뉴스 기사 문제 저장하기
	@PostMapping("/save-member-solved-news-quiz")
	public ResponseEntity<Map<String, String>> saveMemberSolvedNewsQuiz(
			@Valid @RequestBody SaveMemberSolvedNewsQuiz save_member_solved_news_quiz,
			@AuthenticationPrincipal UserDetails user_details
	){
		
		String email = user_details.getUsername();
		
		// 뉴스 기사 문제 저장
		news_service.saveMemberSolvedNewsQuiz(save_member_solved_news_quiz, email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SAVE_MEMBER_SOLVED_NEWS_QUIZ));
		
	}
	
	// 사용자가 풀은 뉴스 기사 문제 삭제하기
	@DeleteMapping("/delete-member-solved-news-quiz")
	public ResponseEntity<Map<String, String>> deleteMemberSolvedNewsQuiz(
			@RequestParam("news_quiz_no")
			@NotNull(message = MessageCode.NEWS_QUIZ_NO_NOT_NULL)
			Long news_quiz_no,
			@AuthenticationPrincipal UserDetails user_details
	){
		
		String email = user_details.getUsername();
		
		// 뉴스 기사 문제 삭제
		news_service.deleteMemberSolvedNewsQuiz(email, news_quiz_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_NEWS_QUIZ_ATTEMPT_SUCCESS				
		));
		
	}
	
	
	
	
}
