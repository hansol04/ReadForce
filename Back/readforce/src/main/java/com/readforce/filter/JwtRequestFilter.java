package com.readforce.filter;



import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.readforce.enums.MessageCode;
import com.readforce.enums.Prefix;
import com.readforce.service.AuthService;
import com.readforce.util.JwtUtil;

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
	protected void doFilterInternal(HttpServletRequest http_servlet_request, HttpServletResponse http_servlet_response, FilterChain filter_chain)
			throws ServletException, IOException {

		final String authorization_header = http_servlet_request.getHeader("Authorization");
		
		String username = null;
		String access_token = null;
		
		if(authorization_header != null && authorization_header.startsWith(Prefix.BEARER.getName())) {
			
			access_token = authorization_header.substring(Prefix.BEARER.getName().length());
			username = jwt_util.extractUsername(access_token);
		
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user_details = this.auth_service.loadUserByUsername(username);

            if(jwt_util.expiredToken(access_token)) {
            	
            	throw new JwtException(MessageCode.ACCESS_TOKEN_EXPIRED);
            	
            }
            
            if(jwt_util.validateToken(access_token, user_details)) {
                
            	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                		user_details, null, user_details.getAuthorities());
            	
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(http_servlet_request));
               
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            
            } else {
            	
            	throw new JwtException(MessageCode.AUTHENTICATION_FAIL);
            	
            }
        }
		filter_chain.doFilter(http_servlet_request, http_servlet_response);
	}
	
}
