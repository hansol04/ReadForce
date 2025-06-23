package com.readforce.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.readforce.dto.OAuth2UserDto;
import com.readforce.dto.OAuthAttributesDto;
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Prefix;
import com.readforce.enums.Role;
import com.readforce.enums.Status;
import com.readforce.exception.DuplicateException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private final MemberService member_service;
	private final MemberRepository member_repository;
	private final StringRedisTemplate redis_template;
	
	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
		
		// 표준 OAuth2 사용자 정보 로딩
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User o_auth2_user = delegate.loadUser(userRequest);
		
		// 현재 로그인 진행 중인 서비스를 구분(google, naver, kakao)
		String registration_id = userRequest.getClientRegistration().getRegistrationId();
		
		// OAuth2 로그인 진행 시 키가 되는 필드값
		String user_name_attribute_name = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		// 서비스에서 받아온 사용자 정보를 DTO로 변환
		OAuthAttributesDto o_auth_attributes_dto = OAuthAttributesDto.of(registration_id, user_name_attribute_name, o_auth2_user.getAttributes());
		String provider = registration_id;
		String provider_id = o_auth_attributes_dto.getProvider_id();
		String social_email = o_auth_attributes_dto.getEmail();
		
		// 소셜 계정 연동 시나리오(사용자가 이미 로그인 상태인지 확인)
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		
		
		
		if(attributes == null) {
			
			throw new OAuth2AuthenticationException("Request attriutes is null, cannot check state.");
			
		}
		
		HttpServletRequest request = attributes.getRequest();
		
		String state = request.getParameter("state");
		
		if(state != null) {
			
			log.warn(state);
			
			String email = redis_template.opsForValue().get(Prefix.SOCIAL_LINK_STATE.getName() + state);
			
			if(email != null) {
				
				
				
				// Redis 삭제
				redis_template.delete(Prefix.SOCIAL_LINK_STATE.getName() + state);
				
				try {
					
					member_service.linkSocialAccount(email, provider, provider_id, social_email);
					
				} catch(DuplicateException exception){
					
					throw new OAuth2AuthenticationException(exception.getMessage());
					
				}
				
				// 연동된 사용자의 정보로 계속 진행
				
				Member linked_member = member_repository.findByEmailAndStatus(email, Status.ACTIVE)
						.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL)); 
				
				return createOAuth2UserDto(linked_member, o_auth_attributes_dto, false, registration_id);

				
			}
			
		}
		
		// 일반 소셜 로그인 시나리오(사용자가 로그인 하지 않은 상태)
		
		// 소설 제공자 정보로 회원 조회
		Optional<Member> member_optional = member_repository.findBySocialProviderAndSocialProviderId(provider, provider_id);
		
		Member member;
		boolean is_new_user;
		
		if(member_optional.isPresent()) {
			
			// 소셜 제공자 정보로 회원을 찾음(기존 회원)
			member = member_optional.get();
			is_new_user = false;
			
		} else {	
			
			// 소셜 제공자 정보로 찾지 못했다면 이메일로 회원 조회(첫 소셜 로그인시 자동 연동)
			Optional<Member> member_optional_email = member_repository.findByEmailAndStatus(social_email, Status.ACTIVE);			
			
			if(member_optional_email.isPresent()) {
				
				// 이 이메일을 가진 사용자가 존재함으로 자동으로 연동 진행
				member = member_optional_email.get();
				member.setSocial_provider(provider);
				member.setSocial_provider_id(provider_id);
				member_repository.save(member);
				is_new_user = false;
			
			} else {
				
				// 소셜 제공자 정보와 이메일로 모두 회원을 찾을 수 없음(신규 회원)
				member = null;
				is_new_user = true;
								
			}

		}
				
		return createOAuth2UserDto(member, o_auth_attributes_dto, is_new_user, registration_id);
	}
	
	private OAuth2UserDto createOAuth2UserDto(Member member, OAuthAttributesDto o_auth_attributes_dto, boolean is_new_user, String registration_id) {
		
		String primary_email;
		Role role;
		
		if(is_new_user) {
			
			// 신규 사용자는 소셜 이메일을 기본 이메일로 사용
			primary_email = o_auth_attributes_dto.getEmail();
			role = Role.USER;
			
		} else {
			
			// 기존 사용자는 DB의 기본 이메일 사용
			primary_email = member.getEmail();
			role = member.getRole();
			
		}
		
		GrantedAuthority granted_authority = new SimpleGrantedAuthority(Prefix.ROLE.getName() + role.name());
		
		return new OAuth2UserDto(
					Collections.singleton(granted_authority),
					o_auth_attributes_dto.getAttributes(),
					o_auth_attributes_dto.getNameAttributeKey(),
					primary_email,
					is_new_user,
					registration_id
		);
		
		
	}
	
}


	
	
	
