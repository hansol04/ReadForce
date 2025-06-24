package com.readforce.dto;

import java.time.LocalDateTime;

import com.readforce.enums.Level;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.validation.ValidEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
		
		private String title;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureQuiz{
		
		private Long literature_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private Long literature_no;

		private Long literature_paragraph_no;
		
		private String content;
		
		private String title;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetChallengeLiteratureQuiz{
		
		private Long literature_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private Long literature_no;

		private Long literature_paragraph_no;
		
		private String title;
		
		private String level;
		
		private String content;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SaveMemberSolvedLiteratureQuiz{
		
		@NotNull(message = MessageCode.LITERATURE_QUIZ_NO_NOT_NULL)
		private Long literature_quiz_no;
		
		@NotNull(message = MessageCode.SELECTED_OPTION_INDEX_NOT_NULL)
		private Integer selected_option_index;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LiteratureByAdmin{
		
		@NotBlank(message = MessageCode.LITERATURE_TITLE_NOT_BLANK)
		private String title;
		
		@NotBlank(message = MessageCode.LITERATURE_TYPE_NOT_BLANK)
		@ValidEnum(enumClass = LiteratureRelate.type.class, message = MessageCode.LITERATURE_TYPE_PATTERN_INVALID)
		private String type;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureByAdmin{

		private Long literature_no;

		private String title;

		private String type;

		private LocalDateTime created_date;	
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LiteratureParagraphByAdmin{
		
		@NotNull(message = MessageCode.LITERATURE_NO_NOT_NULL)
		private Long literature_no;
	
		@NotBlank(message = MessageCode.LITERATURE_PARAGRAPH_CATEGORY_NOT_BLANK)
		@ValidEnum(enumClass = LiteratureRelate.category.class, message = MessageCode.LITERATURE_PARAGRAPH_CATEGORY_INVALID)
		private String category;

		@NotBlank(message = MessageCode.LEVEL_NOT_BLANK)
		@ValidEnum(enumClass = Level.class, message = MessageCode.LEVEL_PATTERN_INVALID)
		private String level;

		@NotBlank(message = MessageCode.LITERATURE_PARAGRAPH_CONTENT_NOT_BLANK)
		private String content;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureParagraphByAdmin{
		
		private Long literature_paragraph_no;
		
		private Long literature_no;
		
		private String category;
		
		private String level;
		
		private String content;
		
		private LocalDateTime created_date;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureQuizByAdmin{

		private Long literature_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private LocalDateTime created_date;

		private Long literature_no;

		private Long literature_paragraph_no;	
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddLiteratureQuizAttempt{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@NotNull(message = MessageCode.LITERATURE_QUIZ_NO_NOT_NULL)
		private Long literature_quiz_no;

		@NotNull(message = MessageCode.SELECTED_OPTION_INDEX_NOT_NULL)
		private Integer selected_option_index;
		
	}
	
	

}
