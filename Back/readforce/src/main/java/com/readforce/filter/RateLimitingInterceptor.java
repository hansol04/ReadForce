package com.readforce.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.readforce.enums.MessageCode;
import com.readforce.exception.RateLimitExceededException;
import com.readforce.service.RateLimitingService;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor{

	private final RateLimitingService rate_limiting_service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		// email 기반 Rate Limiting(로그인한 회원 경우)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
			
			String email = authentication.getName();
			Bucket email_bucket = rate_limiting_service.resolveBucketForMail(email);
			ConsumptionProbe email_probe = email_bucket.tryConsumeAndReturnRemaining(1);
			
			if(!email_probe.isConsumed()) {
				
				throw new RateLimitExceededException(MessageCode.EMAIL_REQUEST_LIMIT_EXCEEDED);
				
			}
			
		} else {
			
			// IP 기반 Rate Limiting
			String ip_address = getClientIp(request);
			Bucket ip_bucket = rate_limiting_service.resolveBucketForIp(ip_address);
			ConsumptionProbe ip_probe = ip_bucket.tryConsumeAndReturnRemaining(1);
			
			if(!ip_probe.isConsumed()) {
				
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
