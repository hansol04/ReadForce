package com.readforce.filter;


import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Qualifier; // 추가
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver; // 추가

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.enums.MessageCode;
import com.readforce.service.AuthService;
import com.readforce.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	private final AuthService auth_service;
	private final JwtUtil jwt_util;
	private final HandlerExceptionResolver handlerExceptionResolver;
	
	public JwtRequestFilter(
			AuthService auth_service,
			JwtUtil jwt_util,
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
	) {
		this.auth_service = auth_service;
		this.jwt_util = jwt_util;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authorization_header = request.getHeader("Authorization");
		
		String username = null;
		String jwt = null;
		
		if(authorization_header != null && authorization_header.startsWith("Bearer ")) {
			jwt = authorization_header.substring(7);
			try {
				username = jwt_util.extractUsername(jwt);
			} catch (Exception e) {
				handlerExceptionResolver.resolveException(request, response, null, e);
				return;
			}
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user_details = this.auth_service.loadUserByUsername(username);

            if (jwt_util.validateToken(jwt, user_details)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                		user_details, null, user_details.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
		filterChain.doFilter(request, response);
	}
	
}
