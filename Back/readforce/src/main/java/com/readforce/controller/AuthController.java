package com.readforce.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.enums.MessageCode;
import com.readforce.enums.Name;
import com.readforce.exception.AuthenticationException;
import com.readforce.service.AuthService;
import com.readforce.util.JwtUtil;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService auth_service;
	private final JwtUtil jwt_util;
	
	// 엑세스 토큰 재발급
    @PostMapping("/reissue-refresh-token")
    public ResponseEntity<?> reissueRefreshToken(
    		@RequestParam("refresh_token")
    		@NotBlank(message = MessageCode.REFRESH_TOKEN_NOT_BLANK)
    		String refresh_token
    ){
    	
    	String username = jwt_util.extractUsername(refresh_token);
    	String stored_refresh_token = auth_service.getRefreshToken(username);
    	
    	// 저장된 리프레쉬 토큰이 없는 경우(이미 사용되었거나 탈취 가능성 고려)
    	if(stored_refresh_token == null) {
    		
    		// 보안 위협으로 간주하고 해당 유저의 모든 리프레쉬 토큰을 삭제하여 강제 로그아웃 처리
    		auth_service.deleteRefreshToken(username);
    		log.warn("보안 경고 : 유효하지 않은 리프레쉬 토큰 사용 시도. 사용자 {}", username);
    		throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
    		
    	}
    	
    	
    	// 저장된 리프레쉬 토큰과 일치하는지, 만료되지 않았는지 확인
    	if(!stored_refresh_token.equals(refresh_token) || jwt_util.expiredToken(refresh_token)) {
    		
    		throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
    		
    	}
    	
    	// 새로운 토큰 생성
    	final UserDetails user_details = auth_service.loadUserByUsername(username);
    	final String new_access_token = jwt_util.generateAcessToken(user_details);
    	final String new_refresh_token = jwt_util.generateRefreshToken(user_details);
    	
    	// 새로운 리프레쉬 토큰 저장(회전 전략)
    	auth_service.storeRefreshToken(username, new_refresh_token);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(
    			Name.ACCESS_TOKEN.toString(), new_access_token,
    			Name.REFRESH_TOKEN.toString(), new_refresh_token,
    			MessageCode.MESSAGE_CODE, MessageCode.TOKEN_SUCCESS
    	));

    }
	
	
	
	
}
