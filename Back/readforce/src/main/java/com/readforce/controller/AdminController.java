package com.readforce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.MemberDto.GetMemberObject;
import com.readforce.enums.MessageCode;
import com.readforce.service.AttendanceService;
import com.readforce.service.MemberService;
import com.readforce.service.NewsService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
	
	private final MemberService member_service;
	private final AttendanceService attendance_service;
	private final NewsService news_service;

	// 전체 회원 목록 조회
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-member-list")
	public ResponseEntity<List<GetMemberObject>> getAllMemberList(){
		
		List<GetMemberObject> member_list = member_service.getAllMemberList();
		
		return ResponseEntity.status(HttpStatus.OK).body(member_list);
	}
	
	// 계정 비활성화
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/deactivate-member")
	public ResponseEntity<Map<String, String>> deactivateMember(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		member_service.withdrawMember(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_DEACTIVATE_SUCCESS));
		
	}
	
	// 계정 활성화
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/activate-member")
	public ResponseEntity<Map<String, String>> activateMember(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email		
	){
		
		member_service.activateMember(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_ACTIVATE_SUCCESS));
		
	}
	
	// 출석일 조회
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/get-attendance-count")
	public ResponseEntity<Long> getAttendanceCount(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email		
	){
		
		Long attandance_count = attendance_service.getAttendanceCount(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(attandance_count);
		
	}
	
	// 뉴스 생성(언어 * 난이도 * 카테고리 수만큼 생성)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/generate-creative-news")
	public ResponseEntity<Map<String, String>> generateCreativeNews(){
		
		// 뉴스 생성
		news_service.generateCreativeNewsByGemini();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_CREATIVE_NEWS_SUCCESS));
		
	}
	
	// 뉴스 문제 생성(뉴스에 해당하는 문제가 없을 경우 생성)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/generate-creative-news-quiz")
	public ResponseEntity<Map<String, String>> generateCreativeNewsQuiz(){
		
		// 뉴스 퀴즈 생성
		news_service.generateCreativeNewsQuizByGemini();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_CREATIVE_NEWS_QUIZ_SUCCESS));
		
	}
	
}
