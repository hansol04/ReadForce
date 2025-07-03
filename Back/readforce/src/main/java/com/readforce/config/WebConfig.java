package com.readforce.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.readforce.filter.RateLimitingInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@ConditionalOnProperty(name = "rate-limiting.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final RateLimitingInterceptor rate_limiting_interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(rate_limiting_interceptor)
				.addPathPatterns("/**") // 모든 경로에 인터셉터 적용
				.excludePathPatterns("/css/**", "/images/**", "/js/**", "/error"); // 특정 경로 제외
		
	}
	
}
