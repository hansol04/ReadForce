package com.readforce.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.readforce.entity.Literature;
import com.readforce.id.LiteratureParagraphId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
		private LocalDateTime created_date;
		
	}
	
}
