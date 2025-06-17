package com.readforce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.readforce.dto.NewsDto.GetNewsPassage;
import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.entity.NewsQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.exception.NewsException;
import com.readforce.repository.NewsPassageRepository;
import com.readforce.repository.NewsQuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
	
	private final NewsPassageRepository news_passage_repository;
	private final NewsQuizRepository news_quiz_repository;
	
	// 뉴스 기사 리스트(내림차순) 가져오기
	public List<GetNewsPassage> getNewsPassagelist(String country, String level) {
		
		List<GetNewsPassage> news_passage_list = news_passage_repository.findByCountryAndLevelOrderByCreatedDate(country, level);
		
		if(news_passage_list.isEmpty()) {
			
			throw new NewsException(MessageCode.NEWS_PASSAGE_NOT_FOUND);
			
		}
		
		return news_passage_list;
		
	}

	// 뉴스 문제 가져오기
	public GetNewsQuiz getNewsQuizObject(Long news_passage_no) {

		return news_quiz_repository.findByNewsPassageNo(news_passage_no)
				.orElseThrow(() -> new NewsException(MessageCode.NEWS_QUIZ_NOT_FOUND));

	}


}
