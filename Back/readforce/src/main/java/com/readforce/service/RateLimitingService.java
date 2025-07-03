package com.readforce.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.enums.Prefix;
import com.readforce.exception.RateLimitExceededException;

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
		
		// 00시까지 남은 시간 계산
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
		Duration duration_until_midnight = Duration.between(now, midnight);
		
		// 뉴스
		if(classification.equals(Classification.NEWS.toString()) && language != null) {
			
			String key = Prefix.CHALLENGE_LIMIT.getName() + email + ":" + classification + ":" + language;
			
			ValueOperations<String, String> operations = redis_template.opsForValue();
			
			// 영어
			if(language.equals(NewsRelate.Language.ENGLISH.toString())) {
				
				if(operations.get(key) != null) {
					
					throw new RateLimitExceededException(MessageCode.ENGLISH_NEWS_CHALLENGE_ALREADY_TAKEN_TODAY);
					
				}
				
			}
			
			// 한국어
			if(language.equals(NewsRelate.Language.KOREAN.toString())) {
				
				if(operations.get(key) != null) {
					
					throw new RateLimitExceededException(MessageCode.KOREAN_NEWS_CHALLENGE_ALREADY_TAKEN_TODAY);
					
				}
				
			}
			
			// 일본어
			if(language.equals(NewsRelate.Language.JAPANESE.toString())) {
				
				if(operations.get(key) != null) {
					
					throw new RateLimitExceededException(MessageCode.JAPANESE_NEWS_CHALLENGE_ALREADY_TAKEN_TODAY);
					
				}
				
			}
			
			// 키가 없다면 생성(만료 시각 다음날 00시)
			operations.set(key, "1", duration_until_midnight);
			
		}
		
		// 문학
		if(classification.equals(Classification.LITERATURE.toString()) && type != null) {
			
			String key = Prefix.CHALLENGE_LIMIT.getName() + email + ":" + classification + ":" + type;
			
			ValueOperations<String, String> operations = redis_template.opsForValue();
			
			// 소설
			if(type.equals(LiteratureRelate.type.NOVEL.toString())) {
				
				if(operations.get(key) != null) {
					
					throw new RateLimitExceededException(MessageCode.NOVEL_CHALLENGE_ALREADY_TAKEN_TODAY);
					
				}
				
				
			}
			
			// 동화
			if(type.equals(LiteratureRelate.type.FAIRYTALE.toString())) {
				
				if(operations.get(key) != null) {
					
					throw new RateLimitExceededException(MessageCode.FAIRYTALE_CHALLENGE_ALREADY_TAKEN_TODAY);
					
				}
				
			}
			
			// 키가 없다면 생성(만료 시각 다음날 00시)
			operations.set(key, "1", duration_until_midnight);
			
		}
		
	}

}
