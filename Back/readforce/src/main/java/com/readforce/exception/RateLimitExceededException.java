package com.readforce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	public RateLimitExceededException(String message) {
		super(message);
	}
	

}
