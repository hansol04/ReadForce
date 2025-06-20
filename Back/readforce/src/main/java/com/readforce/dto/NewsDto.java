package com.readforce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NewsDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetNews{
		
		private Long new_no;

		private String language;

		private String category;

		private String level;

		private String title;

		private String content;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetNewsQuiz{

		private Long news_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private Long news_no;

	}	
	
	public record NewsResult(String title, String content) {}
	
}
