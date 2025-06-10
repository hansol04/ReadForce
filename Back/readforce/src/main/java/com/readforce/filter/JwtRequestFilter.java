package com.readforce.filter;





import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.enums.MessageCode;
import com.readforce.service.AuthService;
import com.readforce.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{
	
	private final AuthService auth_service;
	private final JwtUtil jwt_util;
	
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
			} catch (ExpiredJwtException e) {
				log.error("JWT token has expired: {}", e.getMessage());
				sendErrorResponse(response, MessageCode.TOKEN_ERROR);
				return;
			} catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
				log.error("Invalid JWT token: {}", e.getMessage());
				sendErrorResponse(response, MessageCode.TOKEN_ERROR);
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
	
	// 에러 응답 생성
	private void sendErrorResponse(HttpServletResponse response, String messageCode) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		Map<String, String> body = new HashMap<>();
		body.put(MessageCode.MESSAGE_CODE, messageCode);
		
		ObjectMapper object_mapper = new ObjectMapper();
		String json_body = object_mapper.writeValueAsString(body);
		
		response.getWriter().write(json_body);
	}
	
}
