package com.readforce.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.readforce.enums.MessageCode;
import com.readforce.exception.RateLimitExceededException;
import com.readforce.service.RateLimitingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor{

	private final RateLimitingService rate_limiting_service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
			
			String email = authentication.getName();
			if(!rate_limiting_service.isEmailRequestAllowed(email)) {
				
				throw new RateLimitExceededException(MessageCode.EMAIL_REQUEST_LIMIT_EXCEEDED);
				
			}

		} else {
			
			String ip_address = getClientIp(request);
			if(!rate_limiting_service.isIpRequestAllowed(ip_address)) {
				
				throw new RateLimitExceededException(MessageCode.IP_ADDRESS_REQUEST_LIMIT_EXCEEDED);
				
			}
			
		}
		
		return true;
		
	}
	
	private String getClientIp(HttpServletRequest request) {
		
		String xf_header = request.getHeader("X-Forwarded-For");
		if(xf_header == null || xf_header.isEmpty()) {
			return request.getRemoteAddr();
		}
		return xf_header.split(",")[0];
		
	}
	
	
	
}
