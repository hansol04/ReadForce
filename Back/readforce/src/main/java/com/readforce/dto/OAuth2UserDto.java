package com.readforce.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Getter;

@Getter
public class OAuth2UserDto extends DefaultOAuth2User {

	private static final long serialVersionUID = 1L;
	private final String email;
	private final boolean isNewUser;
	
	
	public OAuth2UserDto(
			Collection<? extends GrantedAuthority> authorities, 
			Map<String, Object> attributes,
			String nameAttributeKey,
			String email,
			boolean isNewUser
	) {
		super(authorities, attributes, nameAttributeKey);
		this.email = email;
		this.isNewUser = isNewUser;
	}
	
}
