package com.readforce.dto;

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
	
}
