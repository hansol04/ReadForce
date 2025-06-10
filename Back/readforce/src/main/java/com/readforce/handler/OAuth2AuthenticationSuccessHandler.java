package com.readforce.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.readforce.dto.OAuthAttributesDto;
import com.readforce.enums.Prefix;
import com.readforce.enums.Role;
import com.readforce.service.AuthService;
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
	private final StringRedisTemplate redis_template;
	private final ClientRegistrationRepository client_registration_repository;
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest http_servlet_request,
			HttpServletResponse http_servlet_response,
			Authentication authentication
	) throws IOException, ServletException{
		
		OAuth2User o_auth2_user = (OAuth2User) authentication.getPrincipal();
		
		// 신규 소셜 회원인지 확인
		boolean isNewUser = o_auth2_user.getAuthorities()
								.stream()
								.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Prefix.ROLE.getName() + Role.GUEST.toString()));
		
		String registration_id = ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication)
				.getAuthorizedClientRegistrationId();
		
		// ClientRegistrationRepository를 사용하여 ClientRegistration 객체 조회
        ClientRegistration client_registration = client_registration_repository.findByRegistrationId(registration_id);
        String user_name_attribute_name = client_registration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributesDto o_auth_attributes_dto = OAuthAttributesDto.of(registration_id, user_name_attribute_name, o_auth2_user.getAttributes());
        String email = o_auth_attributes_dto.getEmail();

        String target_url;

        if (isNewUser) {
            // 신규 회원
            String temp_token = UUID.randomUUID().toString();
            redis_template.opsForValue().set(
                    Prefix.SOCIAL_SIGN_UP.getName() + temp_token,
                    email,
                    Duration.ofMinutes(10));
            // 프론트 엔드 추가 정보 입력 페이지로 리다이렉트
            target_url = UriComponentsBuilder.fromUriString("http://localhost:3000/social-sign-up")
                    .queryParam("token", temp_token)
                    .build()
                    .toUriString();
        } else {
            // 기존 회원
            final UserDetails user_details = auth_service.loadUserByUsername(email);
            final String jwt = jwt_util.generateToken(user_details);

            // 프론트 엔드 로그인 콜백
            target_url = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/callback")
                    .queryParam("token", jwt)
                    .build()
                    .toUriString();
        }

        http_servlet_response.sendRedirect(target_url);
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
