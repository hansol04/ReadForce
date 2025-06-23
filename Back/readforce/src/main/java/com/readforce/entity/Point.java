package com.readforce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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
public class Point {
	
	@Id
	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email", insertable = false, updatable = false)
	private Member member;
	
	@Column(nullable = false)
	private Double total = 0.0;
	
	@Column(name = "korean_news", nullable = false)
	private Double korean_news = 0.0;
	
	@Column(name = "english_news", nullable = false)
	private Double english_news = 0.0;
	
	@Column(name = "japanese_news", nullable = false)
	private Double japanese_news = 0.0;
	
	@Column(nullable = false)
	private Double novel = 0.0;
	
	@Column(nullable = false)
	private Double fairytale = 0.0;
	
	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private LocalDateTime created_date;
	
	@LastModifiedDate
	@Column(name = "last_modified_date")
	private LocalDateTime last_modified_date;

}
