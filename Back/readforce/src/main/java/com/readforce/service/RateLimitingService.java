package com.readforce.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.readforce.enums.Classification;
import com.readforce.enums.NewsRelate;
import com.readforce.enums.Prefix;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

	@Value("${rate-limiting.ip.max-requests}")
	private int ip_max_requests;
	
	@Value("${rate-limiting.ip.per-minute}")
	private int ip_per_minute;
	
	@Value("${rate-limiting.email.max-requests}")
	private int email_max_requests;
	
	@Value("${rate-limiting.email.per-minute}")
	private int email_per_minute;
	
	private final StringRedisTemplate redis_template;
	
	// IP 주소 기반
	public boolean isIpRequestAllowed(String ip_address) {
		
		String key = Prefix.RATE_LIMIT_IP + ip_address;
		
		return isRequestAllowed(key, ip_max_requests, Duration.ofMinutes(ip_per_minute));
		
	}
	
	// email 주소 기반
	public boolean isEmailRequestAllowed(String email) {
		
		String key = Prefix.RATE_LIMIT_EMAIL + email;
		
		return isRequestAllowed(key, email_max_requests, Duration.ofMinutes(email_per_minute));
		
	}
	
	public boolean isRequestAllowed(String key, long max_requests, Duration duration) {
		
		ValueOperations<String, String> operations = redis_template.opsForValue();
		
		Long current_requests = operations.increment(key);
		
		if(current_requests == null) {

			return false;

		}
		
		// 해당 키로 첫 요청이 오면 만료 시각 설정
		if(current_requests == 1) {
			
			redis_template.expire(key, duration);
			
		}
		
		return current_requests <= max_requests;		
		
	}
	
	// 일일 도전과제 응시 횟수 제한
	public void checkDailyChallengeLimit(String email, String classification, String type, String language) {
		
		// 뉴스
		if(classification.equals(Classification.NEWS.toString())) {
			
			String key = Prefix.CHALLENGE_LIMIT.getName() + email + ":" + classification + ":" + language;
			
			ValueOperations<String, String> operations = redis_template.opsForValue();
			
			// 영어
			if(language.equals(NewsRelate.Language.ENGLISH)) {
				
			}
			
		}
		
	}
	
	
}
