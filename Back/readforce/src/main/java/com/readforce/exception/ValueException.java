package com.readforce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public ValueException(String message) {
		super(message);
	}

}
