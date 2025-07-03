package com.readforce.dto;

import java.time.LocalDateTime;

import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.validation.ValidEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

		private Double total = 0.0;

		private Double korean_news = 0.0;

		private Double english_news = 0.0;

		private Double japanese_news = 0.0;

		private Double novel = 0.0;

		private Double fairytale = 0.0;
		
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
	
	// 김기찬찬
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IncrementPoint {

	    @NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	    @Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	    private String email;

	    @NotBlank(message = "카테고리를 선택하세요.")
	    private String category;

	    @NotNull(message = "변경할 점수를 입력하세요.")
	    private Double delta;
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
		private Double total = 0.0;

		@Min(value = 0, message = MessageCode.KOREAN_NEWS_POINT_MUST_BE_POSITIVE_NUMBER)
		private Double korean_news = 0.0;

		@Min(value = 0, message = MessageCode.ENGILISH_NEWS_POINT_MUST_BE_POSITIVE_NUMBER)
		private Double english_news = 0.0;

		@Min(value = 0, message = MessageCode.JAPANESE_NEWS_POINT_MUST_BE_POSITIVE_NUMBER)
		private Double japanese_news = 0.0;

		@Min(value = 0, message = MessageCode.NOVEL_POINT_MUST_BE_POSITIVE_NUMBER)
		private Double novel = 0.0;

		@Min(value = 0, message = MessageCode.FAIRYTALE_POINT_MUST_BE_POSITIVE_NUMBER)
		private Double fairytale = 0.0;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetPoint{

		private String email;

		private Double total = 0.0;

		private Double korean_news = 0.0;

		private Double english_news = 0.0;

		private Double japanese_news = 0.0;

		private Double novel = 0.0;

		private Double fairytale = 0.0;

		private LocalDateTime created_date;

		private LocalDateTime last_modified_date;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SaveChallengePoint{
		
		@NotBlank(message = MessageCode.CLASSIFICATION_NOT_BLANK)
		@ValidEnum(enumClass = Classification.class, message = MessageCode.CLASSIFICATION_PATTERN_INVALID)
		private String classification;
		
		@ValidEnum(enumClass = NewsRelate.Language.class, message = MessageCode.NEWS_ARTICLE_LANGUAGE_NOT_BLANK)
		private String language;

		@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_NOT_BLANK)
		private String type;

		@NotNull(message = MessageCode.POINT_NOT_NULL)
		private Double point;
		
	}
	
	
}
