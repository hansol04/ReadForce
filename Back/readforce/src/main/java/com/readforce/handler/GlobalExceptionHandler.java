package com.readforce.handler;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.readforce.enums.MessageCode;
import com.readforce.exception.PasswordNotMatchException;
import com.readforce.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// 유효성 검사 에러
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception){
		
		log.error("MethodArgumentNotValidException occured : {}", exception.getMessage(), exception);
		Map<String, String> error_map = new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error->{
			String message_code = error.getDefaultMessage();
			error_map.put(MessageCode.MESSAGE_CODE, message_code);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error_map);
		
	}
	
	// 데이터 없음 에러
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException exception){
		
		log.error("ResourceNotFoundException occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
	
	}
	
	// 아이디, 비밀번호 불일치
	@ExceptionHandler(PasswordNotMatchException.class)
	public ResponseEntity<Map<String, String>> handlePasswordNotMatchException(PasswordNotMatchException exception){
		
		log.error("PasswordNotMatchException occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	
	// 서버 에러
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Map<String, String>> handlerException(Exception exception){
	
		log.error("Exception occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.INTERNER_SERVER_ERROR));
		
	}
}
