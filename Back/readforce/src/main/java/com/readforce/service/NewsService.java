package com.readforce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.NewsDto;
import com.readforce.dto.NewsDto.GetNews;
import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.dto.NewsDto.ProficiencyTestItem;
import com.readforce.dto.NewsDto.SaveMemberSolvedNewsQuiz;
import com.readforce.entity.Member;
import com.readforce.entity.News;
import com.readforce.entity.NewsQuiz;
import com.readforce.entity.NewsQuizAttempt;
import com.readforce.enums.Level;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.id.NewsQuizAttemptId;
import com.readforce.repository.MemberRepository;
import com.readforce.repository.NewsQuizAttemptRepository;
import com.readforce.repository.NewsQuizRepository;
import com.readforce.repository.NewsRepository;
import com.readforce.enums.Status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

	private final NewsRepository news_repository;
	private final NewsQuizRepository news_quiz_repository;
	private final GeminiService gemini_service;
	private final NewsQuizAttemptRepository news_quiz_attempt_repository;
	private final MemberRepository member_repository;

	// News -> GetNews 변환
	private GetNews transformEntity(News news) {
		
		GetNews get_news = new GetNews();
		get_news.setCategory(news.getCategory());
		get_news.setContent(news.getContent());
		get_news.setLanguage(news.getLanguage());
		get_news.setLevel(news.getLevel());
		get_news.setNews_no(news.getNews_no());
		get_news.setTitle(news.getTitle());
		
		return get_news;
		
	}
	
	// NewsQuiz -> GetNewsQuiz 변환
	private GetNewsQuiz transformEntity(NewsQuiz news_quiz) {
		
		GetNewsQuiz get_news_quiz = new GetNewsQuiz();
		get_news_quiz.setQuestion_text(news_quiz.getQuestion_text());
		get_news_quiz.setChoice1(news_quiz.getChoice1());
		get_news_quiz.setChoice2(news_quiz.getChoice2());
		get_news_quiz.setChoice3(news_quiz.getChoice3());
		get_news_quiz.setChoice4(news_quiz.getChoice4());
		get_news_quiz.setCorrect_answer_index(news_quiz.getCorrect_answer_index());
		get_news_quiz.setExplanation(news_quiz.getExplanation());
		get_news_quiz.setNews_no(news_quiz.getNews_no());
		get_news_quiz.setNews_quiz_no(news_quiz.getNews_quiz_no());
		get_news_quiz.setScore(news_quiz.getScore());
		
		return get_news_quiz;
		
	}
	
	// 언어에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguage(String language, String order_by) {

		List<News> news_list = new ArrayList<>();

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
		
		List<GetNews> get_news_list = new ArrayList<>();
		
		for(News news : news_list) {
			
			get_news_list.add(transformEntity(news));
			
		}

		return get_news_list;

	}

	// 언어와 난이도에 해당하는 뉴스기사 가져오기(반환시 내림차순 리스트 반환)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguageAndLevel(String language, String level, String order_by) {

		List<News> news_list = new ArrayList<>();

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

		List<GetNews> get_news_list = new ArrayList<>();
		
		for(News news : news_list) {
			
			get_news_list.add(transformEntity(news));
			
		}

		return get_news_list;

	}

	// 언어와 난이도, 카테고리에 해당하는 뉴스기사 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetNews> getNewsListByLanguageAndLevelAndCategory(
			String language,
			String level,
			String category,
			String order_by) {

		List<News> news_list = new ArrayList<>();

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

		List<GetNews> get_news_list = new ArrayList<>();
		
		for(News news : news_list) {
			
			get_news_list.add(transformEntity(news));
			
		}

		return get_news_list;
	}

	// 뉴스 기사 문제 가져오기
	@Transactional(readOnly = true)
	public GetNewsQuiz getNewsQuizObject(Long news_no) {

		NewsQuiz news_quiz = news_quiz_repository.findByNewsNo(news_no)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND));


		return transformEntity(news_quiz);

	}

	// 난이도에 해당하는 테스트 문제 가져오기
	@Transactional(readOnly = true)
	public List<ProficiencyTestItem> getProficiencyTestQuizMap(String language) {

		List<ProficiencyTestItem> proficiency_test_quiz_list = new ArrayList<>();

		// 초급 뉴스 가져오기
		News beginner_news = news_repository.findByLanguageAndBeginnerRandom(language)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.BEGINNER_NEWS_NOT_FOUND));

		// 초급 뉴스 문제 가져오기
		NewsQuiz beginner_news_quiz = news_quiz_repository.findByNewsNo(beginner_news.getNews_no())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.BEGINNER_NEWS_QUIZ_NOT_FOUND));

		// List에 저장
		proficiency_test_quiz_list.add(new ProficiencyTestItem(transformEntity(beginner_news), transformEntity(beginner_news_quiz)));

		// 중급 뉴스 가져오기
		List<News> intermediate_news_list = news_repository.findByLanguageAndIntermediateRandom(language);

		if(intermediate_news_list.isEmpty() || intermediate_news_list.size() < 2) {

			throw new ResourceNotFoundException(MessageCode.INTERMEDIATE_NEWS_NOT_FOUND);

		}

		// 중급 뉴스 문제 가져오기
		for(News intermediate_news : intermediate_news_list) {

			NewsQuiz intermediate_news_quiz = news_quiz_repository.findByNewsNo(intermediate_news.getNews_no())
					.orElseThrow(() -> new ResourceNotFoundException(MessageCode.INTERMEDIATE_NEWS_QUIZ_NOT_FOUND));

			proficiency_test_quiz_list.add(new ProficiencyTestItem(transformEntity(intermediate_news), transformEntity(intermediate_news_quiz)));

		}

		// 고급 뉴스 가져오기
		List<News> advanced_news_list = news_repository.findByLanguageAndAdvancedRandom(language);

		if(advanced_news_list.isEmpty() || advanced_news_list.size() < 2) {

			throw new ResourceNotFoundException(MessageCode.ADVANCED_NEWS_NOT_FOUND);

		}

		// 고급 뉴스 문제 가져오기
		for(News advanced_news : advanced_news_list) {

			NewsQuiz advanced_news_quiz = news_quiz_repository.findByNewsNo(advanced_news.getNews_no())
					.orElseThrow(() -> new ResourceNotFoundException(MessageCode.ADVANCED_NEWS_QUIZ_NOT_FOUND));

			proficiency_test_quiz_list.add(new ProficiencyTestItem(transformEntity(advanced_news), transformEntity(advanced_news_quiz)));

		}

		return proficiency_test_quiz_list;
	}

	// Gemini 창작 뉴스 생성
	@Transactional
	public void generateCreativeNewsByGemini() {

		log.info("Gemini 창작 뉴스 생성 시작");

		int count = 0;

		for(NewsRelate.Language language : NewsRelate.Language.values()) {

			for(Level level : Level.values()) {

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

	// 뉴스 문제 생성(뉴스에 해당하는 문제가 없을 시 생성)
	@Transactional
	public void generateCreativeNewsQuizByGemini() {

		log.info("Gemini 뉴스 문제 생성 시작");

		List<News> unquizzed_news_list = news_repository.findUnquizzedNews();
		
		int count = 0;
		
		for(News unquizzed_news : unquizzed_news_list) {
			
			try {
				
				log.info("뉴스 문제 생성 시도: {}", unquizzed_news.getTitle());
				Thread.sleep(9000);
				
				NewsQuiz generated_news_quiz = gemini_service.generateCreativeNewsQuiz(unquizzed_news);
				
				// DB 저장
				news_quiz_repository.save(generated_news_quiz);
				
				log.info("뉴스 문제 저장 완료: {}", generated_news_quiz.getQuestion_text());
				
				count++;
				
			} catch(Exception exception) {
				
				log.warn("뉴스 문제 생성 실패: {}", exception.getMessage());
				
			}
			
		}
		
		log.info("최종 뉴스 문제 생성 갯수: {}", count);

	}

	// 사용자가 푼 뉴스 문제 저장
//	@Transactional
//	public void saveMemberSolvedNewsQuiz(SaveMemberSolvedNewsQuiz save_member_solved_news_quiz, String email) {
//		
//		// 사용자가 정답을 입력했는지 확인
//		NewsQuiz news_quiz = news_quiz_repository.findById(save_member_solved_news_quiz.getNews_quiz_no())
//				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND));
//		
//		boolean is_correct = false;
//		
//		if(news_quiz.getCorrect_answer_index() == save_member_solved_news_quiz.getSelected_option_index()) {
//			
//			is_correct = true;
//			
//		}
//		
//		// 복합키 생성
//		NewsQuizAttemptId news_quiz_attempt_id = new NewsQuizAttemptId();
//		news_quiz_attempt_id.setEmail(email);
//		news_quiz_attempt_id.setNews_quiz_no(save_member_solved_news_quiz.getNews_quiz_no());
//		
//		// Member 엔티티 조회
//		Member member = member_repository.findByEmailAndStatus(email, Status.ACTIVE)
//			    .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
//
//		
//		// 사용자가 푼 뉴스 문제 엔티티 생성
//		NewsQuizAttempt news_quiz_attempt = new NewsQuizAttempt();
//		news_quiz_attempt.setNews_quiz_attempt_id(news_quiz_attempt_id);
//	    news_quiz_attempt.setMember(member); // 김기찬이 추가
//		news_quiz_attempt.setIs_correct(is_correct);
//		news_quiz_attempt.setSelected_option_index(save_member_solved_news_quiz.getSelected_option_index());
//		
//		news_quiz_attempt_repository.save(news_quiz_attempt);
//		
//	}
	// 사용자가 푼 뉴스 문제 저장
	@Transactional
	public void saveMemberSolvedNewsQuiz(SaveMemberSolvedNewsQuiz dto, String email) {
		
		// 퀴즈 정보 조회
		NewsQuiz news_quiz = news_quiz_repository.findById(dto.getNews_quiz_no())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND));

		boolean is_correct = news_quiz.getCorrect_answer_index() == dto.getSelected_option_index();

		// 복합키 생성
		NewsQuizAttemptId id = new NewsQuizAttemptId();
		id.setEmail(email);
		id.setNews_quiz_no(dto.getNews_quiz_no());

		// Member 조회
		Member member = member_repository.findByEmailAndStatus(email, Status.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

		// 저장할 엔티티 구성
		NewsQuizAttempt attempt = new NewsQuizAttempt();
		attempt.setNews_quiz_attempt_id(id);
		attempt.setMember(member); // 필요
		attempt.setNews_quiz(news_quiz); // 필요
		attempt.setIs_correct(is_correct);
		attempt.setSelected_option_index(dto.getSelected_option_index());

		news_quiz_attempt_repository.save(attempt);
	}

	// 사용자가 풀은 뉴스 기사 문제 삭제
	@Transactional
	public void deleteMemberSolvedNewsQuiz(String email, Long news_quiz_no) {

		// 복합키 생성
		NewsQuizAttemptId news_quiz_attempt_id = new NewsQuizAttemptId();
		news_quiz_attempt_id.setEmail(email);
		news_quiz_attempt_id.setNews_quiz_no(news_quiz_no);
		
		// 삭제
		news_quiz_attempt_repository.deleteById(news_quiz_attempt_id);

	}



















}