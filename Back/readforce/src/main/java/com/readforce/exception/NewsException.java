package com.readforce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NewsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public NewsException(String message) {
		super(message);
	}
	
}
