package com.readforce.controller;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.MemberDto;
import com.readforce.enums.MessageCode;
import com.readforce.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService member_service;
	private static final String ID_REGEX_PATTERN = "^(?=.*[a-z])[a-z\\d]{6,20}$";
    private static final Pattern ID_COMPILED_PATTERN = Pattern.compile(ID_REGEX_PATTERN);
	
	
	// 회원 탈퇴
	@PutMapping("/withdraw")
	public ResponseEntity<Map<String, String>> withdraw(@Valid @RequestBody MemberDto.SignIn sign_in){
		
		// 로그인
		member_service.idAndPasswordCheck(sign_in.getId(), sign_in.getPassword());
		
		// 회원 탈퇴
		member_service.withdraw(sign_in.getId());

		return ResponseEntity.ok().body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_WITHDRAW_SUCCESS));
		
	}
	
	// 회원 삭제
}
