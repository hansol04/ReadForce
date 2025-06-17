package com.readforce.service;

import java.time.Duration;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.enums.MessageCode;
import com.readforce.enums.Prefix;
import com.readforce.enums.Status;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService{

	public final MemberRepository member_repository;
	
	private final StringRedisTemplate redis_template;
	
	@Value("${spring.jwt.refresh-expiration-time}")
	private long refresh_expiration_time;
	
	// Spring Security가 사용할 UserDetails 반환
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return member_repository.findByEmailAndStatus(username, Status.ACTIVE)
				.map(member -> new User(
                        member.getEmail(),
                        member.getPassword(),
                        // Role enum을 GrantedAuthority 객체로 변환하여 리스트에 담습니다.
                        Collections.singletonList(new SimpleGrantedAuthority(Prefix.ROLE.getName() + member.getRole().name()))
                ))
				.orElseThrow(
						() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL)
				);
	
	}
	
	// 리프레쉬 토큰 저장
	public void storeRefreshToken(String email, String refresh_token) {
		
		redis_template.opsForValue().set(
				Prefix.REFRESH_TOKEN.getName() + email,
				refresh_token,
				Duration.ofMillis(refresh_expiration_time)
		);
		
	}
	
	// 리프레쉬 토큰 가져오기
	public String getRefreshToken(String email) {
		
		return redis_template.opsForValue().get(Prefix.REFRESH_TOKEN.getName() + email);
		
	}
	
	// 리프레쉬 토큰 삭제
	public void deleteRefreshToken(String email) {
		
		redis_template.delete(Prefix.REFRESH_TOKEN.getName() + email);
		
	}

}
