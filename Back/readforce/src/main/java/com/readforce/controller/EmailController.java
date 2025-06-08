package com.readforce.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.EmailDto;
import com.readforce.enums.MessageCode;
import com.readforce.service.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
	
	private final EmailService email_service;
	
	// 이메일 인증 번호 전송
	@PostMapping("/send-verification-code")
	public ResponseEntity<Map<String, String>> sendVerificationCode(@Valid @RequestBody EmailDto.VerificationRequest verification_request) {
		
		email_service.sendVerificationCode(verification_request.getEmail());
		
		return ResponseEntity.ok().body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_SEND_SUCCESS));
		
	}
	
	// 이메일 인증 번호 확인
	@PostMapping("/verify-verification-code")
	public ResponseEntity<Map<String, String>> verifyVerificationCode(@Valid @RequestBody EmailDto.VerificationConfirm verification_confirm, BindingResult binding_result) {
		
		boolean verify = email_service.verifyVerificationCode(verification_confirm.getEmail(), verification_confirm.getCode());
		if(verify) {
			return ResponseEntity.ok().body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_VERIFY_SUCCESS));
		}
		return ResponseEntity.badRequest().body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_VERIFY_FAIL));
		
	}
	
	
	
	
	
	
}
