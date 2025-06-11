package com.readforce.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
					            "/email/send-verification-code-sign-up",
					            "/email/verify-verification-code-sign-up",
					            "/email/send-password-reset-link",
					            "/oauth2/**"
						).permitAll()
						.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 소셜 로그인(OAuth2) 기능 활성화
			.oauth2Login(oauth2 -> oauth2
					// 소셜 서비스에서 사용자 정보를 가져온 후 처리할 서비스 지정
					.userInfoEndpoint(userInfo -> userInfo.userService(custom_o_auth2_user_service))
					// 인증 성공 후 로직을 처리할 핸들러 지정
					.successHandler(o_auth2_authentication_success_handler)
			);

    private final JwtRequestFilter jwt_request_filter;
    private final CustomAuthenticationEntryPoint custom_authentication_entry_point;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication_configuration) throws Exception {
        return authentication_configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http_security) throws Exception {
        http_security
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/member/sign-in",
                    "/member/sign-up",
                    "/member/email-check",
                    "/member/nickname-check",
                    "/member/password-reset-by-link",
                    "/email/send-verification-code-sign-up",
                    "/email/verify-verification-code-sign-up",
                    "/email/send-password-reset-link",
                    "/api/news"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(custom_authentication_entry_point))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http_security.addFilterBefore(jwt_request_filter, UsernamePasswordAuthenticationFilter.class);
        return http_security.build();
    }


    // ✅ 실제 CORS 정책 설정 (Security용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 필수: 프론트가 credentials: include 사용 시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
