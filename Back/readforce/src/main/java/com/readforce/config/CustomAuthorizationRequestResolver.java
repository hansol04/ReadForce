package com.readforce.config;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final OAuth2AuthorizationRequestResolver default_resolver;
	
	public CustomAuthorizationRequestResolver(ClientRegistrationRepository client_registration_repository, String authorization_request_base_uri) {
		
		this.default_resolver = new DefaultOAuth2AuthorizationRequestResolver(client_registration_repository, authorization_request_base_uri);
		
	}
	
	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest http_servlet_request) {
		
		OAuth2AuthorizationRequest o_auth2_authorization_request = default_resolver.resolve(http_servlet_request);
		
		if(o_auth2_authorization_request != null) {
			
			o_auth2_authorization_request = customizeAuthorizationRequest(o_auth2_authorization_request, http_servlet_request);
			
		}
		
		return o_auth2_authorization_request;
	}
	
	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest http_servlet_request, String client_registration_id) {

		OAuth2AuthorizationRequest o_auth2_authorization_request = default_resolver.resolve(http_servlet_request, client_registration_id);

		if(o_auth2_authorization_request != null) {
			
			o_auth2_authorization_request = customizeAuthorizationRequest(o_auth2_authorization_request, http_servlet_request);
			
		}
		
		return o_auth2_authorization_request;
	}
	
	private OAuth2AuthorizationRequest customizeAuthorizationRequest(
				OAuth2AuthorizationRequest o_auth2_authorization_request,
				HttpServletRequest http_servlet_request
			) {
		
		// 프론트에서 보낸 state가 있는지 확인
		String state = http_servlet_request.getParameter("state");
		
		if(state != null) {
			
			// state 파라미터가 있다면, 기존 요청에 해당 state를 포함하여 재생성
			return OAuth2AuthorizationRequest
					.from(o_auth2_authorization_request)
					.state(state)
					.build();
			
		}
		
		return o_auth2_authorization_request;
		
	}

	

}
