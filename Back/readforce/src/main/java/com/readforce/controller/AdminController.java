package com.readforce.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;
import com.readforce.service.AdminService;
import com.readforce.service.LiteratureService;
import com.readforce.service.NewsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
	
	private final NewsService news_service;
	private final LiteratureService literature_service;
	private final AdminService admin_service;
	private final MemberRepository member_repository; // 기찬스추가

	// 회원 -------------------------------------------------------------------------
	// 전체 회원 목록 조회(최신순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-member-list")
	public ResponseEntity<List<MemberObjectByAdmin>> getAllMemberList(){
		
		List<MemberObjectByAdmin> member_list = admin_service.getAllMemberList();
		
		return ResponseEntity.status(HttpStatus.OK).body(member_list);
		
	}
	
	// 회원 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-member")
	public ResponseEntity<Map<String, String>> addMember(
			@Valid
			@RequestBody SignUpByAdmin sign_up_by_admin
	){
		
		admin_service.addMember(sign_up_by_admin);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS
		));
		
	}
	
	// 회원 정보 수정
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify-info")
	public ResponseEntity<Map<String, String>> modifyInfo(
			@Valid 
			@RequestBody ModifyByAdmin modify_by_admin
	){
		
		admin_service.modifyInfo(modify_by_admin);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MEMBER_INFO_MODIFY_SUCCESS
		));
		
	}
	
	// 회원 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-member-by-email")
	public ResponseEntity<Map<String, String>> deleteMemberByEmail(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		admin_service.deleteMemberByEmail(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_MEMBER_SUCCESS
		));
		
	}
	
	// 유저 정보 불러오기 - 기찬
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-user-info")
	public ResponseEntity<MemberObjectByAdmin> getUserInfoByEmail(@RequestParam("email") String email) {
	    Member member = member_repository.findByEmail(email)
	        .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

	    MemberObjectByAdmin dto = new MemberObjectByAdmin();
	    dto.setEmail(member.getEmail());
	    dto.setNickname(member.getNickname());
	    dto.setBirthday(member.getBirthday());
	    dto.setStatus(member.getStatus());
	    dto.setCreate_date(member.getCreate_date());
	    return ResponseEntity.ok(dto);
	}
	
	// 뉴스 관리 ---------------------------------------------------------------------
	// 뉴스 생성(언어 * 난이도 * 카테고리 수만큼 생성)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/generate-creative-news")
	public ResponseEntity<Map<String, String>> generateCreativeNews(){
		
		// 뉴스 생성
		news_service.generateCreativeNewsByGemini();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_CREATIVE_NEWS_SUCCESS));
		
	}

	// 전체 뉴스 불러오기(뉴스 번호순)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/get-all-news-list")
	public ResponseEntity<List<NewsByAdmin>> getAllNewsList(){
		
		List<NewsByAdmin> all_news_list = admin_service.getAllNewsList();
		
		return ResponseEntity.status(HttpStatus.OK).body(all_news_list);
		
	}
	
	// 뉴스 + 뉴스 문제 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-news-and-news-quiz-by-news-no")
	public ResponseEntity<Map<String, String>> deleteNewsAndNewsQuizByNewsNo(
			@RequestParam("news_no")
			@NotNull(message = MessageCode.NEWS_NO_NOT_NULL)
			Long news_no
	){
		
		// 뉴스 + 뉴스 문제 삭제
		admin_service.deleteNewsAndNewsQuizByNewsNo(news_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_NEWS_AND_NEWS_QUIZ_SUCCESS
		));
		
	}

	// 뉴스 문제 관리 ----------------------------------------------------------------------
	// 뉴스 문제 생성(뉴스에 해당하는 문제가 없을 경우 생성)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/generate-creative-news-quiz")
	public ResponseEntity<Map<String, String>> generateCreativeNewsQuiz(){
		
		// 뉴스 문제 생성
		news_service.generateCreativeNewsQuizByGemini();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_CREATIVE_NEWS_QUIZ_SUCCESS));
		
	}
	
	// 전체 뉴스 문제 불러오기(뉴스 문제 번호순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-news-quiz-list")
	public ResponseEntity<List<NewsQuizByAdmin>> getAllNewsQuizList(){
		
		List<NewsQuizByAdmin> news_quiz_by_admin_list = admin_service.getAllNewsQuizList();
		
		return ResponseEntity.status(HttpStatus.OK).body(news_quiz_by_admin_list);
		
	}
	
	// 뉴스 문제 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-news-quiz")
	public ResponseEntity<Map<String, String>> deleteNewsQuiz(
			@RequestParam("news_quiz_no")
			@NotNull(message = MessageCode.NEWS_QUIZ_NO_NOT_NULL)
			Long news_quiz_no
	){
		
		admin_service.deleteNewsQuiz(news_quiz_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_NEWS_QUIZ_SUCCESS
		));
		
	}

	// 문학 관리 -----------------------------------------------------------------------------
	// 문학 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-literature")
	public ResponseEntity<Map<String, String>> addLiterature(
			@Valid
			@RequestBody LiteratureByAdmin literauture_by_admin
	){
		
		admin_service.addLiterature(literauture_by_admin);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_LITERATURE_SUCCESS
		));
		
	}
	
	// 전체 문학 불러오기(문학 번호 순)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/get-all-literature-list")
	public ResponseEntity<List<GetLiteratureByAdmin>> getAllLiteratureList(){
		
		List<GetLiteratureByAdmin> get_literature_by_admin_list = admin_service.getAllLiteratureList();
		
		return ResponseEntity.status(HttpStatus.OK).body(get_literature_by_admin_list);
		
	}
	
	// 문학 + 문학 문단 + 문학 문제 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-literature-and-literature-paragraph-and-literature-quiz-by-literature-no")
	public ResponseEntity<Map<String, String>> deleteLiteratureAndLiteratureParagraphAndLiteratureQuizByLiteratureNo(
			@RequestParam("literature_no")
			@NotNull(message = MessageCode.LITERATURE_NO_NOT_NULL)
			Long literature_no
	){
		
		admin_service.deleteLiteratureAndLiteratureParagraphAndLiteratureQuizByLiteratureNo(literature_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LITERATURE_AND_LITERATURE_PARAGRAPH_AND_LITERATURE_QUIZ_SUCCESS
		));
		
	}

	// 문학 문단 관리 ---------------------------------------------------------------------------
	// 문학 문단 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-literature-paragraph")
	public ResponseEntity<Map<String, String>> addLiteratureParagraph(
			@Valid
			@RequestBody LiteratureParagraphByAdmin literature_paragraph_by_admin
	){
		
		admin_service.addLiteratureParagraph(literature_paragraph_by_admin);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_LITERATURE_PARAGRAPH_SUCCESS
		));
		
	}
	
	// 전체 문학 문단 불러오기(문학 문단 번호순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-literature-paragraph-list")
	public ResponseEntity<List<GetLiteratureParagraphByAdmin>> getAllLiteratureParagraphList(){
		
		List<GetLiteratureParagraphByAdmin> get_literature_paragraph_by_admin_list = admin_service.getAllLiteratureParagraphList();
		
		return ResponseEntity.status(HttpStatus.OK).body(get_literature_paragraph_by_admin_list);
		
	}

	// 문학 문단 + 문학 문제 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-literature-paragraph-and-literature-by-literature-paragraph-no")
	public ResponseEntity<Map<String, String>> deleteLiteratureParagraphAndLiteratureByLiteratureParagraphNo(
			@RequestParam("literature_paragraph_no")
			@NotNull(message = MessageCode.LITERATURE_PARAGRAPH_NO_NOT_NULL)
			Long literature_paragraph_no,
			@RequestParam("literature_no")
			@NotNull(message = MessageCode.LITERATURE_NO_NOT_NULL)
			Long literature_no
	){
		
		admin_service.deleteLiteratureParagraphAndLiteratureByLiteratureParagraphNo(literature_paragraph_no, literature_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LITERATURE_PARAGRAPH_AND_LITERATURE_QUIZ_SUCCESS
		));
		
	}
	
	// 문학 문제 관리 ---------------------------------------------------------------------
	// 문학 문제 생성(문학 문단에 해당하는 문제가 없을 경우 생성)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/generate-creative-literature-quiz")
	public ResponseEntity<Map<String, String>> generatedCreativeLiteratureQuiz(){
		
		// 문학 문제 생성
		literature_service.generatedCreativeLiteratureQuizByGemini();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_CREATIVE_LITERATURE_QUIZ_SUCCESS));
				
	}
	
	// 전체 문학 문제 불러오기(문학 문제 번호 순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-literature-quiz-list")
	public ResponseEntity<List<GetLiteratureQuizByAdmin>> getAllLiteratureQuizList(){
		
		List<GetLiteratureQuizByAdmin> get_literature_quiz_by_admin = admin_service.getAllLiteratureQuizList();
		
		return ResponseEntity.status(HttpStatus.OK).body(get_literature_quiz_by_admin);
		
	}
	
	// 문학 문제 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-literature-quiz")
	public ResponseEntity<Map<String, String>> deleteLiteratureQuiz(
			@RequestParam("literature_quiz_no")
			@NotNull(message = MessageCode.LITERATURE_QUIZ_NO_NOT_NULL)
			Long literature_quiz_no
	){
		
		admin_service.deleteLiteratureQuiz(literature_quiz_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LITERATURE_QUIZ_SUCCESS
		));
		
	}
	
	// 뉴스 퀴즈 풀이 기록 관리 -----------------------------------------
	// 뉴스 퀴즈 풀이 기록 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-news-quiz-attempt")
	public ResponseEntity<Map<String, String>> addNewsQuizAttempt(
			@Valid
			@RequestBody AddNewsQuizAttempt add_news_quiz_attempt
	){
		
		// 뉴스 기사 문제 저장
		admin_service.saveMemberSolvedNewsQuiz(add_news_quiz_attempt);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_NEWS_QUIZ_ATTEMPT_SUCCESS
		));
		
	}
	
	// 뉴스 퀴즈 풀이 기록 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-news-quiz-attempt")
	public ResponseEntity<Map<String, String>> deleteNewsQuizAttempt(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email,
			@RequestParam("news_quiz_no")
			@NotNull(message = MessageCode.NEWS_QUIZ_NO_NOT_NULL)
			Long news_quiz_no
	){
		
		admin_service.deleteNewsQuizAttempt(email, news_quiz_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_NEWS_QUIZ_ATTEMPT_SUCCESS
		));
		
	}
	
	
	
	// 문학 퀴즈 풀이 기록 관리 ------------------------------------------------
    // 문학 퀴즈 풀이 기록 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-literature-quiz-attempt")
	public ResponseEntity<Map<String, String>> addLiteratureQuizAttempt(
			@Valid
			@RequestBody AddLiteratureQuizAttempt add_literature_quiz_attempt
	){
		
		// 추가
		admin_service.addLiteratureQuizAttempt(add_literature_quiz_attempt);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_LITERATURE_QUIZ_ATTEMPT_SUCCESS
		));
		
	}

	// 문학 퀴즈 풀이 기록 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-literature-quiz-attempt")
	public ResponseEntity<Map<String, String>> deleteLiteratureQuizAttempt(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email,
			@RequestParam("literature_quiz_no")
			@NotNull(message = MessageCode.LITERATURE_QUIZ_NO_NOT_NULL)
			Long literature_quiz_no			
	){
		
		admin_service.deleteLiteratureQuizAttempt(email, literature_quiz_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LITERATURE_QUIZ_ATTEMPT_SUCCESS
		));
		
	}
	
	// 출석 관리 ---------------------------------
	// 이메일에 해당하는 출석 불러오기(최신순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-attendance-list-by-email")
	public ResponseEntity<List<GetAttendance>> getAttendanceListByEmail(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		List<GetAttendance> get_attendance_list = admin_service.getAttendanceListByEmail(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(get_attendance_list);
		
	}
	
	// 출석 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-attendance")
	public ResponseEntity<Map<String, String>> addAttendance(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email,
			@RequestParam("date")
			@NotNull(message = "date")
			LocalDate date
			
	){
		
		admin_service.addAttendance(email, date);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_ATTENDANCE_SUCCESS
		));
		
	}
	
	// 출석 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-attendance")
	public ResponseEntity<Map<String, String>> deleteAttendance(
			@RequestParam("attendance_no")
			@NotNull(message = MessageCode.ATTENDANCE_NO_NOT_NULL)
			Long attendance_no
	){
		
		admin_service.deleteAttendance(attendance_no);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_ATTENDANCE_SUCCESS
		));
		
	}
	
	// 점수 관리 --------------------------------------
	// 전체 점수 불러오기(최신순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-point-list")
	public ResponseEntity<List<GetPoint>> getAllPointList(){
		
		List<GetPoint> get_point_list = admin_service.getAllPointList();
		
		return ResponseEntity.status(HttpStatus.OK).body(get_point_list);
		
	}
	
	// 점수 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-point")
	public ResponseEntity<Map<String, String>> addPoint(
			@Valid
			@RequestBody PointDto.AddPoint add_point
	){
		
		admin_service.addPoint(add_point);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_POINT_SUCCESS
		));
	
	}
	
	
	// 점수 수정
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/update-point")
	public ResponseEntity<Map<String, String>> updatePoint(
			@Valid
			@RequestBody PointDto.UpdatePoint update_point
	){
		
		admin_service.updatePoint(update_point);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.UPDATE_POINT_SUCCESS
		));
		
	}
	
	// 점수 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-point")
	public ResponseEntity<Map<String, String>> deletePoint(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		admin_service.deletePoint(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_POINT_SUCCESS
		));
		
	}
	
	// 회원 관련 정보 불러오기
	// 회원 정보 불러오기
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-member-info-object")
	public ResponseEntity<MemberObjectByAdmin> getMemberInfoObject(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		MemberObjectByAdmin member_object_by_admin = admin_service.getMemberInfoObject(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(member_object_by_admin);
		
	}
	
	// 회원 출석 불러오기
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-member-attendance-list")
	public ResponseEntity<List<GetAttendance>> getMemberAttendanceList(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		List<GetAttendance> get_attendance_list = admin_service.getMemberAttendanceList(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(get_attendance_list);
		
	}
	
	// 회원 점수 불러오기
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-member-point-object")
	public ResponseEntity<GetPoint> getMemberPointObject(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		GetPoint get_point = admin_service.getMemberPointObject(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(get_point);
		
	}
	
	// 회원 뉴스 문제 기록 불러오기(최신순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-member-news-quiz-attempt-list")
	public ResponseEntity<List<GetNewsQuizAttemptByEmail>> getMemberNewsQuizAttemptList(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		List<GetNewsQuizAttemptByEmail> get_new_quiz_attempt_by_email_list = admin_service.getNewsQuizAttempListtByEmail(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(get_new_quiz_attempt_by_email_list);
		
	}
	
	// 회원 문학 문제 기록 불러오기(최신순)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-member-literature-quiz-attempt-list")
	public ResponseEntity<List<GetLiteratureQuizAttemptListByEmail>> getMemberLiteratureQuizAttemptList(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		List<GetLiteratureQuizAttemptListByEmail> get_literature_quiz_attempt_list_by_email = 
				admin_service.getLiteratureQuizAttemptListByEmail(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(get_literature_quiz_attempt_list_by_email);
		
	}
	
	// 점수 수정 관련 - 김기찬
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/increment-point")
	public ResponseEntity<Map<String, String>> incrementPoint(
	    @RequestBody @Valid PointDto.IncrementPoint dto
	) {
	    admin_service.incrementPoint(dto);
	    return ResponseEntity.ok(Map.of(
	        MessageCode.MESSAGE_CODE, MessageCode.UPDATE_POINT_SUCCESS
	    ));
	}
}
