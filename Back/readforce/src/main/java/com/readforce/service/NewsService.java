package com.readforce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.NewsDto.GetNews;
import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.NewsQuizRepository;
import com.readforce.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {

	private final NewsRepository news_repository;
	private final NewsQuizRepository news_quiz_repository;
	
	// 언어에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguage(String language, String order_by) {
		
		List<GetNews> news_list = new ArrayList<>();
		
		switch(order_by) {
			
			case "ASC":
				news_list = news_repository.findByLanguageOrderByCreatedDateAsc(language);
				break;
			
			case "DESC":
				news_list = news_repository.findByLanguageOrderByCreatedDateDesc(language);
				break;
		
		}
				
		if(news_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_NOT_FOUND);
			
		}
		
		return news_list;
		
	}

	// 언어와 난이도에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguageAndLevel(String language, String level, String order_by) {

		List<GetNews> news_list = new ArrayList<>();
		
		switch(order_by) {
		
			case "ASC":
				news_list = news_repository.findByLanguageAndLevelOrderByCreatedDateAsc(language, level);
				break;
				
			case "DESC":
				news_list = news_repository.findByLanguageAndLevelOrderByCreatedDateDesc(language, level);
				break;
				
		}
				
		if(news_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_NOT_FOUND);
			
		}
		
		return news_list;
	
	}

	// 언어와 난이도, 카테고리에 해당하는 뉴스기사 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguageAndLevelAndCategory(
			String language,
			String level, 
			String category,
			String order_by) {

		List<GetNews> news_list = new ArrayList<>();
		
		switch(order_by) {
		
			case "ASC":
				news_list = news_repository.findByLanguageAndLevelAndCategoryOrderByCreatedDateAsc(language, level, category);
				break;
			
			case "DESC":
				news_list = news_repository.findByLanguageAndLevelAndCategoryOrderByCreatedDateDesc(language, level, category);
				break;
				
		}

		if(news_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_NOT_FOUND);
			
		}
		
		return news_list;
	}
	
	// 뉴스 기사 문제 가져오기
	@Transactional(readOnly = true)
	public GetNewsQuiz getNewsQuizObject(Long news_no) {

		GetNewsQuiz news_quiz = news_quiz_repository.findByNewsNo(news_no)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND));
		
		
		return news_quiz;
		
	}

	// 난이도에 해당하는 테스트 문제 가져오기
	@Transactional(readOnly = true)
	public Map<GetNews, GetNewsQuiz> getProficiencyTestQuizMap(String language) {
		
		Map<GetNews, GetNewsQuiz> proficiency_test_quiz = new HashMap<>();
		
		// 초급 뉴스 가져오기
		GetNews beginner_news = news_repository.findByLanguageAndBeginnerRandom(language)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.BEGINNER_NEWS_NOT_FOUND));
		
		// 초급 뉴스 문제 가져오기
		GetNewsQuiz beginner_news_quiz = news_quiz_repository.findByNewsNo(beginner_news.getNew_no())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.BEGINNER_NEWS_QUIZ_NOT_FOUND));
		
		// Map에 저장
		proficiency_test_quiz.put(beginner_news, beginner_news_quiz);
		
		// 중급 뉴스 가져오기
		List<GetNews> intermediate_news_list = news_repository.findByLanguageAndIntermediateRandom(language);
		
		if(intermediate_news_list.isEmpty() || intermediate_news_list.size() < 2) {
			
			throw new ResourceNotFoundException(MessageCode.INTERMEDIATE_NEWS_NOT_FOUND);
			
		}
		
		// 중급 뉴스 문제 가져오기
		for(GetNews intermediate_news : intermediate_news_list) {
			
			GetNewsQuiz intermediate_news_quiz = news_quiz_repository.findByNewsNo(intermediate_news.getNew_no())
					.orElseThrow(() -> new ResourceNotFoundException(MessageCode.INTERMEDIATE_NEWS_QUIZ_NOT_FOUND));
			
			proficiency_test_quiz.put(intermediate_news, intermediate_news_quiz);

		}

		// 고급 뉴스 가져오기
		List<GetNews> advanced_news_list = news_repository.findByLanguageAndAdvancedRandom(language);
		
		if(advanced_news_list.isEmpty() == advanced_news_list.size() < 2) {
			
			throw new ResourceNotFoundException(MessageCode.ADVANCED_NEWS_NOT_FOUND);
			
		}
		
		// 고급 뉴스 문제 가져오기
		for(GetNews advanced_news : advanced_news_list) {
			
			GetNewsQuiz advanced_news_quiz = news_quiz_repository.findByNewsNo(advanced_news.getNew_no())
					.orElseThrow(() -> new ResourceNotFoundException(MessageCode.ADVANCED_NEWS_QUIZ_NOT_FOUND));

			proficiency_test_quiz.put(advanced_news, advanced_news_quiz);
			
		}
 		
		return proficiency_test_quiz;
	}

}
