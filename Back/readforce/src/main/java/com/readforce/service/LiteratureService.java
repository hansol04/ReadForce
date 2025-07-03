package com.readforce.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
import com.readforce.dto.LiteratureDto.GetLiteratureQuiz;
import com.readforce.dto.LiteratureDto.SaveMemberSolvedLiteratureQuiz;
import com.readforce.entity.LiteratureParagraph;
import com.readforce.entity.LiteratureQuiz;
import com.readforce.entity.LiteratureQuizAttempt;
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.id.LiteratureQuizAttemptId;
import com.readforce.repository.LiteratureParagraphRepository;
import com.readforce.repository.LiteratureQuizAttemptRepository;
import com.readforce.repository.LiteratureQuizRepository;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiteratureService {
	
	private final LiteratureParagraphRepository literature_paragraph_repository;
	private final LiteratureQuizRepository literature_quiz_repository;
	private final LiteratureQuizAttemptRepository literature_quiz_attempt_repository;
	private final GeminiService gemini_service;
	private final MemberRepository member_repository;
	
	// LiteratureParagraph -> GetLiteratureParagraph 변환
	private GetLiteratureParagraph transformEntity(LiteratureParagraph literature_paragraph) {
		
		GetLiteratureParagraph get_literature_paragraph = new GetLiteratureParagraph();
		get_literature_paragraph.setCategory(literature_paragraph.getCategory());
		get_literature_paragraph.setContent(literature_paragraph.getContent());
		get_literature_paragraph.setLevel(literature_paragraph.getLevel());
		get_literature_paragraph.setLiterature_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_no());
		get_literature_paragraph.setLiterature_paragraph_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_paragraph_no());
		get_literature_paragraph.setTitle(literature_paragraph.getLiterature().getTitle());
		
		return get_literature_paragraph;
		
	}

	// 타입에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByType(String type, String order_by) {
		
		List<LiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeOrderByAsc(type);
				break;
				
			default:
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeOrderByDesc(type);
				
		}
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		List<GetLiteratureParagraph> get_literature_paragraph_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			get_literature_paragraph_list.add(transformEntity(literature_paragraph));
			
		}
		
		return get_literature_paragraph_list;
		
	}

	// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByTypeAndLevel(
			String type,
			String level,
			String order_by
	) {
		
		List<LiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelOrderByAsc(type, level);
				break;
			
			default :
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelOrderByDesc(type, level);
		}
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		List<GetLiteratureParagraph> get_literature_paragraph_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			get_literature_paragraph_list.add(transformEntity(literature_paragraph));
			
		}
		
		return get_literature_paragraph_list;
		
	}

	// 타입과 난이도, 카테고리에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategory(
			String type, 
			String level,
			String category, 
			String order_by
	) {
		
		List<LiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByAsc(type, level, category);
				break;
			
			default :
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByDesc(type, level, category);
		
		}
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		List<GetLiteratureParagraph> get_literature_paragraph_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			get_literature_paragraph_list.add(transformEntity(literature_paragraph));
			
		}
		
		return get_literature_paragraph_list;
		
	}

	// 문학 문제 가져오기
	@Transactional(readOnly = true)
	public GetLiteratureQuiz getLiteratureQuizObject(Long literature_paragraph_no, Long literature_no) {

		LiteratureQuiz literature_quiz = literature_quiz_repository.findByLiteratureParagraphNoAndLiteratureNo(literature_paragraph_no, literature_no);
		
		GetLiteratureQuiz get_literature_quiz = new GetLiteratureQuiz();
		get_literature_quiz.setLiterature_no(literature_quiz.getLiterature_no());
		get_literature_quiz.setQuestion_text(literature_quiz.getQuestion_text());
		get_literature_quiz.setChoice1(literature_quiz.getChoice1());
		get_literature_quiz.setChoice2(literature_quiz.getChoice2());
		get_literature_quiz.setChoice3(literature_quiz.getChoice3());
		get_literature_quiz.setChoice4(literature_quiz.getChoice4());
		get_literature_quiz.setCorrect_answer_index(literature_quiz.getCorrect_answer_index());
		get_literature_quiz.setExplanation(literature_quiz.getExplanation());
		get_literature_quiz.setLiterature_paragraph_no(literature_quiz.getLiterature_paragraph_no());
		get_literature_quiz.setLiterature_quiz_no(literature_quiz.getLiterature_quiz_no());
		get_literature_quiz.setScore(literature_quiz.getScore());
		get_literature_quiz.setContent(literature_quiz.getLiterature_paragraph().getContent());
		get_literature_quiz.setTitle(literature_quiz.getLiterature_paragraph().getLiterature().getTitle());
		
		return get_literature_quiz;
		
	}
	
	// 문학 문제 생성(문학 문단에 해당하는 문제가 없을 시 생성)
	public void generatedCreativeLiteratureQuizByGemini() {
		
		log.info("Gemini 문학 문제 생성 시작");
		
		// 문학 문단에 해당하는 문제가 없는 문학 문단 리스트 조회
		List<LiteratureParagraph> no_quiz_literature_paragraph_list = literature_paragraph_repository.findLiteratureParagraphListWithoutLiteratureQuiz();
		
		log.warn("1");
		
		int count = 0;
		
		for(LiteratureParagraph literature_paragraph : no_quiz_literature_paragraph_list) {
			
			try {
				
				LiteratureQuiz literature_quiz = gemini_service.generateCreativeLiteratureQuiz(literature_paragraph);
				
				literature_quiz_repository.save(literature_quiz);
				
				log.info("문학 문제 생성 완료: {}", literature_quiz.getQuestion_text());
				
				count++;
				
				Thread.sleep(9000);
				
			} catch(Exception exception) {
				
				log.warn("문학 문제 생성 실패: {}", exception.getMessage());
				
			}
			
		}

		log.info("최종 문학 문제 생성 갯수: {}", count);

	}
	
	// 사용자가 푼 문학 문제 저장
