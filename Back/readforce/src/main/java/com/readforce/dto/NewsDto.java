package com.readforce.dto;

import java.time.LocalDateTime;

import com.readforce.enums.MessageCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NewsDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetNews{
		
		private Long news_no;

		private String language;

		private String category;

		private String level;

		private String title;

		private String content;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetNewsQuiz{

		private Long news_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private Long news_no;

	}	
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetChallengeNewsQuiz{

		private Long news_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private Long news_no;
		
		private String title;
		
		private String level;
		
		private String content;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SaveMemberSolvedNewsQuiz{
		
		@NotNull(message = MessageCode.NEWS_QUIZ_NO_NOT_NULL)
		private Long news_quiz_no;

		@NotNull(message = MessageCode.SELECTED_OPTION_INDEX_NOT_NULL)
		private Integer selected_option_index;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProficiencyTestItem{
		
		private GetNews get_news;
		
		private GetNewsQuiz get_news_quiz;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberSolvedNewsQuiz{

		private Long news_quiz_no;
		
		private Boolean is_correct;

		private Integer selected_option_index;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NewsByAdmin{

		private Long news_no;

		private String language;

		private String category;

		private String level;

		private String title;

		private String content;

		private LocalDateTime created_date;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NewsQuizByAdmin{

		private Long news_quiz_no;

		private String question_text;

		private String choice1;

		private String choice2;

		private String choice3;

		private String choice4;

		private Integer correct_answer_index;

		private String explanation;

		private Double score;

		private LocalDateTime created_date;

		private Long news_no;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddNewsQuizAttempt{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@NotNull(message = MessageCode.NEWS_QUIZ_NO_NOT_NULL)
		private Long news_quiz_no;
		
		@NotNull(message = MessageCode.SELECTED_OPTION_INDEX_NOT_NULL)
		private Integer selected_option_index;

	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetNewsQuizAttemptByEmail{
		
		private String email;
		
		private Long news_quiz_no;

		private Boolean is_correct;

		private Integer selected_option_index;

		private LocalDateTime created_date;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetLiteratureQuizAttemptListByEmail{
		
		private String email;
		
		private Long literature_quiz_no;

		private Boolean is_correct;

		private Integer selected_option_index;

		private LocalDateTime created_date;
		
	}
	
	public record NewsResult(String title, String content) {}
	
}
