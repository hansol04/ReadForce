package com.readforce.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.dto.MemberDto.GetMemberObject;
import com.readforce.dto.OAuth2UserDto;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Name;
import com.readforce.enums.Prefix;
import com.readforce.exception.JsonException;
import com.readforce.service.AttendanceService;
import com.readforce.service.AuthService;
import com.readforce.service.MemberService;
import com.readforce.util.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	private final JwtUtil jwt_util;
	private final AuthService auth_service;
	private final MemberService member_service;
	private final AttendanceService attendance_service;
	private final StringRedisTemplate redis_template;
	
	@Value("${custom.fronted.social-login-success.exist-member-url}")
	private String social_login_success_exist_member_url;
	
	@Value("${custom.fronted.social-login-success.new-member-url}")
	private String social_login_success_new_member_url;
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest http_servlet_request,
			HttpServletResponse http_servlet_response,
			Authentication authentication
	) throws IOException, ServletException{
		
		OAuth2UserDto o_auth2_user_dto = (OAuth2UserDto) authentication.getPrincipal();
		
		// 신규 소셜 회원인지 확인
		boolean is_new_user = o_auth2_user_dto.is_new_user();
		String email = o_auth2_user_dto.getEmail();
		
		String target_url;
		
        if (is_new_user) {
            // 신규 회원
            String temporal_token = UUID.randomUUID().toString();
            
            // 소셜 정보(이메일, 제공자, 제공자 ID)를 Map에 담기
            Map<String, String> social_info = Map.of(
            	"email", email,
            	"provider", o_auth2_user_dto.getRegistration_id(),
            	"provider_id", o_auth2_user_dto.getName()
            );
            
            try {
            	
            	// Map을 JSON 문자열로 변환하여 Redis에 저장
            	String social_info_json = new ObjectMapper().writeValueAsString(social_info);
            	redis_template.opsForValue().set(
            			Prefix.SOCIAL_SIGN_UP.getName() + temporal_token, 
            			social_info_json,
            			Duration.ofMinutes(10)
            	);
            	
            } catch(JsonProcessingException exception) {
            	
            	throw new JsonException(MessageCode.JSON_PROCESSING_FAIL);
            	
            }
       
            // 프론트 엔드 추가 정보 입력 페이지로 리다이렉트
            target_url = UriComponentsBuilder.fromUriString(social_login_success_new_member_url)
                    .queryParam(Name.TEMPORAL_TOKEN.toString(), temporal_token)
                    .build()
                    .toUriString();
        } else {
        	
            // 기존 회원
            final UserDetails user_details = auth_service.loadUserByUsername(email);
            final String access_token = jwt_util.generateAcessToken(user_details);
            final String refresh_token = jwt_util.generateRefreshToken(user_details);
        	
            // 회원 조회
        	GetMemberObject get_member_dto = member_service.getMemberObjectByEmail(email);
            
        	// 임시 기존 회원 인증 토큰 생성
        	String temporal_token = UUID.randomUUID().toString();
        	
        	// redis 엑세스 토큰과 리프레쉬 토큰 저장
        	Map<String, String> token_map = Map.of(
        		Name.ACCESS_TOKEN.toString(), access_token,
        		Name.REFRESH_TOKEN.toString(), refresh_token,
        		Name.NICK_NAME.toString(), get_member_dto.getNickname(),
        		Name.PROVIDER.toString(), get_member_dto.getProvider()
        	);
        	
        	// Map을 JSON 문자열로 변환 후 저장
        	redis_template.opsForValue().set(
        			Prefix.TEMPORAL_TOKEN.getName() + temporal_token, 
        			new ObjectMapper().writeValueAsString(token_map),
        			Duration.ofMinutes(3)
        	);
        	
        	// 리프레쉬 토큰 저장
            auth_service.storeRefreshToken(email, refresh_token);
            
            // 출석 체크
            attendance_service.recordAttendance(email);
            
            
   
            // 프론트 엔드 로그인 콜백
            target_url = UriComponentsBuilder.fromUriString(social_login_success_exist_member_url)
                    .queryParam(Name.TEMPORAL_TOKEN.toString(), temporal_token)
                    .build()
                    .toUriString();
        }

        http_servlet_response.sendRedirect(target_url);
		
	}
	
	
	
	
	
	
	
	
	
	
	
}

