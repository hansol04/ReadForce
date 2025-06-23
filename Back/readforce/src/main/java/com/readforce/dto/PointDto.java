package com.readforce.dto;

import com.readforce.enums.MessageCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PointDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PointRanking{

		private Long total = 0L;

		private Long korean_news = 0L;

		private Long english_news = 0L;

		private Long japanese_news = 0L;

		private Long novel = 0L;

		private Long fairytale = 0L;
		
		private String nickname;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddPoint{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdatePoint{

		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;	
		
		@Min(value = 0, message = MessageCode.TOTAL_POINT_MUST_BE_POSITIVE_NUMBER)
		private Long total = 0L;

		@Min(value = 0, message = MessageCode.KOREAN_NEWS_POINT_MUST_BE_POSITIVE_NUMBER)
		private Long korean_news = 0L;

		@Min(value = 0, message = MessageCode.ENGILISH_NEWS_POINT_MUST_BE_POSITIVE_NUMBER)
		private Long english_news = 0L;

		@Min(value = 0, message = MessageCode.JAPANESE_NEWS_POINT_MUST_BE_POSITIVE_NUMBER)
		private Long japanese_news = 0L;

		@Min(value = 0, message = MessageCode.NOVEL_POINT_MUST_BE_POSITIVE_NUMBER)
		private Long novel = 0L;

		@Min(value = 0, message = MessageCode.FAIRYTALE_POINT_MUST_BE_POSITIVE_NUMBER)
		private Long fairytale = 0L;
		
	}
	
	
}
