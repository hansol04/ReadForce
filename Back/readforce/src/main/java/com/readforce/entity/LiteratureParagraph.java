package com.readforce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.id.LiteratureParagraphId;

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
public class LiteratureParagraph {
	
	@EmbeddedId
	private LiteratureParagraphId literature_paragraph_id;
	
	@MapsId("literature_no")
	@ManyToOne
	@JoinColumn(name = "literature_no")
	private Literature literature;
	
	@Column(nullable = false)
	private String category;
	
	@Column(nullable = false)
	private String level;
	
	@Column(nullable = false, columnDefinition = "text")
	private String content;
	
	@CreatedDate
	@Column(name = "created_date")
	private LocalDateTime created_date;
	
}
