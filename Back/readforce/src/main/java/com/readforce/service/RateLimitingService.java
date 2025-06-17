package com.readforce.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

	@Value("${rate-limiting.ip.requests}")
	private int ip_requests;
	
	@Value("${rate-limiting.ip.per-minute}")
	private int ip_per_minute;
	
	@Value("${rate-limiting.email.requests}")
	private int email_requests;
	
	@Value("${rate-limiting.email.per-minute}")
	private int email_per_minute;
	
	private CacheManager cache_manager;
	
	public Bucket resolveBucketForIp(String ip_address) {
		
		Bandwidth limit = Bandwidth.simple(ip_requests, Duration.ofMinutes(ip_per_minute));
		return cache_manager.getCache("rate-limit-buckets")
				.get(ip_address, () -> Bucket.builder().addLimit(limit).build());
		
	}
	
	public Bucket resolveBucketForMail(String email) {
		
		Bandwidth limit = Bandwidth.simple(email_requests, Duration.ofMinutes(email_per_minute));
		return cache_manager.getCache("rate-limit-buckets")
				.get(email, () -> Bucket.builder().addLimit(limit).build());
		
	}	
	
}
