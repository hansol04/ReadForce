package com.readforce.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class GeminiDto {

	// 제미나이 요청
	public record GeminiRequest(List<Content> content_list) {
		
		public static GeminiRequest fromPrompt(String prompt) {
			
			return new GeminiRequest(List.of(new Content(List.of(new Part(prompt)))));
			
		}
		
	}
	
	// 제미나이 응답
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record GeminiResponse(List<Candidate> candidate_list) {
		
		public String extractText() {
			
			if(candidate_list != null && !candidate_list.isEmpty()) {
				
				Candidate first_candidate = candidate_list.get(0);
				if(first_candidate != null && first_candidate.content() != null) {
					
					Part first_part = first_candidate.content().part_list().get(0);
					if(first_part != null) {
						return first_part.text();
					}
					
				}
				
			}
			
			return null; // 텍스트를 못 찾은 경우
			
		}
		
	}
	
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	record Content(List<Part> part_list) {}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	record Part(String text) {}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	record Candidate(Content content) {}
	
}
