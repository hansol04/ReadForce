package com.readforce.handler;


import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.readforce.enums.MessageCode;
import com.readforce.exception.AuthenticationException;
import com.readforce.exception.ChallengeException;
import com.readforce.exception.DuplicateException;
import com.readforce.exception.FileException;
import com.readforce.exception.GeminiException;
import com.readforce.exception.InvalidJwtSecretKeyException;
import com.readforce.exception.JsonException;
import com.readforce.exception.ValueException;
import com.readforce.exception.NewsException;
import com.readforce.exception.NotMatchException;
import com.readforce.exception.RankingException;
import com.readforce.exception.RateLimitExceededException;
import com.readforce.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// JWT 관련 에러
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException exception) {
        
    	log.error("JwtException occurred : {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    
    }
    
    // 랭킹 관련 에러
    @ExceptionHandler(RankingException.class)
    public ResponseEntity<Map<String, String>> handleRankingException(RankingException exception){
    	
    	log.error("RankingException occurred : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // 도전 관련 에러
    @ExceptionHandler(ChallengeException.class)
    public ResponseEntity<Map<String, String>> handleChallengeException(ChallengeException exception){
    	
    	log.error("ChallengeException occurred : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // 값 입력 에러
    @ExceptionHandler(ValueException.class)
    public ResponseEntity<Map<String, String>> ValueException(ValueException exception){
    	
    	log.error("MissingValueException occured : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // Gemini 관련 에러
    @ExceptionHandler(GeminiException.class)
    public ResponseEntity<Map<String, String>> handleGeminiException(GeminiException exception){
    	
    	log.error("GeminiException occured : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // 뉴스 관련 에러
    @ExceptionHandler(NewsException.class)
    public ResponseEntity<Map<String, String>> handleNewsException(NewsException exception){
    	
    	log.error("NewsException occurred : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // 요청 횟수 제한 초과 에러
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleRateLimitExceededException(RateLimitExceededException exception){
    	
    	log.warn("RateLimitExceededException occurred : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // JSON 관련 에러
    @ExceptionHandler(JsonException.class)
    public ResponseEntity<Map<String, String>> JsonException(JsonException exception){
    	
    	log.error("JsonException occured : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    
    // 파일 관련 에러
    @ExceptionHandler(FileException.class)
    public ResponseEntity<Map<String, String>> handleFileException(FileException exception){
    	
    	log.error("FileException occured : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
    	
    }
    
    // JWT secret key 관련 에러
    @ExceptionHandler(InvalidJwtSecretKeyException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJwtSecretKeyException(InvalidJwtSecretKeyException exception){
    	
    	log.error("InvalidJwtSecretKeyException occured : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.CHECK_JWT_SECERET_KEY));
    }
	
    // 데이터 무결성 제약 조건 위배 에러
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException exception){
    	
    	log.error("DataIntegrityViolationException occured : {}", exception.getMessage(), exception);
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.DATA_INTEGRITY_VIOLATION));
    	
    }

	// 유효성 검사 에러
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception){
		
		log.error("MethodArgumentNotValidException occured : {}", exception.getMessage(), exception);
		String message_code = exception.getBindingResult()
										.getFieldErrors()
										.stream()
										.map(field_error -> field_error.getDefaultMessage())
										.collect(Collectors.joining("/"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, message_code));
		
	}
	
	// 인증 실패 에러
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException exception){
		
		log.error("AuthenticationException occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	// 값이 불일치 에러
	@ExceptionHandler(NotMatchException.class)
	public ResponseEntity<Map<String, String>> handleNotMatchException(NotMatchException exception){
		
		log.error("NotMatchException occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	
	// 메소드 파라미터 또는 경로 변수 유효성 검사 에러
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException exception){
		
		log.error("ConstraintViolationException occured : {}", exception.getMessage(), exception);
		String message_code = exception.getConstraintViolations()
										.stream()
										.map(constraint_violation -> constraint_violation.getMessage())
										.collect(Collectors.joining("/"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, message_code));
				
	}
	
	// 데이터 없음 에러
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException exception){
		
		log.error("ResourceNotFoundException occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
	
	}
	
	// 데이터 중복 에러
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateException(DuplicateException exception){
		
		log.error("DuplicateException occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}	
	
	// 서버 에러
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Map<String, String>> handlerException(Exception exception){
	
		log.error("Exception occured : {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
}
