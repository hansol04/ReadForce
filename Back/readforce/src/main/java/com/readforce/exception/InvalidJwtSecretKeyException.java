package com.readforce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidJwtSecretKeyException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	public InvalidJwtSecretKeyException(String message) {
		super(message);
	}
	
}
