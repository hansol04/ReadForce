package com.readforce.dto;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.readforce.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributesDto {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String email;
	private String provider_id;
	
	@Builder
	public OAuthAttributesDto(Map<String, Object> attributes, String nameAttributeKey, String email, String provider_id) {
		
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.email = email;
		this.provider_id = provider_id;
		
	}
	
	// 각 소셜 서비스에 맞는 of 메소드 구현
	public static OAuthAttributesDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		
		// 네이버
		if("naver".equals(registrationId)) {
			return ofNaver(userNameAttributeName, attributes);
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
				.provider_id(attributes.get(userNameAttributeName).toString())
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.build();
		
	}
	
	// 네이버
	private static OAuthAttributesDto ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuthAttributesDto.builder()
				.email((String) response.get("email"))
				.provider_id(response.get("id").toString())
				.attributes(response)
				.nameAttributeKey("id")
				.build();
		
	}
	
	// 카카오
	private static OAuthAttributesDto ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		return OAuthAttributesDto.builder()
				.email((String) kakaoAccount.get("email"))
				.provider_id(attributes.get(userNameAttributeName).toString())
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
