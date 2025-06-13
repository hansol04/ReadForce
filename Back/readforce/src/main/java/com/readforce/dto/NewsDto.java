package com.readforce.dto;

import com.readforce.enums.MessageCode;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NewsDto {

	// 뉴스 재구성
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TransformArticle{
		
		@NotBlank(message = MessageCode.NEWS_ARTICLE_TITLE_NOT_BLANK)
		private String title;
		
		@NotBlank(message = MessageCode.NEWS_ARTICLE_DESCRIPTION_NOT_BLANK)
		private String description;
		
		@NotBlank(message = MessageCode.NEWS_ARTICLE_CONTENT_NOT_BLANK)
		private String content;
		
		@NotBlank(message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
		private String language = "한국어";
		
		
	}
	
}
