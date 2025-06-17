package com.readforce.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.validation.ValidBirthday;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
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
		
		@NotBlank(message = MessageCode.BIRTHDAY_NOT_NULL)
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
		private LocalDate createDate;
	    private String status;    
	}
	
	// 기무찬 
	public static GetMemberObject from(Member member) {
		return new GetMemberObject(
				member.getEmail(),
			    member.getNickname(),
			    member.getSocial_provider(),
			    member.getBirthday(),
			    member.getCreate_date().toLocalDate(),
			    member.getStatus().toString()
			);
	}
	

	
	
}
