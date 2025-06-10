package com.readforce.config;
import com.readforce.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtRequestFilter jwt_request_filter;
	private final CustomAuthenticationEntryPoint custom_authentication_entry_point;
	
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
					            "/member/email-check",
					            "/member/nickname-check",
					            "/member/password-reset-by-link",
					            "/email/send-verification-code-sign-up",
					            "/email/verify-verification-code-sign-up",
					            "/email/send-password-reset-link"
						).permitAll()
						.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http_security.addFilterBefore(jwt_request_filter, UsernamePasswordAuthenticationFilter.class);
		return http_security.build();
		
	}
	
	
	
	
}
