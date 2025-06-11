package com.readforce.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.readforce.dto.OAuthAttributesDto;
import com.readforce.entity.Member;
import com.readforce.enums.Prefix;
import com.readforce.enums.Role;
import com.readforce.enums.Status;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private final MemberRepository member_repository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User o_auth2_user = delegate.loadUser(userRequest);
		
		// 현재 로그인 진행 중인 서비스를 구분(google, naver, kakao)
		String registration_id = userRequest.getClientRegistration().getRegistrationId();
		// OAuth2 로그인 진행 시 키가 되는 필드값
		String user_name_attribute_name = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		// 서비스에서 받아온 사용자 정보를 DTO로 변환
		OAuthAttributesDto o_auth_attributes_dto = OAuthAttributesDto.of(registration_id, user_name_attribute_name, o_auth2_user.getAttributes());
		
		// DB에서 사용자 조회
        Optional<Member> member_optional = member_repository.findByEmailAndStatus(o_auth_attributes_dto.getEmail(), Status.ACTIVE);
		
        Set<GrantedAuthority> authorities;

        if (member_optional.isPresent()) {
            // 기존 회원일 경우, 실제 권한 부여
            Member member = member_optional.get();
            authorities = Collections
                    .singleton(new SimpleGrantedAuthority(Prefix.ROLE.getName() + member.getRole().name()));
        } else {
            // 신규 회원일 경우, 임시 GUEST 권한 부여
            authorities = Collections.singleton(new SimpleGrantedAuthority(Prefix.ROLE.getName() + Role.GUEST.toString()));
        }
        
		// DB 저장 없이 속성과 임시 권한만 담아서 반환
		return new DefaultOAuth2User(
				authorities,
				o_auth_attributes_dto.getAttributes(),
				o_auth_attributes_dto.getNameAttributeKey()
		);
		
	}
	
}
