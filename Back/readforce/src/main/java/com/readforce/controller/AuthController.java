package com.readforce.controller;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Name;
import com.readforce.enums.Prefix;
import com.readforce.exception.AuthenticationException;
import com.readforce.exception.JsonException;
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
	private final StringRedisTemplate redis_template;
	
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
    		log.info("요청받은 리프레시 토큰: {}", refresh_token);
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
    			MessageCode.MESSAGE_CODE, MessageCode.REISSUE_ACCESS_TOKEN_SUCCESS
    	));

    }
	
	// 토큰 받아오기
    @PostMapping("/get-tokens")
    public ResponseEntity<Map<String, String>> getTokens(
    	@RequestParam("temporal_token")
    	@NotBlank(message = MessageCode.TEMPORAL_TOKEN_NOT_BLANK)
    	String temporal_token
    ){
    	
    	// 토큰 불러오기
    	String token_json = (String)redis_template.opsForValue().get(Prefix.TEMPORAL_TOKEN.getName() + temporal_token);
    	
    	if(token_json == null) {
    		throw new AuthenticationException(MessageCode.TEMPORAL_TOKEN_AUTHENTICATION_FAIL);
    	}
    	
    	// 임시 토큰 삭제
    	redis_template.delete(Prefix.TEMPORAL_TOKEN.getName() + temporal_token);
    	
    	Map<String, String> tokens;
		try {
			
			tokens = new ObjectMapper().readValue(token_json, new TypeReference<Map<String, String>>(){});
			tokens.put(MessageCode.MESSAGE_CODE, MessageCode.GET_TOKENS_SUCCESS);
			
		} catch (JsonMappingException e) {
			
			throw new JsonException(MessageCode.JSON_MAPPING_FAIL);
			
		} catch (JsonProcessingException e) {
			
			throw new JsonException(MessageCode.JSON_PROCESSING_FAIL);
			
		}
    	
    	return ResponseEntity.status(HttpStatus.OK).body(tokens);
    	
    }
    
    // 소셜 계정 연동 토큰 받아오기
    @PostMapping("/get-social-account-link-token")
    public ResponseEntity<Map<String, String>> getSocialAccountLinkToken(@AuthenticationPrincipal UserDetails user_details){
    	
    	// 이메일 조회
    	String email = user_details.getUsername();
    	
    	// 임시 토큰 발급
    	String state = UUID.randomUUID().toString();
    	
    	// Redis에 저장
    	redis_template.opsForValue().set(
    			Prefix.SOCIAL_LINK_STATE.getName() + state, 
    			email,
    			Duration.ofMinutes(5)
    	);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(
    			Name.STATE.toString(), state    			
    	));   	
    	
    }
	
	
}
