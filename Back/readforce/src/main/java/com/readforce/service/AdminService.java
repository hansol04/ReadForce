package com.readforce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.LiteratureDto.GetLiteratureByAdmin;
import com.readforce.dto.LiteratureDto.GetLiteratureParagraphByAdmin;
import com.readforce.dto.LiteratureDto.GetLiteratureQuizByAdmin;
import com.readforce.dto.LiteratureDto.LiteratureByAdmin;
import com.readforce.dto.LiteratureDto.LiteratureParagraphByAdmin;
import com.readforce.dto.MemberDto.MemberObjectByAdmin;
import com.readforce.dto.MemberDto.ModifyByAdmin;
import com.readforce.dto.MemberDto.SignUpByAdmin;
import com.readforce.dto.NewsDto.NewsByAdmin;
import com.readforce.dto.NewsDto.NewsQuizByAdmin;
import com.readforce.entity.Literature;
import com.readforce.entity.LiteratureParagraph;
import com.readforce.entity.LiteratureQuiz;
import com.readforce.entity.Member;
import com.readforce.entity.News;
import com.readforce.entity.NewsQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.id.LiteratureParagraphId;
import com.readforce.repository.LiteratureParagraphRepository;
import com.readforce.repository.LiteratureQuizRepository;
import com.readforce.repository.LiteratureRepository;
import com.readforce.repository.MemberRepository;
import com.readforce.repository.NewsQuizRepository;
import com.readforce.repository.NewsRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final MemberRepository member_repository;
	private final PasswordEncoder password_encoder;
	private final NewsRepository news_repository;
	private final NewsQuizRepository news_quiz_repository;
	private final LiteratureRepository literature_repository;
	private final LiteratureParagraphRepository literature_paragraph_repository;
	private final LiteratureQuizRepository literature_quiz_repository;

	// 전체 회원 정보 불러오기
	@Transactional(readOnly = true)
	public List<MemberObjectByAdmin> getAllMemberList() {

		// 모든 회원 정보 조회
		List<Member> all_member_list = member_repository.findAll();
		
		if(all_member_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND);
			
		}
		
		List<MemberObjectByAdmin> get_member_object_by_admin_list = new ArrayList<>();
		
		for(Member member : all_member_list) {
			
			MemberObjectByAdmin get_member_object_by_admin = new MemberObjectByAdmin();
			get_member_object_by_admin.setEmail(member.getEmail());
			get_member_object_by_admin.setBirthday(member.getBirthday());
			get_member_object_by_admin.setNickname(member.getNickname());
			get_member_object_by_admin.setProfile_image_url(member.getProfile_image_url());
			get_member_object_by_admin.setSocial_provider(member.getSocial_provider());
			get_member_object_by_admin.setSocial_provider_id(member.getSocial_provider_id());
			get_member_object_by_admin.setRole(member.getRole());
			get_member_object_by_admin.setStatus(member.getStatus());
			get_member_object_by_admin.setCreate_date(member.getCreate_date());
			get_member_object_by_admin.setLast_modified_date(member.getLast_modified_date());
			get_member_object_by_admin.setWithdraw_date(member.getWithdraw_date());
			
			get_member_object_by_admin_list.add(get_member_object_by_admin);
			
		}
		
		return get_member_object_by_admin_list;

	}

	// 회원 정보 수정
	@Transactional
	public void modifyInfo(ModifyByAdmin modify_by_admin) {
		
		// 회원 조회
		Member member = member_repository.findByEmail(modify_by_admin.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

		// 닉네임 변경
		if(modify_by_admin.getNickname() != null) {
			
			member.setNickname(modify_by_admin.getNickname());
			
		}
		
		// 생일 변경
		if(modify_by_admin.getBirthday() != null) {
			
			member.setBirthday(modify_by_admin.getBirthday());
			
		}
		
		// 상태 변경
		if(modify_by_admin.getStatus() != null) {
			
			member.setStatus(modify_by_admin.getStatus());
			
		}
		
		// 역할 변경
		if(modify_by_admin.getRole() != null) {
			
			member.setRole(modify_by_admin.getRole());
			
		}
		
	}

	// 회원 가입
	@Transactional
	public void addMember(@Valid SignUpByAdmin sign_up_by_admin) {
		
		Member member = new Member();
		member.setEmail(sign_up_by_admin.getEmail());
		member.setPassword(password_encoder.encode(sign_up_by_admin.getPassword()));
		member.setNickname(sign_up_by_admin.getNickname());
		member.setBirthday(sign_up_by_admin.getBirthday());
		member.setRole(sign_up_by_admin.getRole());
		
		member_repository.save(member);
		
	}

	// 전체 뉴스 가져오기
	@Transactional(readOnly = true)
	public List<NewsByAdmin> getAllNewsList() {
		
		List<News> news_list = news_repository.findAll();
		
		if(news_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_NOT_FOUND);
			
		}
		
		List<NewsByAdmin> news_by_admin_list = new ArrayList<>();
		
		for(News news : news_list) {
			
			NewsByAdmin news_by_admin = new NewsByAdmin();
			news_by_admin.setNews_no(news.getNews_no());
			news_by_admin.setTitle(news.getTitle());
			news_by_admin.setContent(news.getContent());
			news_by_admin.setCategory(news.getCategory());
			news_by_admin.setLanguage(news.getLanguage());
			news_by_admin.setLevel(news.getLevel());
			news_by_admin.setCreated_date(news.getCreated_date());
			
			news_by_admin_list.add(news_by_admin);
			
		}
		
		return news_by_admin_list;

	}

	// 뉴스 + 뉴스 문제 삭제
	@Transactional
	public void deleteNewsAndNewsQuizByNewsNo(Long news_no) {
	
		// 뉴스 문제 삭제
		news_quiz_repository.deleteByNewsNo(news_no);
		
		// 뉴스 삭제
		news_repository.deleteById(news_no);
		
	}
	
	// 뉴스 문제 삭제
	@Transactional
	public void deleteNewsQuiz(Long news_quiz_no) {
		
		// 뉴스 문제 삭제
		news_quiz_repository.deleteById(news_quiz_no);
		
	}

	// 전체 뉴스 문제 가져오기
	@Transactional(readOnly = true)
	public List<NewsQuizByAdmin> getAllNewsQuizList() {

		List<NewsQuiz> news_quiz_list = news_quiz_repository.findAll();
		
		if(news_quiz_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND);
			
		}
		
		List<NewsQuizByAdmin> news_quiz_by_admin_list = new ArrayList<>();
		
		for(NewsQuiz news_quiz : news_quiz_list) {
			
			NewsQuizByAdmin news_quiz_by_admin = new NewsQuizByAdmin();
			news_quiz_by_admin.setNews_quiz_no(news_quiz.getNews_quiz_no());
			news_quiz_by_admin.setQuestion_text(news_quiz.getQuestion_text());
			news_quiz_by_admin.setChoice1(news_quiz.getChoice1());
			news_quiz_by_admin.setChoice2(news_quiz.getChoice2());
			news_quiz_by_admin.setChoice3(news_quiz.getChoice3());
			news_quiz_by_admin.setChoice4(news_quiz.getChoice4());
			news_quiz_by_admin.setCorrect_answer_index(news_quiz.getCorrect_answer_index());
			news_quiz_by_admin.setExplanation(news_quiz.getExplanation());
			news_quiz_by_admin.setScore(news_quiz.getScore());
			news_quiz_by_admin.setNews_no(news_quiz.getNews_no());
			news_quiz_by_admin.setCreated_date(news_quiz.getCreated_date());
			
			news_quiz_by_admin_list.add(news_quiz_by_admin);
			
		}
		
		return news_quiz_by_admin_list;
		
	}

	// 문학 추가
	@Transactional
	public void addLiterature(LiteratureByAdmin literauture_by_admin) {
		
		Literature literature = new Literature();
		literature.setTitle(literauture_by_admin.getTitle());
		literature.setType(literauture_by_admin.getType());
		
		literature_repository.save(literature);
	
	}

	// 전체 문학 가져오기
	@Transactional(readOnly = true)
	public List<GetLiteratureByAdmin> getAllLiteratureList() {

		List<Literature> literature_list = literature_repository.findAll();
		
		if(literature_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		List<GetLiteratureByAdmin> get_literature_by_admin_list = new ArrayList<>();
		
		for(Literature literature : literature_list) {
			
			GetLiteratureByAdmin get_literature_by_admin = new GetLiteratureByAdmin();
			get_literature_by_admin.setLiterature_no(literature.getLiterature_no());
			get_literature_by_admin.setTitle(literature.getTitle());
			get_literature_by_admin.setType(literature.getType());
			get_literature_by_admin.setCreated_date(literature.getCreated_date());
			
			get_literature_by_admin_list.add(get_literature_by_admin);
			
		}
		
		return get_literature_by_admin_list;
		
	}

	// 문학 + 문학 문단 + 문학 문제 삭제
	public void deleteLiteratureAndLiteratureParagraphAndLiteratureQuizByLiteratureNo(Long literature_no) {
		
		// 문학 문단 조회
		LiteratureParagraph literature_paragraph = literature_paragraph_repository.findByLiterature_no(literature_no);
		
		if(literature_paragraph == null) {
			
			// 문학 문단 없음
			// 문학 삭제
			literature_repository.deleteById(literature_no);
			
		} else {
			
			// 문학 문단 있음
			// 문학 퀴즈 조회
			LiteratureQuiz literature_quiz = literature_quiz_repository.findByLiteratureParagraphNoAndLiteratureNo(
					literature_paragraph.getLiterature_paragraph_id().getLiterature_paragraph_no(),
					literature_paragraph.getLiterature_paragraph_id().getLiterature_no()
			);
			
			if(literature_quiz == null) {
				
				// 문학 퀴즈 없음
				// 문학 문단 삭제 
				literature_paragraph_repository.deleteById(literature_paragraph.getLiterature_paragraph_id());
				
				// 문학 삭제
				literature_repository.deleteById(literature_no);
				
			} else {
				
				// 문학 퀴즈 있음
				// 문학 퀴즈 삭제
				literature_quiz_repository.deleteById(literature_quiz.getLiterature_quiz_no());
				
				// 문학 문단 삭제 
				literature_paragraph_repository.deleteById(literature_paragraph.getLiterature_paragraph_id());
				
				// 문학 삭제
				literature_repository.deleteById(literature_no);
				
			}

		}
		
	}
	
	// 문학 문단 추가
	@Transactional
	public void addLiteratureParagraph(LiteratureParagraphByAdmin literature_paragraph_by_admin) {

		// 문학 번호에 해당하는 마지막 문학 문단 번호 불러오기
		Long last_literature_paragraph_no = literature_paragraph_repository.findLastLiteratureParagraphNoByLiteratureNo(literature_paragraph_by_admin.getLiterature_no());
		
		if(last_literature_paragraph_no == null) {
			
			last_literature_paragraph_no = 0L;
			
		}
		
		// 복합키 생성
		LiteratureParagraphId literature_paragraph_id = new LiteratureParagraphId();
		literature_paragraph_id.setLiterature_no(literature_paragraph_by_admin.getLiterature_no());
		literature_paragraph_id.setLiterature_paragraph_no(last_literature_paragraph_no + 1);
		
		// 문학 문단 엔티티 생성
		LiteratureParagraph literature_paragraph = new LiteratureParagraph();
		literature_paragraph.setLiterature_paragraph_id(literature_paragraph_id);
		literature_paragraph.setCategory(literature_paragraph_by_admin.getCategory());
		literature_paragraph.setContent(literature_paragraph_by_admin.getContent());
		literature_paragraph.setLevel(literature_paragraph_by_admin.getLevel());
		
		literature_paragraph_repository.save(literature_paragraph);
		
	}

	// 전체 문학 문단 리스트 가져오기
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraphByAdmin> getAllLiteratureParagraphList() {
		
		List<LiteratureParagraph> literature_paragraph_list = literature_paragraph_repository.findAll();
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_PARAGRAPH_NOT_FOUND);
			
		}
		
		List<GetLiteratureParagraphByAdmin> get_literature_paragraph_by_admin_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			GetLiteratureParagraphByAdmin get_literature_paragraph_by_admin = new GetLiteratureParagraphByAdmin();
			get_literature_paragraph_by_admin.setLiterature_paragraph_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_paragraph_no());
			get_literature_paragraph_by_admin.setLiterature_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_no());
			get_literature_paragraph_by_admin.setLevel(literature_paragraph.getLevel());
			get_literature_paragraph_by_admin.setContent(literature_paragraph.getContent());
			get_literature_paragraph_by_admin.setCategory(literature_paragraph.getCategory());
			get_literature_paragraph_by_admin.setCreated_date(literature_paragraph.getCreated_date());
			
			get_literature_paragraph_by_admin_list.add(get_literature_paragraph_by_admin);
			
		}
		
		return get_literature_paragraph_by_admin_list;

	}

	// 문학 문단 + 문학 문제 삭제
	@Transactional
	public void deleteLiteratureParagraphAndLiteratureByLiteratureParagraphNo(Long literature_paragraph_no, Long literature_no) {
		
		// 문학 문제 삭제
		literature_quiz_repository.deleteByLiteratureParagraphNoAndLiteratureNo(literature_paragraph_no, literature_no);
		
		// 문학 문단 복합키 생성
		LiteratureParagraphId literature_paragraph_id = new LiteratureParagraphId();
		literature_paragraph_id.setLiterature_no(literature_no);
		literature_paragraph_id.setLiterature_paragraph_no(literature_paragraph_no);
		
		// 문학 문단 삭제
		literature_paragraph_repository.deleteById(literature_paragraph_id);
		
	}
	
	// 전체 문학 퀴즈 가져오기
	@Transactional(readOnly = true)
	public List<GetLiteratureQuizByAdmin> getAllLiteratureQuizList() {

		List<LiteratureQuiz> literature_quiz_list = literature_quiz_repository.findAll();
		
		if(literature_quiz_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_QUIZ_NOT_FOUND);
			
		}
		
		List<GetLiteratureQuizByAdmin> get_literature_quiz_by_admin_list = new ArrayList<>();
		
		for(LiteratureQuiz literature_quiz : literature_quiz_list) {
			
			GetLiteratureQuizByAdmin get_literature_quiz_by_admin = new GetLiteratureQuizByAdmin();
			get_literature_quiz_by_admin.setLiterature_quiz_no(literature_quiz.getLiterature_quiz_no());
			get_literature_quiz_by_admin.setQuestion_text(literature_quiz.getQuestion_text());
			get_literature_quiz_by_admin.setChoice1(literature_quiz.getChoice1());
			get_literature_quiz_by_admin.setChoice2(literature_quiz.getChoice2());
			get_literature_quiz_by_admin.setChoice3(literature_quiz.getChoice3());
			get_literature_quiz_by_admin.setChoice4(literature_quiz.getChoice4());
			get_literature_quiz_by_admin.setCorrect_answer_index(literature_quiz.getCorrect_answer_index());
			get_literature_quiz_by_admin.setExplanation(literature_quiz.getExplanation());
			get_literature_quiz_by_admin.setLiterature_no(literature_quiz.getLiterature_no());
			get_literature_quiz_by_admin.setLiterature_paragraph_no(literature_quiz.getLiterature_paragraph_no());
			get_literature_quiz_by_admin.setScore(literature_quiz.getScore());
			get_literature_quiz_by_admin.setCreated_date(literature_quiz.getCreated_date());
			
			get_literature_quiz_by_admin_list.add(get_literature_quiz_by_admin);
			
		}
		
		return get_literature_quiz_by_admin_list;

	}

	// 문학 문제 삭제
	@Transactional
	public void deleteLiteratureQuiz(Long literature_quiz_no) {
		
		literature_quiz_repository.deleteById(literature_quiz_no);
		
	}



	
	
}
