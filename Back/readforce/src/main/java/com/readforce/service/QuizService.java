package com.readforce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.readforce.dto.QuizDto.RequestGenerateQuiz;
import com.readforce.dto.QuizDto.ResponseGenerateQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.exception.GeminiException;
import com.readforce.exception.QuizException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final GeminiService gemini_service;
	
	// 퀴즈 생성
	public ResponseGenerateQuiz generateQuiz(@Valid RequestGenerateQuiz request_generate_quiz) {

		// 프롬프트 작성
		String prompt = writeMultipleChoiceQuizPrompt(request_generate_quiz);
		
		// 제미나이 퀴즈 생성 요청
		String gemini_response = gemini_service.callGeminiApi(prompt);
		
		// 제미나이 응답 퀴즈 변환
		ResponseGenerateQuiz transformed_quiz = transformToMultipleChoiceQuiz(gemini_response);
		
		return transformed_quiz;
	
	}
	
	// 객관식 퀴즈 프롬프트 작성
	private String writeMultipleChoiceQuizPrompt(RequestGenerateQuiz request_generate_quiz) {
		
		String prompt;
		String language = request_generate_quiz.getLanguage();
		String article = request_generate_quiz.getArticle();
		String difficulty = request_generate_quiz.getDifficulty();
		
		switch(language.toLowerCase()) {
		
			case "english":
				prompt = """
						Based on the following article, create exactly one multiple-choice quiz question in English.
						The difficulty level should be: %s
						
			        	Format:
			        	Question: [Your question here]
			        	1. [Option A]
			        	2. [Option B]
			        	3. [Option C]
			        	4. [Option D]
			        	Answer: [A/B/C/D]
			        	Explanation: [Short explanation]
			
			        	Only one question. Do not generate more than one.
			
			        	Article:
			        	%s
				""".formatted(difficulty, article);
				break;
		
			case "japanese":
				prompt = """
						以下の記事に基づいて、日本語でクイズを1問だけ作成してください。
						難易度: %s

			        	形式：
			        	問題: [質問文]
			        	1. [選択肢A]
			        	2. [選択肢B]
			        	3. [選択肢C]
			        	4. [選択肢D]
			        	正解: [A/B/C/D]
			        	解説: [理由を説明]
			
			        	記事:
			        	%s						
				""".formatted(difficulty, article);
				break;
			
			default:
				prompt = """
						다음 기사를 바탕으로 퀴즈를 하나 만들어주세요. 형식은 아래와 같아야 합니다:
						난이도는 %s 수준에 맞춰야 합니다.

		                문제: [질문 내용]
		                1. [보기1]
		                2. [보기2]
		                3. [보기3]
		                4. [보기4]
		                정답: [A/B/C/D]
		                해설: [정답에 대한 설명]
		
		                기사:
		                %s
				""".formatted(difficulty, article);
		
		}
		
		return prompt;
		
	}
	
	
	
	// 키워드 조회
	private String getKeyword(String text, List<String> candidate_list) {
		
		for(String key : candidate_list) {
			
			if(text.contains(key)) {
				
				return key;
				
			}
			
		}
		
		return "";
		
	}
	
	// 제미나이 응답 객관식 퀴즈 변환
	private ResponseGenerateQuiz transformToMultipleChoiceQuiz(String gemini_response) {
		
		if(gemini_response == null || gemini_response.isBlank()) {
			
			throw new GeminiException(MessageCode.GEMINI_RESPONSE_NOT_FOUND);
			
		}
		
		String text = gemini_response.replace("\r\n", "\n").trim();
		
		String question_key = getKeyword(text, List.of("문제:", "Question", "問題:"));
		String answer_key = getKeyword(text, List.of("정답:", "Answer:", "正解:"));
		String explanation_key = getKeyword(text, List.of("해설:", "Explanation:", "解説:"));
		
		int question_start = text.indexOf(question_key);
		int answer_start = text.indexOf(answer_key);
		int explanation_start = text.indexOf(explanation_key);
		
		if(question_start == -1 || answer_start == -1 || explanation_start == -1) {
			
			throw new GeminiException(MessageCode.GEMINI_RESPONSE_PATTERN_INVALID);
			
		}
		
		String question_block = text.substring(question_start + question_key.length(), answer_start).trim();
		String answer_raw = text.substring(answer_start + answer_key.length(), explanation_start).trim();
		String answer_letter = answer_raw.replaceAll("[^A-Da-d1-4]", "").toUpperCase();
		
		if(answer_letter.matches("[1-4]")) {
			
			answer_letter = switch(answer_letter) {
			
				case "1" -> "A";
				case "2" -> "B";
				case "3" -> "C";
				case "4" -> "D";
				default -> answer_letter;
			
			};
			
		}
		
		String explanation = text.substring(explanation_start + explanation_key.length()).trim();
		
		List<String> choice_list = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\d+\\.\\s(.+)");
		Matcher matcher = pattern.matcher(question_block);
		
		while(matcher.find()) {
			
			choice_list.add(matcher.group(1).trim());
			
		}
		
		String question_line = question_block.split("\n")[0].trim();
		
		int answer_index = switch(answer_letter) {
		
			case "A" -> 0;
			case "B" -> 1;
			case "C" -> 2;
			case "D" -> 3;
			default -> throw new QuizException(MessageCode.QUIZ_ANSWER_PATTERN_INVALID);
		
		};
		
		if(answer_index >= choice_list.size()) {
		
			throw new QuizException(MessageCode.QUIZ_ANSWER_INDEX_SIZE_INVALID);
		
		}
		
		return new ResponseGenerateQuiz(
				question_line,
				choice_list,
				choice_list.get(answer_index),
				explanation
		);

	}
	
}
