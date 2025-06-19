package com.readforce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsQuiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_quiz_no")
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
	@Column(name = "created_date")
	private LocalDateTime created_date;
	
	@Column(name = "news_no", nullable = false)
	private Long news_no;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "news_no", insertable = false, updatable = false)
	private News news;
	
	
}
