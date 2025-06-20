package com.readforce.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.readforce.dto.NewsDto;
import com.readforce.dto.NewsDto.NewsResult;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate.Category;
import com.readforce.enums.NewsRelate.Language;
import com.readforce.enums.NewsRelate.Level;
import com.readforce.exception.GeminiException;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class GeminiService {
	
	private final RestTemplate rest_template;
	
	@Value("${gemini.api.url}")
	private String gemini_api_url;
	
	@Value("${gemini.api.key}")
	private String gemini_api_key;
	
	
	// 창작 뉴스 생성
	public NewsResult generateCreativeNews(Language language, Level level, Category category) {

		String news_prompt = buildNewsPrompt(language, level, category);
		
		String raw_text = callGeminiApi(news_prompt);
		
		return splitTitleAndBody(raw_text);
	}

	// 뉴스 프롬프트 생성
	private String buildNewsPrompt(Language language, Level level, Category category) {

		return 
				switch(language) {
					
					case KOREAN -> """
							다음 주제에 맞는 창작 뉴스를 작성해주세요:
							- 주제: %s
							- 난이도: %s
							- 최소 500자 이상
							- 한국어 뉴스 기사 스타일
							- 허구적인 사건과 등장인물 사용
							- 제목을 첫 문장으로 작성하고, 본문은 그 다음 줄로부터 이어지도록 작성하세요.
							기사 전체를 자연스럽게 출력해주세요.
					""".formatted(category, level);
					
					case JAPANESE -> """
							以下の条件に従って、創作ニュース記事を作成してください:
				            - トピック: %s
				            - 難易度: %s
				            - 最低500文字以上
				            - 日本語のニュース記事スタイル
				            - 架空の出来事・人物を使用
				            - 記事の最初の一文をタイトルにしてください。
				            - 本文はその次の文から続けてください。
					""".formatted(category, level);
					
					default -> """
							 Write an original fictional news article with the following:
					        - Topic: %s
					        - Level: %s
					        - At least 500 characters
					        - Written in realistic journalistic English
					        - Start with a one-line title as the first sentence.
					        - Continue with the full article body from the second sentence.
					""".formatted(category, level);
		
		};

	}
	
	// Gemini API 호출
	private String callGeminiApi(String news_prompt) {
		
		String url = UriComponentsBuilder
				.fromUriString(gemini_api_url)
				.queryParam("key", gemini_api_key)
				.toUriString();
		
		JSONObject body = new JSONObject();
		JSONArray parts = new JSONArray().put(new JSONObject().put("text", news_prompt));
		body.put("contents", new JSONArray().put(new JSONObject().put("parts", parts)));
		
		HttpHeaders http_headers = new HttpHeaders();
		http_headers.setContentType(MediaType.APPLICATION_JSON);
		
		try {
			
			// 요청
			ResponseEntity<String> response = rest_template.exchange(
					url, HttpMethod.POST, new HttpEntity<>(body.toString(), http_headers), String.class
			);
			
			if(response.getStatusCode() == HttpStatus.OK) {
				
				JSONObject object = new JSONObject(response.getBody());
				
				JSONArray candidates = object.optJSONArray("candidates");
				
				JSONObject first_candidate = candidates.optJSONObject(0);
				
				if(first_candidate != null) {
					
					JSONObject content = first_candidate.optJSONObject("content");
					
					if(content != null) {
						
						JSONArray parts_array = content.optJSONArray("parts");
						
						if(parts_array != null && !parts_array.isEmpty()) {
							
							JSONObject first_part = parts_array.optJSONObject(0);
							
							if(first_part != null) {
								
								return first_part.optString("text", "");
								
							}
							
						}
						
					}	
					
				}
				
			}
			
			throw new GeminiException(MessageCode.GEMINI_REQUEST_FAIL);
			
		} catch(RestClientException exception) {
			
			throw new GeminiException(MessageCode.GEMINI_REQUEST_FAIL);
			
		} catch(JSONException exception) {
			
			throw new GeminiException(MessageCode.GEMINI_RESPONSE_PATTERN_INVALID);
			
		} catch(Exception exception) {
			
			throw new GeminiException(MessageCode.GEMINI_REQUEST_FAIL);
			
		}
		
	}
	
	// 타이틀과 바디 분리
	private NewsDto.NewsResult splitTitleAndBody(String raw_text){
		
		String clean = sanitizeMarkdown(raw_text);
		
		String[] lines = clean.split("\\n", 2);
		
		String title = lines.length > 0 ? lines[0].trim() : "제목 없음";
		
		String body = lines.length > 1 ? lines[0].trim() : "";
		
		return new NewsDto.NewsResult(title, body);
		
	}
	
	// 특수 문자 제거
	private String sanitizeMarkdown(String text) {
		
		return text == null ? null : text
				.replaceAll("(?m)^\\s*([#*>\\-`]+)\\s*", "")
				.replaceAll("\\*\\*(.*?)\\*\\*", "$1")
				.replaceAll("\\*(.*?)\\*", "$1")
				.replaceAll("_([^_]+)_", "$1")
				.trim();
		
	}
	
	
	
	
	
	
}
