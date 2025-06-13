package com.readforce.dto;

import java.util.List;

import com.readforce.enums.MessageCode;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class QuizDto {
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RequestGenerateQuiz{
		
		@NotBlank(message = MessageCode.QUIZ_ARTICLE_NOT_BLANK)
		private String article;
		
		private String language = "한국어";
		
		private String difficulty = "중급";
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ResponseGenerateQuiz{
		
		private String question;
		
		private List<String> choices;
		
		private String answer;
		
		private String explanation;
		
	}
	

	
}
