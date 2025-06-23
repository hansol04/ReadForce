package com.readforce.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.MemberDto.MemberAttemptedQuiz;
import com.readforce.dto.MemberDto.MemberIncorrectQuiz;
import com.readforce.entity.LiteratureQuiz;
import com.readforce.entity.LiteratureQuizAttempt;
import com.readforce.entity.NewsQuiz;
import com.readforce.entity.NewsQuizAttempt;
import com.readforce.enums.Classification;
import com.readforce.enums.Level;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.exception.ValueException;
import com.readforce.repository.LiteratureQuizAttemptRepository;
import com.readforce.repository.LiteratureQuizRepository;
import com.readforce.repository.NewsQuizAttemptRepository;
import com.readforce.repository.NewsQuizRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class QuizService {
	
	private final NewsQuizAttemptRepository news_quiz_attempt_repository;
	private final LiteratureQuizAttemptRepository literature_quiz_attempt_repository;
	private final NewsQuizRepository news_quiz_repository;
	private final LiteratureQuizRepository literature_quiz_repository;

	

	// 사용자가 푼 최근 문제 10개 가져오기
	@Transactional(readOnly = true)
	public List<MemberAttemptedQuiz> getMemberSolvedQuizList10(String email) {
		
		// 사용자가 풀은 뉴스 문제 가져오기
		List<NewsQuizAttempt> news_quiz_attempt_list = news_quiz_attempt_repository.findByEmailWithNewQuizList(email);
		
		// 사용자가 푼 문학 문제 가져오기
		List<LiteratureQuizAttempt> literature_quiz_attempt_list = literature_quiz_attempt_repository.getMemberSolvedLiteratureQuizList(email);
		
		if(news_quiz_attempt_list.isEmpty() && literature_quiz_attempt_list.isEmpty()) {
			
			if(news_quiz_attempt_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.NEWS_QUIZ_ATTEMPT_NOT_FOUND);
				
			}
			
			
			if(literature_quiz_attempt_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.LITERATURE_QUIZ_ATTEMPT_NOT_FOUND);
				
			}
			
		}
		
		// 합치기
		List<MemberAttemptedQuiz> member_attempted_quiz = new ArrayList<>();
		
		// 뉴스 문제
		for(NewsQuizAttempt news_quiz_attempt : news_quiz_attempt_list) {
			
			MemberAttemptedQuiz member_attempt_quiz = new MemberAttemptedQuiz();
			member_attempt_quiz.setQuestion_text(news_quiz_attempt.getNews_quiz().getQuestion_text());
			member_attempt_quiz.setClassification(Classification.NEWS.toString());
			member_attempt_quiz.setCreated_date(news_quiz_attempt.getCreated_date());
			
			member_attempted_quiz.add(member_attempt_quiz);
			
		}
		
		// 문학 문제
		for(LiteratureQuizAttempt literature_quiz_attempt : literature_quiz_attempt_list) {
			
			MemberAttemptedQuiz member_attempt_quiz = new MemberAttemptedQuiz();
			member_attempt_quiz.setQuestion_text(literature_quiz_attempt.getLiterature_quiz().getQuestion_text());
			member_attempt_quiz.setClassification(Classification.LITERATURE.toString());
			member_attempt_quiz.setCreated_date(literature_quiz_attempt.getCreated_date());
			
			member_attempted_quiz.add(member_attempt_quiz);
			
		}
		
		// 내림차순 정렬
		member_attempted_quiz.sort(Comparator.comparing(MemberAttemptedQuiz::getCreated_date).reversed());
		
		// 상위 10개만 반환
		int size = member_attempted_quiz.size();
		List<MemberAttemptedQuiz> final_member_attempted_quiz_list = member_attempted_quiz.subList(0, Math.min(size, 10));
		
		return final_member_attempted_quiz_list;

	}


	// 틀린 문제 가져오기(최신순)
	@Transactional(readOnly = true)
	public List<MemberIncorrectQuiz> getMemberIncorrectQuizList(String email) {

		// 사용자가 풀은 뉴스 문제중 틀린 문제 가져오기
		List<NewsQuizAttempt> incorrect_news_quiz_attempt = news_quiz_attempt_repository.getIncorrectNewQuizAttemptList(email);
		
		// 사용자가 풀은 문학 문제중 틀린 문제 가져오기
		List<LiteratureQuizAttempt> incorrect_literature_quiz_attempt = literature_quiz_attempt_repository.getIncorrectLiteratureQuizAttemptList(email);
		
		// 합치기
		List<MemberIncorrectQuiz> member_incorrect_quiz_list = new ArrayList<>();
		
		// 뉴스 문제
		for(NewsQuizAttempt news_quiz_attempt : incorrect_news_quiz_attempt) {
			
			MemberIncorrectQuiz member_incorrect_quiz = new MemberIncorrectQuiz();
			member_incorrect_quiz.setQuestion_text(news_quiz_attempt.getNews_quiz().getQuestion_text());
			member_incorrect_quiz.setQuiz_no(news_quiz_attempt.getNews_quiz().getNews_quiz_no());
			member_incorrect_quiz.setClassification(Classification.NEWS.toString());
			member_incorrect_quiz.setCreated_date(news_quiz_attempt.getCreated_date());
			
			member_incorrect_quiz_list.add(member_incorrect_quiz);
			
		}
		
		// 문학 문제
		for(LiteratureQuizAttempt literature_quiz_attempt : incorrect_literature_quiz_attempt) {
			
			MemberIncorrectQuiz member_incorrect_quiz = new MemberIncorrectQuiz();
			member_incorrect_quiz.setQuestion_text(literature_quiz_attempt.getLiterature_quiz().getQuestion_text());
			member_incorrect_quiz.setQuiz_no(literature_quiz_attempt.getLiterature_quiz().getLiterature_quiz_no());
			member_incorrect_quiz.setClassification(Classification.LITERATURE.toString());
			member_incorrect_quiz.setCreated_date(literature_quiz_attempt.getCreated_date());
			
			member_incorrect_quiz_list.add(member_incorrect_quiz);
			
		}
		
		// 내림차순 정렬
		member_incorrect_quiz_list.sort(Comparator.comparing(MemberIncorrectQuiz::getCreated_date).reversed());
		
		return member_incorrect_quiz_list;
		
	}
	
	// 퀴즈 ID 목록을 가져와 무작위로 섞은 뒤, 필요한 개수만큼 ID를 반환
	private List<Long> getRandomQuizIdList(List<Long> all_id_list, int required_number, String exception_message){
		
		if(all_id_list.size() < required_number) {
			
			throw new ResourceNotFoundException(exception_message);
			
		}
		
		Collections.shuffle(all_id_list);
		
		return all_id_list.subList(0, required_number);
		
	}

	// 뉴스 도전 문제 가져오기
	@Transactional(readOnly = true)
	public List<NewsQuiz> getChallengeQuizWithNewsQuiz(String type, String language) {
		
		
		if(type.isBlank() && language.isBlank()) {
			
			throw new ValueException(MessageCode.TYPE_AND_LANGUAGE_NOT_BLANK);
			
		}
		
		if(!type.isBlank() && !language.isBlank()) {
			
			throw new ValueException(MessageCode.ONLY_ONE_BETWEEN_TYPE_OR_LANGUAGE);
			
		}
		
		// 뉴스 초급에 해당하는 뉴스 퀴즈 번호 7개 랜덤으로 가져오기
		List<Long> beginner_news_quiz_no_list = getRandomQuizIdList(
				news_quiz_repository.findNewsQuizNoByLevel(Level.BEGINNER.toString()),
				7,
				MessageCode.LACK_OF_BEGINNER_NEWS_QUIZ
		);

		// 뉴스 중급에 해당하는 뉴스 퀴즈 번호 6개 랜덤으로 가져오기
		List<Long> intermediate_news_quiz_no_list = getRandomQuizIdList(
				news_quiz_repository.findNewsQuizNoByLevel(Level.INTERMEDIATE.toString()),
				6,
				MessageCode.LACK_OF_INTERMEDIATE_NEWS_QUIZ
		);
		
		// 뉴스 고급에 해당하는 뉴스 퀴즈 번호 7개 랜덤으로 가져오기
		List<Long> advanced_news_quiz_no_list = getRandomQuizIdList(
				news_quiz_repository.findNewsQuizNoByLevel(Level.ADVANCED.toString()),
				7,
				MessageCode.LACK_OF_ADVANCED_NEWS_QUIZ
		);
		
		// 뉴스 퀴즈 번호 합치기
		List<Long> combined_news_quiz_no_list = Stream.of(beginner_news_quiz_no_list, intermediate_news_quiz_no_list, advanced_news_quiz_no_list)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		
		// 합친 뉴스 퀴즈 번호에 해당하는 뉴스 퀴즈 가져오기
		List<NewsQuiz> combined_news_quiz_list = news_quiz_repository.findAllById(combined_news_quiz_no_list);
		
		return combined_news_quiz_list;

	}

	// 문학 도전 문제 가져오기
	@Transactional(readOnly = true)
	public List<LiteratureQuiz> getChallengeQuizWithLiteratureQuiz() {
		
		// 문학 초급에 해당하는 문학 퀴즈 번호 7개 랜덤으로 가져오기
		List<Long> beginner_literature_quiz_no_list = getRandomQuizIdList(
				literature_quiz_repository.findLiteratureQuizNoByLevel(Level.BEGINNER.toString()),
				7,
				MessageCode.LACK_OF_BEGINNER_LITERATURE_QUIZ
		);

		// 문학 중급에 해당하는 문학 퀴즈 번호 6개 랜덤으로 가져오기
		List<Long> intermediate_literature_quiz_no_list = getRandomQuizIdList(
				literature_quiz_repository.findLiteratureQuizNoByLevel(Level.INTERMEDIATE.toString()),
				6,
				MessageCode.LACK_OF_INTERMEDIATE_LITERATURE_QUIZ
		);
		
		// 문학 고급에 해당하는 문학 퀴즈 번호 7개 랜덤으로 가져오기
		List<Long> advanced_literature_quiz_no_list = getRandomQuizIdList(
				literature_quiz_repository.findLiteratureQuizNoByLevel(Level.ADVANCED.toString()),
				7,
				MessageCode.LACK_OF_ADVANCED_LITERATURE_QUIZ
		);
		
		// 문학 퀴즈 번호 합치기
		List<Long> combined_literature_quiz_no_list = Stream.of(beginner_literature_quiz_no_list, intermediate_literature_quiz_no_list, advanced_literature_quiz_no_list)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		
		// 합친 문학 퀴즈 번호에 해당하는 문학 퀴즈 가져오기
		List<LiteratureQuiz> combined_literature_quiz_list = literature_quiz_repository.findAllById(combined_literature_quiz_no_list);
		
		return combined_literature_quiz_list;

	}


}
