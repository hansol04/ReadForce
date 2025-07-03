package com.readforce.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.readforce.enums.MessageCode;
import com.readforce.exception.InvalidJwtSecretKeyException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	
	@Value("${spring.jwt.secret}")
	private String secret;
	
	@Value("${spring.jwt.access-expiration-time}")
	private long access_expiration_time;
	
	@Value("${spring.jwt.refresh-expiration-time}")
	private long refresh_expiration_time;
	
	// JWT secret key 검증
	@PostConstruct
	public void validateKeyLength() {
		log.info("JWT secret key를 검증중입니다...");
		// 키를 UTF-8 바이트 배열로 변환
		byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
		
		// HS256 알고리즘에 권장되는 최소 키 길이 (256비트 = 32바이트)
		final int minKeyLengthBytes = 32;

		if (keyBytes.length < minKeyLengthBytes) {
			log.error("FATAL: JWT secret key가 너무 짧습니다. key는 반드시 최소 {} bytes (256 bits) 길이어야 합니다.", minKeyLengthBytes);
			// 예외를 발생시켜 안전하지 않은 설정으로 애플리케이션이 시작되는 것을 방지
			throw new InvalidJwtSecretKeyException(MessageCode.CHECK_JWT_SECERET_KEY);
		}
		
		log.info("JWT secret key 검증에 완료했습니다.");
	}
	
	// 시크릿 키를 HMAC-SHA 알고리즘에 맞는 Key 객체로 변환
	private Key getSigningKey() {
		
		byte[] key_bytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(key_bytes);
		
	}
	
	// 아이디 추출
	public String extractUsername(String token) {
		
		return extractClaim(token, Claims::getSubject);
				
	}
	
	// 만료시간 추출
	public Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
		
	}
	
	// 특정 클레임 추출
	public <T> T extractClaim(String token, Function<Claims, T> claims_resolver) {
		
		final Claims claims = extractAllClaims(token);
		return claims_resolver.apply(claims);
		
	}
	
	// 모든 클레임 추출
	public Claims extractAllClaims(String token) {
		
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		
	}
	
	// Access Token 생성
	public String generateAcessToken(UserDetails user_details) {
		
		Map<String, Object> claims = new HashMap<>();
		List<String> roles = user_details.getAuthorities().stream()
				.map(GrantedAuthority :: getAuthority)
				.collect(Collectors.toList());
		claims.put("roles", roles);
		return createToken(claims, user_details.getUsername(), access_expiration_time);
		
	}
	
	// Refresh Token 생성
	public String generateRefreshToken(UserDetails user_details) {
		
		return createToken(new HashMap<>(), user_details.getUsername(), refresh_expiration_time);
		
	}
	
	// 최종 JWT 생성
	private String createToken(Map<String, Object> claims, String subject, long expiration_time) {
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration_time))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
		
	}
	
	// 토큰 만료 확인
	public Boolean expiredToken(String token) {
		
		return extractExpiration(token).before(new Date());
		
	}
	
	// 토큰 유효성 검사
	public Boolean validateToken(String access_token, UserDetails user_details) {
		
		final String username = extractUsername(access_token);
		return username.equals(user_details.getUsername());
		
	}
	
	
}
