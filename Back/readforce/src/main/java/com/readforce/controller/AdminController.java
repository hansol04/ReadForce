package com.readforce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.MemberDto.GetMemberObject;
import com.readforce.enums.MessageCode;
import com.readforce.service.MemberService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
	
	private final MemberService member_service;

	// 전체 회원 목록 조회
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-member-list")
	public ResponseEntity<List<GetMemberObject>> getAllMemberList(){
		
		List<GetMemberObject> member_list = member_service.getAllMemberList();
		
		return ResponseEntity.status(HttpStatus.OK).body(member_list);
	}
	
	// 계정 비활성화
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/withd-member")
	public ResponseEntity<Map<String, String>> deactivateMember(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		return null;
		
	}
	
}
