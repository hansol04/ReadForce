package com.readforce.service;

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

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {

	private final NewsRepository news_repository;
	private final NewsQuizRepository news_quiz_repository;
	
	// 언어에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguage(String language, String order_by) {
		
		List<GetNews> news_list = news_repository.findByLanguageOrderByCreatedDate(language, order_by);
		
		if(news_list == null) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_NOT_FOUND);
			
		}
		
		return news_list;
		
	}

	// 언어와 난이도에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguageAndLevel(String language, String level, String order_by) {

		List<GetNews> news_list = news_repository.findByLanguageAndLevelOrderByCreatedDate(language, level, order_by);
		
		if(news_list == null) {
			
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

		List<GetNews> news_list = news_repository.findByLanguageAndLevelAndCategoryOrderByCreatedDate(
				language, 
				level, 
				category, 
				order_by
		);
		
		if(news_list == null) {
			
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
	public List<Map<GetNews, GetNewsQuiz>> getProficiencyTestQuizList(String language) {
		
		// 초급 뉴스 가져오기
				
		// 중급 뉴스 가져오기
		
		
				
		// 고급 뉴스 가져오기
		
		return null;
	}

}
