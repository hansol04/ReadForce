package com.readforce.exception;

public class InvalidJwtSecretKeyException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	public InvalidJwtSecretKeyException(String message) {
		super(message);
	}
	
}
