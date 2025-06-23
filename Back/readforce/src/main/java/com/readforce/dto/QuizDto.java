package com.readforce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class QuizDto {
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetQuiz{
		
		private String question_text;
		
		private int percentage;
		
		private Long news_quiz_no;
		
		private Long literature_quiz_no;		
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IncorrectQuiz{
		
		long quiz_no;
		
		String question_text;
		
		String classification;
		
		long incorrect_count;
		
		long total_count;		
		
	}

}
