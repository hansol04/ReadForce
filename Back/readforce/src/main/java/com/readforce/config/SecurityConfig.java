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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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
            .cors(); 

        http_security.addFilterBefore(jwt_request_filter, UsernamePasswordAuthenticationFilter.class);
        return http_security.build();
    }

}
