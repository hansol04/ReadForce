package com.readforce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.id.LiteratureQuizAttemptId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
public class LiteratureQuizAttempt {

	@EmbeddedId
	private LiteratureQuizAttemptId literature_quiz_attempt_id;
	
	@MapsId("email")
	@ManyToOne
	@JoinColumn(name = "email")
	private Member member;
	
	@MapsId("literature_quiz_no")
	@ManyToOne
	@JoinColumn(name = "literature_quiz_no")
	private LiteratureQuiz literature_quiz;
	
	@Column(name = "is_correct", nullable = false)
	private Boolean is_correct;
	
	@Column(name = "selected_option_index", nullable = false)
	private Integer selected_option_index;
	
	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private LocalDateTime created_date;
	
}
