package com.readforce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.readforce.dto.GeminiDto.GeminiRequest;
import com.readforce.dto.GeminiDto.GeminiResponse;
import com.readforce.enums.MessageCode;
import com.readforce.exception.GeminiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeminiService {
	
	private final RestTemplate rest_template;
	
	@Value("${gemini.api.key}")
	private String gemini_api_key;
	
	@Value("${gemini.api.call-url}")
	private String gemini_api_call_url;

	// 프롬프트 제미나이 요청
	public String callGeminiApi(String prompt) {
		
		String url = UriComponentsBuilder
				.fromUriString(gemini_api_call_url)
				.queryParam("key", gemini_api_key)
				.toUriString();
		
		// 제미나이 본문 생성
		GeminiRequest request_body = GeminiRequest.fromPrompt(prompt);
		
		HttpHeaders http_headers = new HttpHeaders();
		http_headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GeminiRequest> entity = new HttpEntity<>(request_body, http_headers);
		
		// 요청
		ResponseEntity<GeminiResponse> response = rest_template.exchange(url, HttpMethod.POST, entity, GeminiResponse.class);
		
		if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			
			String rewritten_text = response.getBody().extractText();
			if(rewritten_text != null) {
				
				return rewritten_text;
			
			}
			
			// Gemini 응답 파싱 실패
			throw new GeminiException(MessageCode.GEMINI_RESPONSE_EMPTY_FAIL);
			
		} else {
			
			// Gemini 호출 실패
			throw new GeminiException(MessageCode.GEMINI_API_CALL_FAIL);
			
		}
			
	}
	
}
