package com.readforce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeminiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public GeminiException(String message) {
		super(message);
	}
	
}
