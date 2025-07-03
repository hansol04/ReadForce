package com.readforce.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.readforce.filter.JwtRequestFilter;
import com.readforce.handler.OAuth2AuthenticationSuccessHandler;
import com.readforce.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtRequestFilter jwt_request_filter;
	private final CustomAuthenticationEntryPoint custom_authentication_entry_point;
	private final CustomOAuth2UserService custom_o_auth2_user_service;
	private final OAuth2AuthenticationSuccessHandler o_auth2_authentication_success_handler;
	private final ClientRegistrationRepository client_registration_repository;
	
	@Bean
	public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
		
		return new CustomAuthorizationRequestResolver(
				this.client_registration_repository, "/oauth2/authorization"
		);
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication_configuration) throws Exception{
		
		return authentication_configuration.getAuthenticationManager();
		
	}
	
	@Bean SecurityFilterChain securityFilterChain(HttpSecurity http_security) throws Exception{
		
		http_security
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exception -> exception.authenticationEntryPoint(custom_authentication_entry_point))
			.authorizeHttpRequests(
					auth -> 
					auth
						.requestMatchers(
								"/member/sign-in",
					            "/member/sign-up",
					            "/member/social-sign-up",
					            "/member/email-check",
					            "/member/nickname-check",
					            "/member/password-reset-by-link",
					            "/member/reissue-refresh-token",
					            "/email/send-verification-code-sign-up",
					            "/email/verify-verification-code-sign-up",
					            "/email/send-password-reset-link",
					            "/auth/get-tokens",
					            "/auth/reissue-refresh-token",
								"/news/get-news-passage-list",
								"/news/get-news-quiz-object",
								"/ranking/get-news-ranking",
								"/ranking/get-literature-ranking",
					            "/oauth2/**"
						).permitAll()
						.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 소셜 로그인(OAuth2) 기능 활성화
			.oauth2Login(oauth2 -> oauth2
					// authorizationEndpoint를 커스터마이징
					.authorizationEndpoint(authz -> authz.authorizationRequestResolver(customAuthorizationRequestResolver()))					
					// 소셜 서비스에서 사용자 정보를 가져온 후 처리할 서비스 지정
					.userInfoEndpoint(userInfo -> userInfo.userService(custom_o_auth2_user_service))
					// 인증 성공 후 로직을 처리할 핸들러 지정
					.successHandler(o_auth2_authentication_success_handler)
			);

		http_security.addFilterBefore(jwt_request_filter, UsernamePasswordAuthenticationFilter.class);
		return http_security.build();
		
	}
	
	
	
	
}
