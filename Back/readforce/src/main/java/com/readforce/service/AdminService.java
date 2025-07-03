package com.readforce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.LiteratureDto.AddLiteratureQuizAttempt;
import com.readforce.dto.LiteratureDto.GetLiteratureByAdmin;
import com.readforce.dto.LiteratureDto.GetLiteratureParagraphByAdmin;
import com.readforce.dto.LiteratureDto.GetLiteratureQuizByAdmin;
import com.readforce.dto.LiteratureDto.LiteratureByAdmin;
import com.readforce.dto.LiteratureDto.LiteratureParagraphByAdmin;
import com.readforce.dto.MemberDto.GetAttendance;
import com.readforce.dto.MemberDto.MemberObjectByAdmin;
import com.readforce.dto.MemberDto.ModifyByAdmin;
import com.readforce.dto.MemberDto.SignUpByAdmin;
import com.readforce.dto.NewsDto.AddNewsQuizAttempt;
import com.readforce.dto.NewsDto.GetLiteratureQuizAttemptListByEmail;
import com.readforce.dto.NewsDto.GetNewsQuizAttemptByEmail;
import com.readforce.dto.NewsDto.NewsByAdmin;
import com.readforce.dto.NewsDto.NewsQuizByAdmin;
import com.readforce.dto.PointDto;
import com.readforce.dto.PointDto.GetPoint;
import com.readforce.entity.Attendance;
import com.readforce.entity.Literature;
import com.readforce.entity.LiteratureParagraph;
import com.readforce.entity.LiteratureQuiz;
import com.readforce.entity.LiteratureQuizAttempt;
import com.readforce.entity.Member;
import com.readforce.entity.News;
import com.readforce.entity.NewsQuiz;
import com.readforce.entity.NewsQuizAttempt;
import com.readforce.entity.Point;
import com.readforce.enums.MessageCode;
import com.readforce.exception.DuplicateException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.id.LiteratureParagraphId;
import com.readforce.id.LiteratureQuizAttemptId;
import com.readforce.id.NewsQuizAttemptId;
import com.readforce.repository.AttendanceRepository;
import com.readforce.repository.LiteratureParagraphRepository;
import com.readforce.repository.LiteratureQuizAttemptRepository;
import com.readforce.repository.LiteratureQuizRepository;
import com.readforce.repository.LiteratureRepository;
import com.readforce.repository.MemberRepository;
import com.readforce.repository.NewsQuizAttemptRepository;
import com.readforce.repository.NewsQuizRepository;
import com.readforce.repository.NewsRepository;
import com.readforce.repository.PointRepository;

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
	private final NewsQuizAttemptRepository news_quiz_attempt_repository;
	private final LiteratureQuizAttemptRepository literature_quiz_attempt_repository;
	private final AttendanceRepository attendance_repository;
	private final PointRepository point_repository;

	// 전체 회원 정보 불러오기(최신순)
	@Transactional(readOnly = true)
	public List<MemberObjectByAdmin> getAllMemberList() {

		// 모든 회원 정보 조회
		List<Member> all_member_list = member_repository.findAllOrderByCreatedDateDesc();
		
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
	
	// 회원 삭제
	@Transactional
	public void deleteMemberByEmail(String email) {
	    // 1. 뉴스 퀴즈 풀이 기록 삭제
		news_quiz_attempt_repository.deleteByEmail(email);

	    // 2. 문학 퀴즈 풀이 기록 삭제
		literature_quiz_attempt_repository.deleteByEmail(email);

	    // 3. 출석 기록 삭제
		attendance_repository.deletebyEmail(email);
		
		// 4. 포인트 기록 삭제
		point_repository.deleteByEmail(email);
		
		// 5. 계정 삭
		member_repository.deleteByEmail(email);
		
	}

	// 회원 가입
	@Transactional
	public void addMember(SignUpByAdmin sign_up_by_admin) {
		
		Member member = new Member();
		member.setEmail(sign_up_by_admin.getEmail());
		member.setPassword(password_encoder.encode(sign_up_by_admin.getPassword()));
		member.setNickname(sign_up_by_admin.getNickname());
		member.setBirthday(sign_up_by_admin.getBirthday());
		member.setRole(sign_up_by_admin.getRole());
		
		
		Point point = new Point();
		point.setEmail(sign_up_by_admin.getEmail());
		
		// 포인트 테이블 생성
		point_repository.save(point);		
		
		member_repository.save(member);
		
	}

	// 전체 뉴스 가져오기(뉴스 번호순)
	@Transactional(readOnly = true)
	public List<NewsByAdmin> getAllNewsList() {
		
		List<News> news_list = news_repository.findAllOrderBy();
		
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

		List<NewsQuiz> news_quiz_list = news_quiz_repository.findAllOrderByNewsQuizNoDesc();
		
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

	// 전체 문학 가져오기(문학 번호순)
	@Transactional(readOnly = true)
	public List<GetLiteratureByAdmin> getAllLiteratureList() {

		List<Literature> literature_list = literature_repository.findAllOrderByLiteratureNoDesc();
		
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
		
		// 문학 엔티티 조회
		Literature literature = literature_repository.findById(literature_paragraph_by_admin.getLiterature_no())
		        .orElseThrow(() -> new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND));
		
		// 문학 문단 엔티티 생성
		LiteratureParagraph literature_paragraph = new LiteratureParagraph();
		literature_paragraph.setLiterature_paragraph_id(literature_paragraph_id);
		literature_paragraph.setCategory(literature_paragraph_by_admin.getCategory());
		literature_paragraph.setContent(literature_paragraph_by_admin.getContent());
		literature_paragraph.setLevel(literature_paragraph_by_admin.getLevel());
		
		// 문학 연결
	    literature_paragraph.setLiterature(literature);
		
		literature_paragraph_repository.save(literature_paragraph);
	}

	// 전체 문학 문단 리스트 가져오기(문학 문단 번호순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraphByAdmin> getAllLiteratureParagraphList() {
		
		List<LiteratureParagraph> literature_paragraph_list = literature_paragraph_repository.findAllOrderByLiteratureParagraphNoAsc();
		
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
	
	// 전체 문학 퀴즈 가져오기(문학 문제 번호 순)
	@Transactional(readOnly = true)
	public List<GetLiteratureQuizByAdmin> getAllLiteratureQuizList() {

		List<LiteratureQuiz> literature_quiz_list = literature_quiz_repository.findAllOrderByLiteratureQuizNoDesc();
		
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

//	// 사용자가 풀은 뉴스 문제 저장
//	@Transactional
//	public void saveMemberSolvedNewsQuiz(AddNewsQuizAttempt add_news_quiz_attempt) {
//		
//		// 사용자가 정답을 입력했는지 확인
//		NewsQuiz news_quiz = news_quiz_repository.findById(add_news_quiz_attempt.getNews_quiz_no())
//				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND));
//		
//		boolean is_correct = false;
//		
//		if(news_quiz.getCorrect_answer_index() == add_news_quiz_attempt.getSelected_option_index()) {
//			
//			is_correct = true;
//			
//		}
//		
//		// 복합키 생성
//		NewsQuizAttemptId news_quiz_attempt_id = new NewsQuizAttemptId();
//		news_quiz_attempt_id.setEmail(add_news_quiz_attempt.getEmail());
//		news_quiz_attempt_id.setNews_quiz_no(add_news_quiz_attempt.getNews_quiz_no());
//		
//		// 엔티티 생성
//		NewsQuizAttempt news_quiz_attempt = new NewsQuizAttempt();
//		news_quiz_attempt.setNews_quiz_attempt_id(news_quiz_attempt_id);
//		news_quiz_attempt.setIs_correct(is_correct);
//		news_quiz_attempt.setSelected_option_index(add_news_quiz_attempt.getSelected_option_index());
//		
//		// 저장
//		news_quiz_attempt_repository.save(news_quiz_attempt);
//		
//	}
// 제가 만들ㄹㄷㅈ러ㅕㅈㄷ 이렇게 바꾸니까 됩니당
	@Transactional
	public void saveMemberSolvedNewsQuiz(AddNewsQuizAttempt add_news_quiz_attempt) {
		
		// 사용자가 정답을 입력했는지 확인
		NewsQuiz news_quiz = news_quiz_repository.findById(add_news_quiz_attempt.getNews_quiz_no())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.NEWS_QUIZ_NOT_FOUND));
		
		boolean is_correct = false;
		
		if(news_quiz.getCorrect_answer_index() == add_news_quiz_attempt.getSelected_option_index()) {
			
			is_correct = true;
			
		}
		
		// 회원 조회
		Member member = member_repository.findByEmail(add_news_quiz_attempt.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		// 복합키 생성
		NewsQuizAttemptId news_quiz_attempt_id = new NewsQuizAttemptId();
		news_quiz_attempt_id.setEmail(add_news_quiz_attempt.getEmail());
		news_quiz_attempt_id.setNews_quiz_no(add_news_quiz_attempt.getNews_quiz_no());
		
		// 엔티티 생성
		NewsQuizAttempt news_quiz_attempt = new NewsQuizAttempt();
		news_quiz_attempt.setNews_quiz_attempt_id(news_quiz_attempt_id);
		news_quiz_attempt.setIs_correct(is_correct);
		news_quiz_attempt.setSelected_option_index(add_news_quiz_attempt.getSelected_option_index());
		news_quiz_attempt.setNews_quiz(news_quiz);
		news_quiz_attempt.setMember(member);
		
		// 저장
		news_quiz_attempt_repository.save(news_quiz_attempt);
		
	}

	// 뉴스 기사 문제 기록 삭제
	@Transactional
	public void deleteNewsQuizAttempt(String email, Long news_quiz_no) {

		// 복합키 생성
		NewsQuizAttemptId news_quiz_attempt_id = new NewsQuizAttemptId();
		news_quiz_attempt_id.setEmail(email);
		news_quiz_attempt_id.setNews_quiz_no(news_quiz_no);
		
		news_quiz_attempt_repository.deleteById(news_quiz_attempt_id);
		
	}

	// 이메일에 해당하는 뉴스 퀴즈 풀이 기록 가져오기(최신순)
	@Transactional(readOnly = true)
	public List<GetNewsQuizAttemptByEmail> getNewsQuizAttempListtByEmail(String email) {
		
		List<NewsQuizAttempt> news_quiz_attempt_list = news_quiz_attempt_repository.findByEmailOrderByCreatedDateDesc(email);
		
		if(news_quiz_attempt_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.NEWS_QUIZ_ATTEMPT_NOT_FOUND);
			
		}
		
		List<GetNewsQuizAttemptByEmail> get_news_quiz_attempt_by_email_list = new ArrayList<>();
		
		for(NewsQuizAttempt news_quiz_attempt : news_quiz_attempt_list) {
			
			GetNewsQuizAttemptByEmail get_news_quiz_attempt_by_email = new GetNewsQuizAttemptByEmail();
			get_news_quiz_attempt_by_email.setEmail(news_quiz_attempt.getNews_quiz_attempt_id().getEmail());
			get_news_quiz_attempt_by_email.setNews_quiz_no(news_quiz_attempt.getNews_quiz_attempt_id().getNews_quiz_no());
			get_news_quiz_attempt_by_email.setIs_correct(news_quiz_attempt.getIs_correct());
			get_news_quiz_attempt_by_email.setSelected_option_index(news_quiz_attempt.getSelected_option_index());
			get_news_quiz_attempt_by_email.setCreated_date(news_quiz_attempt.getCreated_date());
			
			get_news_quiz_attempt_by_email_list.add(get_news_quiz_attempt_by_email);			
			
		}
		
		return get_news_quiz_attempt_by_email_list;

	}

	// 이메일에 해당하는 문학 퀴즈 풀이 기록 가져오기(최신순)
	public List<GetLiteratureQuizAttemptListByEmail> getLiteratureQuizAttemptListByEmail(String email) {

		List<LiteratureQuizAttempt> literature_quiz_attempt_list = 
				literature_quiz_attempt_repository.findByEmailOrderByCreatedDateDesc(email);
		
		if(literature_quiz_attempt_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_QUIZ_ATTEMPT_NOT_FOUND);
			
		}
		
		List<GetLiteratureQuizAttemptListByEmail> get_literature_quiz_attempt_list = new ArrayList<>();
		
		for(LiteratureQuizAttempt literature_quiz_attempt : literature_quiz_attempt_list) {
			
			GetLiteratureQuizAttemptListByEmail get_literature_quiz_attempt = new GetLiteratureQuizAttemptListByEmail();
			get_literature_quiz_attempt.setEmail(literature_quiz_attempt.getLiterature_quiz_attempt_id().getEmail());
			get_literature_quiz_attempt.setLiterature_quiz_no(literature_quiz_attempt.getLiterature_quiz_attempt_id().getLiterature_quiz_no());
			get_literature_quiz_attempt.setIs_correct(literature_quiz_attempt.getIs_correct());
			get_literature_quiz_attempt.setSelected_option_index(literature_quiz_attempt.getSelected_option_index());
			get_literature_quiz_attempt.setCreated_date(literature_quiz_attempt.getCreated_date());
		
			get_literature_quiz_attempt_list.add(get_literature_quiz_attempt);
			
		}
		
		return get_literature_quiz_attempt_list;
		
	}


	// 사용자가 풀은 문학 문제 기록 추가
	@Transactional
	public void addLiteratureQuizAttempt(AddLiteratureQuizAttempt add_literature_quiz_attempt) {
		
		// 사용자가 정답을 입력했는지 확인
		LiteratureQuiz literature_quiz = literature_quiz_repository.findById(add_literature_quiz_attempt.getLiterature_quiz_no())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.LITERATURE_QUIZ_NOT_FOUND));
		
		boolean is_correct = false;
		
		if(literature_quiz.getCorrect_answer_index() == add_literature_quiz_attempt.getSelected_option_index()) {
			
			is_correct = true;
			
		}
		
		// 회원 조회
		Member member = member_repository.findByEmail(add_literature_quiz_attempt.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		// 복합키 생성
		LiteratureQuizAttemptId literature_quiz_attempt_id = new LiteratureQuizAttemptId();
		literature_quiz_attempt_id.setEmail(add_literature_quiz_attempt.getEmail());
		literature_quiz_attempt_id.setLiterature_quiz_no(add_literature_quiz_attempt.getLiterature_quiz_no());
		
		// 엔티티 생성
		LiteratureQuizAttempt literature_quiz_attempt = new LiteratureQuizAttempt();
		literature_quiz_attempt.setLiterature_quiz_attempt_id(literature_quiz_attempt_id);
		literature_quiz_attempt.setIs_correct(is_correct);
		literature_quiz_attempt.setSelected_option_index(add_literature_quiz_attempt.getSelected_option_index());
		literature_quiz_attempt.setMember(member);
		literature_quiz_attempt.setLiterature_quiz(literature_quiz);
		
		// 추가
		literature_quiz_attempt_repository.save(literature_quiz_attempt);
		
	}

	// 문학 퀴즈 풀이 기록 삭제
	@Transactional
	public void deleteLiteratureQuizAttempt(String email, Long literature_quiz_no) {
		
		// 복합키 생성
		LiteratureQuizAttemptId literature_quiz_attempt_id = new LiteratureQuizAttemptId();
		literature_quiz_attempt_id.setEmail(email);
		literature_quiz_attempt_id.setLiterature_quiz_no(literature_quiz_no);
		
		// 삭제
		literature_quiz_attempt_repository.deleteById(literature_quiz_attempt_id);
		
	}

	// 출석 추가
	@Transactional
	public void addAttendance(String email, LocalDate date) {

		// 회원 조회
		Member member = member_repository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		// 날짜가 지정되지 않은 경우 현재 날짜 사용
		LocalDateTime attendance_date_time = (date != null) ? date.atStartOfDay() : LocalDateTime.now();
		
		LocalDateTime start_of_day = attendance_date_time.toLocalDate().atStartOfDay();
		LocalDateTime end_of_day = attendance_date_time.toLocalDate().atTime(LocalTime.MAX);
		
		// 해당 날짜에 이미 출석 기록이 있는지 확인
		if(attendance_repository.existsByEmailAndCreatedDateBetween(email, start_of_day, end_of_day)) {
			
			throw new DuplicateException(MessageCode.ATTENDANCE_DATE_DUPLICATE);
			
		}
		
		// 엔티티 생성
		Attendance attendance = new Attendance();
		attendance.setMember(member);
		attendance.setEmail(email);
		attendance.setCreated_date(attendance_date_time);
		
		attendance_repository.save(attendance);

	}

	// 이메일에 해당하는 출석 불러오기(최신순)
	@Transactional(readOnly = true)
	public List<GetAttendance> getAttendanceListByEmail(String email) {
		
		List<Attendance> attendance_list = attendance_repository.findByEmailOrderByCreatedDateDesc(email);
		
		if(attendance_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.ATTENDANCE_DATE_NOT_FOUND);
			
		}
		
		List<GetAttendance> get_attendance_list = new ArrayList<>();
		
		for(Attendance attendance : attendance_list) {
			
			GetAttendance get_attendance = new GetAttendance();
			get_attendance.setEmail(attendance.getEmail());
			get_attendance.setAttendance_no(attendance.getAttendance_no());
			get_attendance.setCreated_date(attendance.getCreated_date());
			
			get_attendance_list.add(get_attendance);
			
		}
		
		return get_attendance_list;
		
	}

	// 출석 삭제
	@Transactional
	public void deleteAttendance(Long attendance_no) {

		attendance_repository.deleteById(attendance_no);
		
	}

	// 점수 추가
	@Transactional
	public void updatePoint(PointDto.UpdatePoint update_point) {

		// 포인트 조회
		Point point = point_repository.findByEmail(update_point.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND));
				
		
		// 총 점수 수정
		if(update_point.getTotal() != null) {
			
			point.setTotal(update_point.getTotal());
			
		}
		
		// 한국어 뉴스 점수 수정
		if(update_point.getKorean_news() != null) {
			
			point.setKorean_news(update_point.getKorean_news());
			
		}
		
		// 영어 뉴스 점수 수정
		if(update_point.getEnglish_news() != null) {
			
			point.setEnglish_news(update_point.getEnglish_news());
			
		}
		
		// 일본어 뉴스 점수 수정
		if(update_point.getJapanese_news() != null) {
			
			point.setJapanese_news(update_point.getJapanese_news());
			
		}
		
		// 소설 점수 수정
		if(update_point.getNovel() != null) {
		
			point.setNovel(update_point.getNovel());
			
		}
		
		// 동화 점수 수정
		if(update_point.getFairytale() != null) {
			
			point.setFairytale(update_point.getFairytale());
			
		}
		
	}

	// 점수 추가
	@Transactional
	public void addPoint(@Valid PointDto.AddPoint add_point) {

		Point point = new Point();
		point.setEmail(add_point.getEmail());
		
		point_repository.save(point);
		
	}

	// 점수 삭제
	@Transactional
	public void deletePoint(String email) {

		point_repository.deleteByEmail(email);
		
	}

	// 전체 점수 불러오기(최신순)
	@Transactional(readOnly = true)
	public List<GetPoint> getAllPointList() {

		List<Point> point_list = point_repository.findAllOrderByCreatedDateDesc();
		
		if(point_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND);
			
		}
		
		List<GetPoint> get_point_list = new ArrayList<>();
		
		for(Point point : point_list) {
			
			GetPoint get_point = new GetPoint();
			get_point.setEmail(point.getEmail());
			get_point.setTotal(point.getTotal());
			get_point.setKorean_news(point.getKorean_news());
			get_point.setEnglish_news(point.getEnglish_news());
			get_point.setJapanese_news(point.getJapanese_news());
			get_point.setNovel(point.getNovel());
			get_point.setFairytale(point.getFairytale());
			get_point.setCreated_date(point.getCreated_date());
			get_point.setLast_modified_date(point.getLast_modified_date());
			
			get_point_list.add(get_point);
			
		}
		
		return get_point_list;
		
	}

	// 회원 정보 불러오기
	@Transactional(readOnly = true)
	public MemberObjectByAdmin getMemberInfoObject(String email) {

		Member member = member_repository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

		MemberObjectByAdmin member_object_by_admin = new MemberObjectByAdmin();
		
		member_object_by_admin.setEmail(member.getEmail());
		member_object_by_admin.setBirthday(member.getBirthday());
		member_object_by_admin.setNickname(member.getNickname());
		member_object_by_admin.setProfile_image_url(member.getProfile_image_url());
		member_object_by_admin.setRole(member.getRole());
		member_object_by_admin.setSocial_provider(member.getSocial_provider());
		member_object_by_admin.setSocial_provider_id(member.getSocial_provider_id());
		member_object_by_admin.setStatus(member.getStatus());
		member_object_by_admin.setCreate_date(member.getCreate_date());
		member_object_by_admin.setLast_modified_date(member.getLast_modified_date());
		member_object_by_admin.setWithdraw_date(member.getWithdraw_date());
		
		return member_object_by_admin;

	}

	// 회원 포인트 불러오기
	@Transactional(readOnly = true)
	public GetPoint getMemberPointObject(String email) {

		Point point = point_repository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND));
		
		GetPoint get_point = new GetPoint();
		
		get_point.setEmail(point.getEmail());
		get_point.setTotal(point.getTotal());
		get_point.setEnglish_news(point.getEnglish_news());
		get_point.setJapanese_news(point.getJapanese_news());
		get_point.setKorean_news(point.getKorean_news());
		get_point.setNovel(point.getNovel());
		get_point.setFairytale(point.getFairytale());
		get_point.setCreated_date(point.getCreated_date());
		get_point.setLast_modified_date(point.getLast_modified_date());
		
		return get_point;

	}

	// 회원 출석 불러오기
	@Transactional(readOnly = true)
	public List<GetAttendance> getMemberAttendanceList(String email) {
		
		List<Attendance> attendance_list = attendance_repository.findByEmail(email);
		
		if(attendance_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.ATTENDANCE_DATE_NOT_FOUND);
			
		}
		
		List<GetAttendance> get_attendnce_list = new ArrayList<>();
		
		for(Attendance attendance : attendance_list) {
			
			GetAttendance get_attendance = new GetAttendance();
			get_attendance.setAttendance_no(attendance.getAttendance_no());
			get_attendance.setEmail(attendance.getEmail());
			get_attendance.setCreated_date(attendance.getCreated_date());
			
			get_attendnce_list.add(get_attendance);

		}

		return get_attendnce_list;

	}

	// 포인트 수정 - 김기찬
	@Transactional
	public void incrementPoint(PointDto.IncrementPoint dto) {
	    Point point = point_repository.findByEmail(dto.getEmail())
	        .orElseThrow(() -> new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND));

	    switch (dto.getCategory()) {
	        case "korean_news" -> point.setKorean_news(point.getKorean_news() + dto.getDelta());
	        case "english_news" -> point.setEnglish_news(point.getEnglish_news() + dto.getDelta());
	        case "japanese_news" -> point.setJapanese_news(point.getJapanese_news() + dto.getDelta());
	        case "novel" -> point.setNovel(point.getNovel() + dto.getDelta());
	        case "fairytale" -> point.setFairytale(point.getFairytale() + dto.getDelta());
	        default -> throw new IllegalArgumentException("지원하지 않는 카테고리입니다: " + dto.getCategory());
	    }

	    // 총 점수도 갱신
	    double total = point.getKorean_news() + point.getEnglish_news()
	              + point.getJapanese_news() + point.getNovel() + point.getFairytale();
	    point.setTotal(total);
	}


	
	
}
