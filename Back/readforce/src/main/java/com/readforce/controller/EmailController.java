package com.readforce.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.EmailDto;
import com.readforce.enums.MessageCode;
import com.readforce.service.EmailService;
import com.readforce.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
	
	private final EmailService email_service;
	private final MemberService member_service;
	
	// 회원가입 이메일 인증 번호 전송
	@PostMapping("/send-verification-code-sign-up")
	public ResponseEntity<Map<String, String>> sendVerificationCodeSignUp(@Valid @RequestBody EmailDto.VerificationRequest verification_request){
		
		// 이메일 중복 확인
		member_service.emailCheck(verification_request.getEmail());
		// 인증 번호 전송
		email_service.sendVerificationCodeSignUp(verification_request.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_SEND_SUCCESS));
		
	}
	
	// 회원가입 이메일 인증 번호 확인
	@PostMapping("/verify-verification-code-sign-up")
	public ResponseEntity<Map<String, String>> verifyVerificationCodeSignUp(@Valid @RequestBody EmailDto.VerificationConfirm verification_confirm) {
		
		// 인증 번호 확인
		email_service.verifyVerificationCodeSignUp(verification_confirm.getEmail(), verification_confirm.getCode());
		// 인증 성공 상태 저장
		email_service.markEmailAsVerified(verification_confirm.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_VERIFY_SUCCESS));
		
	}
	
	// 비밀번호 재설정 링크 전송
	@PostMapping("/send-password-reset-link")
	public ResponseEntity<Map<String, String>> sendPasswordResetLink(@Valid @RequestBody EmailDto.VerificationRequest verification_request){
		
		// 링크 전송
		email_service.sendPasswordResetLink(verification_request.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SEND_PASSWORD_RESET_LINK_SUCCESS));
		
	}
	
	
		
}
