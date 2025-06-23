package com.readforce.controller;

import java.util.List;
import java.util.Map;

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
import com.readforce.enums.MessageCode;
import com.readforce.service.AdminService;
import com.readforce.service.AttendanceService;
import com.readforce.service.LiteratureService;
import com.readforce.service.MemberService;
import com.readforce.service.NewsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
	
	private final MemberService member_service;
	private final AttendanceService attendance_service;
	private final NewsService news_service;
	private final LiteratureService literature_service;
	private final AdminService admin_service;

	// 회원 -------------------------------------------------------------------------
	// 전체 회원 목록 조회
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
	
	// 뉴스 관리 ---------------------------------------------------------------------
	// 뉴스 생성(언어 * 난이도 * 카테고리 수만큼 생성)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/generate-creative-news")
	public ResponseEntity<Map<String, String>> generateCreativeNews(){
		
		// 뉴스 생성
		news_service.generateCreativeNewsByGemini();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_CREATIVE_NEWS_SUCCESS));
		
	}

	// 전체 뉴스 불러오기
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
	
	// 전체 뉴스 문제 불러오기
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
	
	// 전체 문학 불러오기
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
	
	// 전체 문학 문단 불러오기
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
	
	// 전체 문학 문제 불러오기
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
	
	// 뉴스 퀴즈 풀이 기록 관리 ------------------------------------------
	// 뉴스 퀴즈 풀이 기록 추가

	
	// 뉴스 퀴즈 풀이 기록 삭제
	
	
	// 문학 퀴즈 풀이 기록 관리 ------------------------------------------------
	// 문학 퀴즈 풀이 기록 추가
	
	// 문학 퀴즈 풀이 기록 삭제
	
	// 출석 관리 ---------------------------------
	// 출석 추가
	
	// 출석 삭제
	
	// 점수 관리 --------------------------------------
	// 점수 추가
	
	// 점수 수정
	
	// 점수 삭제
	
	
}
