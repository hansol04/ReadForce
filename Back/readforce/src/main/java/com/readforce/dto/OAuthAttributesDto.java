package com.readforce.dto;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.readforce.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributesDto {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String email;
	
	@Builder
	public OAuthAttributesDto(Map<String, Object> attributes, String nameAttributeKey, String email) {
		
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.email = email;
		
	}
	
	// 각 소셜 서비스에 맞는 of 메소드 구현
	public static OAuthAttributesDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		
		// 네이버
		if("naver".equals(registrationId)) {
			return ofNaver("id", attributes);
		}
		// 카카오
		if("kakao".equals(registrationId)) {
			return ofKakao(userNameAttributeName, attributes);
		}
		// 구글
		return ofGoogle(userNameAttributeName, attributes);
		
	}
	
	// 구글
	private static OAuthAttributesDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		
		return OAuthAttributesDto.builder()
				.email((String) attributes.get("email"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.build();
		
	}
	
	// 네이버
	private static OAuthAttributesDto ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuthAttributesDto.builder()
				.email((String) response.get("email"))
				.attributes(response)
				.nameAttributeKey(userNameAttributeName)
				.build();
		
	}
	
	// 카카오
	private static OAuthAttributesDto ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		return OAuthAttributesDto.builder()
				.email((String) kakaoAccount.get("email"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.build();
		
	}
	
	public Member toEntity(String nickname, LocalDate birthday) {
		
		Member member = new Member();
		member.setNickname(nickname);
		member.setBirthday(birthday);
		member.setEmail(this.email);
		member.setPassword(UUID.randomUUID().toString());
		return member;
		
	}
	
	
}
