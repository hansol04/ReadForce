package com.readforce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class NewsPassage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long new_passage_no;
	
	@Column(nullable = false)
	private String country;
	
	@Column(nullable = false)
	private String level;
	
	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "text")
	private String content;
	
	@CreatedDate
	private LocalDateTime created_date;
	
}
