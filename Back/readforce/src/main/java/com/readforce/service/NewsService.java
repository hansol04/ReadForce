package com.readforce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.NewsDto;
import com.readforce.dto.NewsDto.GetNews;
import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.entity.News;
import com.readforce.entity.NewsQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.NewsQuizRepository;
import com.readforce.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

	private final NewsRepository news_repository;
	private final NewsQuizRepository news_quiz_repository;
	private final GeminiService gemini_service;
	
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
	
	// Gemini 창작 뉴스 생성
	@Transactional
	public void generateCreativeNewsByGemini() {
		
		log.info("Gemini 창작 뉴스 생성 시작");
		
		int count = 0;
		
		for(NewsRelate.Language language : NewsRelate.Language.values()) {
			
			for(NewsRelate.Level level : NewsRelate.Level.values()) {
				
				for(NewsRelate.Category category : NewsRelate.Category.values()) {
					
					try {
						
						log.info("기사 생성 시작 - 언어 : {}, 난이도 : {}, 카테고리 : {}", language, level, category);
						
						NewsDto.NewsResult news_result =
								gemini_service.generateCreativeNews(language, level, category);
						
						// 뉴스 엔티티 생성
						News news = new News();
						news.setTitle(news_result.title());
						news.setLanguage(language.toString());
						news.setCategory(category.toString());
						news.setLevel(level.toString());
						news.setContent(news_result.content());
						
						news_repository.save(news);
						
						count++;
						
						Thread.sleep(1000);
						
					} catch(Exception exception) {
						
						log.error("기사 생성 실패 - 언어 : {}, 난이도 : {}, 카테고리 : {}", language, level, category, exception.getMessage());
						
					}
					
				}
				
			}
			
		}
		
		log.info("전체 Gemini 창작 뉴스 생성 성공 - 총 {}개", count);
		
	}

	// 뉴스 퀴즈 생성(뉴스에 해당하는 문제가 없을 시 생성)
	public void generateCreativeNewsQuizByGemini() {
		
		log.info("Gemini 뉴스 퀴즈 생성 시작");
		
		// 뉴스에 해당하는 뉴스 퀴즈가 없는 뉴스 가져오기
		List<News> unquizzed_news_list = news_repository.findUnquizzedNews();
		
		int count = 0;
		
		for(News unquizzed_news : unquizzed_news_list) {
			
			try {
				
				log.info("뉴스 퀴즈 생성 시도: {}", unquizzed_news.getTitle());
				Thread.sleep(9000);
				
				NewsQuiz generated_news_quiz = gemini_service.generateCreativeNewsQuiz(unquizzed_news);
				
				// DB 저장
				news_quiz_repository.save(generated_news_quiz);
				
				log.info("뉴스 퀴즈 저장 완료: {}", generated_news_quiz.getQuestion_text());
				
				count++;
				
			} catch(Exception exception) {
				
				log.warn("뉴스 퀴즈 생성 실패: {}", exception.getMessage());
				
			}
			
		}
		
		log.info("최종 퀴즈 생성 갯수: {}", count);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
