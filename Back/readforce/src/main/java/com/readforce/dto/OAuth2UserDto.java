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
	private final boolean is_new_user;
	private final String registration_id;
	
	public OAuth2UserDto(
			Collection<? extends GrantedAuthority> authorities, 
			Map<String, Object> attributes,
			String name_attribute_key,
			String email,
			boolean is_new_user,
			String registration_id
	) {
		super(authorities, attributes, name_attribute_key);
		this.email = email;
		this.is_new_user = is_new_user;
		this.registration_id = registration_id;
	}
	
}
