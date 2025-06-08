package com.readforce.dto;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.readforce.enums.MessageCode;

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
		
		@NotBlank(message = MessageCode.ID_NOT_BLANK)
		@Size(min = 6, max = 20, message = MessageCode.ID_SiZE_INVALID)
		@Pattern(regexp = "^(?=.*[a-z])[a-z\\d]{6,20}$", message = MessageCode.ID_PATTERN_INVALID)
		private String id;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 12, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
				regexp = "^(?=.*[a-zA-Z])(?=.*[\\d])(?=.*[!@#$%^&*()-_=+])[a-zA-Z\\d!@#$%^&*()-_=+]{12,}$"
				, message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String password;
		
		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
		@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
		private String nickname;
		
		@NotBlank(message = MessageCode.NAME_NOT_BLANK)
		@Size(min = 2, max = 20, message = MessageCode.NAME_SIZE_INVALID)
		@Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$", message = MessageCode.NAME_PATTERN_INVALID)
		private String name;
		
		@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
		private LocalDate birthday;
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@JsonIgnore
		@AssertTrue(message = MessageCode.BIRTHDAY_RANGE_INVALID)
		public boolean birthdayValid() {
			
			if(birthday == null) {
				return true;
			}
			
			final LocalDate min_date = LocalDate.of(1900, 1, 1); // 1900-1-1 부터
			final LocalDate max_date = LocalDate.now().minusYears(3); // 오늘날짜의 3년전 까지
			
			return !birthday.isBefore(min_date) && !birthday.isAfter(max_date);
			
		}
	}
	
	// 로그인
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SignIn{
		
		@NotBlank(message = MessageCode.ID_NOT_BLANK)
		@Size(min = 6, max = 20, message = MessageCode.ID_SiZE_INVALID)
		@Pattern(regexp = "^(?=.*[a-z])[a-z\\d]{6,20}$", message = MessageCode.ID_PATTERN_INVALID)
		private String id;
		
		@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
		@Size(min = 12, message = MessageCode.PASSWORD_SIZE_INVALID)
		@Pattern(
				regexp = "^(?=.*[a-zA-Z])(?=.*[\\d])(?=.*[!@#$%^&*()-_=+])[a-zA-Z\\d!@#$%^&*()-_=+]{12,}$"
				, message = MessageCode.PASSWORD_PATTERN_INVALID
		)
		private String password;
		
	}
	
	
	
}
