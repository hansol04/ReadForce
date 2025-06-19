package com.readforce.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.readforce.entity.News;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NewsDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class GetNewsPassage{
		
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
	public class GetNewsQuiz{

		private Long news_quiz_no;
		
		@Column(name = "question_text", nullable = false, columnDefinition = "text")
		private String question_text;
		
		@Column(nullable = false, columnDefinition = "text")
		private String choice1;
		
		@Column(nullable = false, columnDefinition = "text")
		private String choice2;
		
		@Column(nullable = false, columnDefinition = "text")
		private String choice3;
		
		@Column(nullable = false, columnDefinition = "text")
		private String choice4;
		
		@Column(name = "correct_answer_index", nullable = false)
		private Integer correct_answer_index;
		
		@Column(nullable = false, columnDefinition = "text")
		private String explanation;
		
		@Column(nullable = false)
		private Double score;
		
		@CreatedDate
		private LocalDateTime created_date;
		
		@Column(nullable = false)
		private Long news_passage_no;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "news_passage_no", insertable = false, updatable = false)
		private News news_passage;

	}
	
	
}
