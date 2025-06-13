package com.readforce.dto;


import com.readforce.enums.MessageCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class EmailDto {
	
	@Getter
	@Setter
	public static class VerificationRequest{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
	}
	
	@Getter
	@Setter
	public static class VerificationConfirm{
		
		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
		private String email;
		
		@NotBlank(message = MessageCode.CODE_NOT_BLANK)
		private String code;
		
	}
	
	
}
