package com.readforce.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.MemberDto;
import com.readforce.enums.MessageCode;
import com.readforce.service.AuthService;
import com.readforce.service.MemberService;
import com.readforce.util.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
	
	private final MemberService member_service;
	private final AuthService auth_service;
	private final AuthenticationManager authentication_manager;
	private final JwtUtil jwt_util;
	
	// 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@Valid @RequestBody MemberDto.SignIn sign_in){
    	
    	System.out.println(sign_in.getEmail());
    	System.out.println(sign_in.getPassword());
    	
    	
    	// 사용자 인증
    	authentication_manager.authenticate(
    			new UsernamePasswordAuthenticationToken(sign_in.getEmail(), sign_in.getPassword())
    	);
    	
    	// jwt 생성
    	final UserDetails user_details = auth_service.loadUserByUsername(sign_in.getEmail());
    	final String jwt = jwt_util.generateToken(user_details);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of("Token", jwt, MessageCode.MESSAGE_CODE, MessageCode.SIGN_IN_SUCCESS)); 
    	
    }
    
    // 이메일 중복 확인
    @GetMapping("/email-check")
    public ResponseEntity<Map<String, String>> emailCheck(
    		@RequestParam("email")
    		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
    		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
    		String email
    ){
    	
    	member_service.emailCheck(email);	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.EMAIL_CAN_USE));
    	
    }
    
    // 닉네임 중복 확인
    @GetMapping("/nickname-check")
    public ResponseEntity<Map<String, String>> nicknameCheck(
    		@RequestParam("nickname")
    		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
    		@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
    		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
    		String nickname
    ){
    	
    	member_service.nicknameCheck(nickname);
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.NICKNAME_CAN_USE));
    	
    }
    
    // 회원 가입
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody MemberDto.SignUp sign_up){
    	
    	member_service.signUp(sign_up);
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS));

    }
    
	// 회원 정보 수정
	@PatchMapping("/modify-info")
	public ResponseEntity<Map<String, String>> modifyInfo(
			@Valid 
			@RequestBody MemberDto.Modify modify, 
			@AuthenticationPrincipal UserDetails user_details
	){
		
		// 현재 로그인 중인 아이디 가져오기
		String current_member_email = user_details.getUsername();
		
		member_service.modifyInfo(current_member_email, modify);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_INFO_MODIFY_SUCCESS));
		
	}
	
	// 회원 탈퇴
	@DeleteMapping("/withdraw-member")
	public ResponseEntity<Map<String, String>> withdrawMember(@AuthenticationPrincipal UserDetails user_details){
		
		// 현재 로그인 중인 아이디 가져오기
		String current_member_id = user_details.getUsername();
		// 회원 탈퇴
		member_service.withdrawMember(current_member_id);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_WITHDRAW_SUCCESS));
		
	}
	
	// 비밀번호 재설정(링크)
	@PatchMapping("/password-reset-by-link")
	public ResponseEntity<Map<String, String>> passwordResetByLink(@Valid @RequestBody MemberDto.PasswordReset password_reset){
		
		member_service.passwordResetByLink(password_reset.getToken(), password_reset.getNew_password());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PASSWORD_RESET_SUCCESS));
		
	}

}
