package com.readforce.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.readforce.entity.LiteratureParagraph;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LiteratureDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureParagraph{
		
		private Long literature_paragraph_no;
		
		private Long literature_no;
		
		private String category;
		
		private String level;
		
		private String content;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureQuiz{
		
		private Long literature_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private Long literature_no;

		private Long literature_paragraph_no;	
		
	}
	
}
