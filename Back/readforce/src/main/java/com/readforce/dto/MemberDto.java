package com.readforce.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.readforce.enums.MessageCode;
import com.readforce.enums.Role;
import com.readforce.enums.Status;
import com.readforce.validation.ValidBirthday;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class MemberDto {
	
	// 회원 가입
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SignUp{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
		        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$"
		        , message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String password;
		
		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
		@Size(min = 2, max = 12, message = MessageCode.NICKNAME_SIZE_INVALID)
		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
		private String nickname;
		
		@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
		@ValidBirthday
		private LocalDate birthday;
		
	}
	
	// 로그인
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SignIn{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
		        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$"
		        , message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String password;
		
	}
	
	// 회원 정보 수정
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Modify{
		
		@Size(min = 2, max = 12, message = MessageCode.NICKNAME_SIZE_INVALID)
		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
		private String nickname;
		
		@ValidBirthday
		private LocalDate birthday;
		
	}

	// 비밀번호 재설정(링크)
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PasswordResetByLink{
		
		@NotBlank(message = MessageCode.TOKEN_NOT_BLANK)
		private String temporal_token;
		
		@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
		@ValidBirthday
		private LocalDate birthday;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
		        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$"
		        , message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String new_password;
		
	}
	
	// 비밀번호 재설정(회원 정보 수정)
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PasswordResetBySite{
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
		        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$"
		        , message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String old_password;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
		        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$"
		        , message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String new_password;
		
	}
	
	// 소셜 회원가입
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SocialSignUp {
		
		@NotBlank(message = MessageCode.TOKEN_NOT_BLANK)
		private String temporal_token;
		
		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
		@Size(min = 2, max = 12, message = MessageCode.NICKNAME_SIZE_INVALID)
		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
		private String nickname;
		
		@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
		@ValidBirthday
		private LocalDate birthday;
		
	}
	
	// 회원 정보 불러오기
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetMemberObject{
		
		private String email;
		
		private String nickname;
		
		private String provider;
		
		private LocalDate birthday;
		
	}
	
	// 사용자가 푼 문제 가져오기
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberAttemptedQuiz{
		
		private String question_text;
		
		private LocalDateTime created_date;
		
		private String classification;
		
	}

	// 사용자가 푼 문제 중 틀린문제 가져오기
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberIncorrectQuiz{
		
		private String question_text;
		
		private String classification;
		
		private Long quiz_no;
		
		private LocalDateTime created_date;
		
	}
	
	// 관리자 - 회원 정보 불러오기
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberObjectByAdmin{
		
		private String email;

		private String nickname;

		private LocalDate birthday;

		private String profile_image_url;

		private LocalDateTime create_date;

		private LocalDateTime last_modified_date;

		private LocalDateTime withdraw_date;

		private Status status;

		private Role role;

		private String social_provider;

		private String social_provider_id;

	}
	
	// 관리자 - 회원 정보 수정
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyByAdmin{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;

		@Size(min = 2, max = 12, message = MessageCode.NICKNAME_SIZE_INVALID)
		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
		private String nickname;

		@ValidBirthday
		private LocalDate birthday;

		private Status status;
		
		private Role role;
		
	}
	
	// 관리자 - 회원 가입
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SignUpByAdmin{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
		        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$"
		        , message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String password;
		
		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
		@Size(min = 2, max = 12, message = MessageCode.NICKNAME_SIZE_INVALID)
		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
		private String nickname;
		
		@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
		@ValidBirthday
		private LocalDate birthday;
		
		private Role role;
		
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdatePoint{
		
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
	public static class GetAttendance{
		
		private Long attendance_no;

		private String email;

		private LocalDateTime created_date;
		
	}
	
	
}
