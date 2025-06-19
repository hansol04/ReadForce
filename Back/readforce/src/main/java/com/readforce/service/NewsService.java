package com.readforce.service;

import java.util.List;

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
	
	// 나라에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguage(String language) {
		
		List<GetNews> news_list = news_repository.findByLanguageOrderByCreatedDateDesc(language);
		
		if(news_list == null) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_NOT_FOUND);
			
		}
		
		return news_list;
		
	}

	// 뉴스 기사 리스트(내림차순) 가져오기
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguageAndLevel(String language, String level) {

		List<GetNews> news_list = news_repository.findByLanguageAndLevelOrderByCreatedDateDesc(language, level);
		
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

}