//	@Transactional
//	public void saveMemberSolvedLiteratureQuiz(SaveMemberSolvedLiteratureQuiz save_member_solved_literature_quiz, String email) {
//		
//		// 사용자가 정답을 입력했는지 확인
//		LiteratureQuiz literature_quiz = literature_quiz_repository.findById(save_member_solved_literature_quiz.getLiterature_quiz_no())
//				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.LITERATURE_QUIZ_NOT_FOUND));
//		
//		boolean is_correct = false;
//		
//		if(save_member_solved_literature_quiz.getSelected_option_index() == literature_quiz.getCorrect_answer_index()) {
//			
//			is_correct = true;
//			
//		}
//		
//		// 복합키 생성
//		LiteratureQuizAttemptId literature_quiz_attempt_id = new LiteratureQuizAttemptId();
//		literature_quiz_attempt_id.setEmail(email);
//		literature_quiz_attempt_id.setLiterature_quiz_no(literature_quiz.getLiterature_quiz_no());
//		
//		// 사용자가 푼 문학 문제 엔티티 생성
//		LiteratureQuizAttempt literature_quiz_attempt = new LiteratureQuizAttempt();
//		literature_quiz_attempt.setLiterature_quiz_attempt_id(literature_quiz_attempt_id);
//		literature_quiz_attempt.setIs_correct(is_correct);
//		literature_quiz_attempt.setSelected_option_index(save_member_solved_literature_quiz.getSelected_option_index());
//		
//		literature_quiz_attempt_repository.save(literature_quiz_attempt);
//		
//	}
	@Transactional
	public void saveMemberSolvedLiteratureQuiz(SaveMemberSolvedLiteratureQuiz dto, String email) {

	    // 퀴즈 조회
	    LiteratureQuiz quiz = literature_quiz_repository.findById(dto.getLiterature_quiz_no())
	            .orElseThrow(() -> new ResourceNotFoundException(MessageCode.LITERATURE_QUIZ_NOT_FOUND));

	    // 정답 여부 판별
	    boolean isCorrect = dto.getSelected_option_index() == quiz.getCorrect_answer_index();

	    // member 조회
	    Member member = member_repository.findById(email)
	            .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

		// 복합키 생성
		LiteratureQuizAttemptId literature_quiz_attempt_id = new LiteratureQuizAttemptId();
		literature_quiz_attempt_id.setEmail(email);
		literature_quiz_attempt_id.setLiterature_quiz_no(quiz.getLiterature_quiz_no());

	    // 엔티티 생성 및 필드 설정
	    LiteratureQuizAttempt attempt = new LiteratureQuizAttempt();
	    attempt.setLiterature_quiz_attempt_id(literature_quiz_attempt_id);
	    attempt.setMember(member); // 필수
	    attempt.setLiterature_quiz(quiz); // 필수
	    attempt.setIs_correct(isCorrect);
	    attempt.setSelected_option_index(dto.getSelected_option_index());

	    literature_quiz_attempt_repository.save(attempt);
	}

	// 사용자가 풀은 문학 문제 삭제하기
	@Transactional
	public void deleteMemberSolvedLiteratureQuiz(String email, Long literature_quiz_no) {

		// 복합키 생성
		LiteratureQuizAttemptId literature_quiz_attempt_id = new LiteratureQuizAttemptId();
		literature_quiz_attempt_id.setEmail(email);
		literature_quiz_attempt_id.setLiterature_quiz_no(literature_quiz_no);
		
		// 삭제
		literature_quiz_attempt_repository.deleteById(literature_quiz_attempt_id);	
		
	}



	

}
