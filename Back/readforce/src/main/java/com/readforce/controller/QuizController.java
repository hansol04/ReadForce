package com.readforce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.QuizDto.RequestGenerateQuiz;
import com.readforce.dto.QuizDto.ResponseGenerateQuiz;
import com.readforce.service.QuizService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {
	
	private final QuizService quiz_service;
	
	// 퀴즈 생성
	@PostMapping("/generate")
	public ResponseEntity<ResponseGenerateQuiz> generateQuiz(@Valid @RequestBody RequestGenerateQuiz request_generate_quiz){
		
		// 퀴즈 생성
		ResponseGenerateQuiz response_generate_quiz = quiz_service.generateQuiz(request_generate_quiz);
		
		return ResponseEntity.status(HttpStatus.OK).body(response_generate_quiz);
		
	}
	
	
}
