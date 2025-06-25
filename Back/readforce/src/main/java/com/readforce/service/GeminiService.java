package com.readforce.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.readforce.dto.NewsDto;
import com.readforce.dto.NewsDto.NewsResult;
import com.readforce.entity.LiteratureParagraph;
import com.readforce.entity.LiteratureQuiz;
import com.readforce.entity.News;
import com.readforce.entity.NewsQuiz;
import com.readforce.enums.Level;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate.Category;
import com.readforce.enums.NewsRelate.Language;
import com.readforce.exception.GeminiException;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class GeminiService {
	
	private final RestTemplate rest_template;
	
	@Value("${gemini.api.url}")
	private String gemini_api_url;
	
	@Value("${gemini.api.key}")
	private String gemini_api_key;
	
	
	// 창작 뉴스 생성
	public NewsResult generateCreativeNews(Language language, Level level, Category category) {
		
		// 프롬프트 생성
		String news_prompt = buildNewsPrompt(language, level, category);
		
		// Gemini 요청
		String gemini_response = callGeminiApi(news_prompt);
		
		// response 변환 및 반환		
		return splitTitleAndBody(gemini_response);
	}

	// 뉴스 프롬프트 생성
	private String buildNewsPrompt(Language language, Level level, Category category) {

		return 
				switch(language) {
					
					case KOREAN -> """
							다음 주제에 맞는 창작 뉴스를 작성해주세요:
							- 주제: %s
							- 난이도: %s
							- 최소 500자 이상
							- 한국어 뉴스 기사 스타일
							- 허구적인 사건과 등장인물 사용
							- 제목을 첫 문장으로 작성하고, 본문은 그 다음 줄로부터 이어지도록 작성하세요.
							기사 전체를 자연스럽게 출력해주세요.
					""".formatted(category, level);
					
					case JAPANESE -> """
							以下の条件に従って、創作ニュース記事を作成してください:
				            - トピック: %s
				            - 難易度: %s
				            - 最低500文字以上
				            - 日本語のニュース記事スタイル
				            - 架空の出来事・人物を使用
				            - 記事の最初の一文をタイトルにしてください。
				            - 本文はその次の文から続けてください。
					""".formatted(category, level);
					
					default -> """
							 Write an original fictional news article with the following:
					        - Topic: %s
					        - Level: %s
					        - At least 500 characters
					        - Written in realistic journalistic English
					        - Start with a one-line title as the first sentence.
					        - Continue with the full article body from the second sentence.
					""".formatted(category, level);
		
		};

	}
	
	// Gemini API 호출
	private String callGeminiApi(String news_prompt) {
		
		String url = UriComponentsBuilder
				.fromUriString(gemini_api_url)
				.queryParam("key", gemini_api_key)
				.toUriString();
		
		JSONObject body = new JSONObject();
		JSONArray parts = new JSONArray().put(new JSONObject().put("text", news_prompt));
		body.put("contents", new JSONArray().put(new JSONObject().put("parts", parts)));
		
		HttpHeaders http_headers = new HttpHeaders();
		http_headers.setContentType(MediaType.APPLICATION_JSON);
		
		try {
			
			// 요청
			ResponseEntity<String> response = rest_template.exchange(
					url, HttpMethod.POST, new HttpEntity<>(body.toString(), http_headers), String.class
			);
			
			if(response.getStatusCode() == HttpStatus.OK) {
				
				JSONObject object = new JSONObject(response.getBody());
				
				JSONArray candidates = object.optJSONArray("candidates");
				
				JSONObject first_candidate = candidates.optJSONObject(0);
				
				if(first_candidate != null) {
					
					JSONObject content = first_candidate.optJSONObject("content");
					
					if(content != null) {
						
						JSONArray parts_array = content.optJSONArray("parts");
						
						if(parts_array != null && !parts_array.isEmpty()) {
							
							JSONObject first_part = parts_array.optJSONObject(0);
							
							if(first_part != null) {
								
								return first_part.optString("text", "");
								
							}
							
						}
						
					}	
					
				}
				
			}
			
			throw new GeminiException(MessageCode.GEMINI_REQUEST_FAIL);
			
		} catch(RestClientException exception) {
			
			throw new GeminiException(MessageCode.GEMINI_REQUEST_FAIL);
			
		} catch(JSONException exception) {
			
			throw new GeminiException(MessageCode.GEMINI_RESPONSE_PATTERN_INVALID);
			
		} catch(Exception exception) {
			
			throw new GeminiException(MessageCode.GEMINI_REQUEST_FAIL);
			
		}
		
	}
	
	// 타이틀과 바디 분리
	private NewsDto.NewsResult splitTitleAndBody(String gemini_response){
		
		String clean = sanitizeMarkdown(gemini_response);
		
		String[] lines = clean.split("\\n", 2);
		
		String title = lines.length > 0 ? lines[0].trim() : "제목 없음";
		
		String body = lines.length > 1 ? lines[1].trim() : "";
		
		return new NewsDto.NewsResult(title, body);
		
	}
	
	// 특수 문자 제거
	private String sanitizeMarkdown(String text) {
		
		return text == null ? null : text
				.replaceAll("(?m)^\\s*([#*>\\-`]+)\\s*", "")
				.replaceAll("\\*\\*(.*?)\\*\\*", "$1")
				.replaceAll("\\*(.*?)\\*", "$1")
				.replaceAll("_([^_]+)_", "$1")
				.trim();
		
	}

	
	// 뉴스 문제 생성
	public NewsQuiz generateCreativeNewsQuiz(
			@NotNull(message = MessageCode.NEWS_ARTICLE_NOT_NULL)
			News unquizzed_news
	) {
		
		// 프롬프트 생성
		String news_quiz_prompt = buildNewsQuizPrompt(
				unquizzed_news.getContent(), 
				unquizzed_news.getLanguage(), 
				unquizzed_news.getLevel()
		);
		
		// Gemini 요청
		String gemini_response = callGeminiApi(news_quiz_prompt);
		
		// response 변환 및 반환
		return parseNewsQuizResponse(gemini_response, unquizzed_news);

	}

	// Gemini response 변환(뉴스 퀴즈)
	private NewsQuiz parseNewsQuizResponse(String gemini_response, News unquizzed_news) {

		String text = gemini_response.replace("\r\n", "\n").trim();
		
		// 지문
		String question_line = extractQuestionLine(text)
				.map(line -> line.replaceAll("(?i)(문제|問題|question|Q)[\\s:：\\-]*", "").trim())
				.orElseThrow(() -> new GeminiException(MessageCode.NEWS_QUIZ_LINE_MISSING));
		
		List<String> choice_list = new ArrayList<>();
		
		// 선택지
		Matcher option_matcher = Pattern.compile("(?m)^([A-Da-d]|[①-④]|\\d+)[.\\)]\\s*(.+)$").matcher(text);
		while(option_matcher.find()) {
			
			choice_list.add(option_matcher.group(2).trim());
			
		}
		if(choice_list.size() < 4) {
			
			throw new GeminiException(MessageCode.NEWS_QUIZ_OPTION_MISSING);
			
		}
		
		// 정답
		Matcher answer_matcher = Pattern.compile("(?i)(정답|Answer|正解):\\s*\\[?([A-D])\\]?", Pattern.CASE_INSENSITIVE).matcher(text);
		if(!answer_matcher.find()) {
			
			throw new GeminiException(MessageCode.NEWS_QUIZ_ANSWER_MISSING);
			
		}
		int correct_answer_index = "ABCD".indexOf(answer_matcher.group(2).toUpperCase());
		
		// 해설
		Matcher explanation_matcher = Pattern.compile("(?i)(해설|Explanation|解説):\\s*(.+)", Pattern.CASE_INSENSITIVE).matcher(text);
		String explanation = explanation_matcher.find() ? explanation_matcher.group(2).trim() : "";
		
		// 점수
		double score = switch(unquizzed_news.getLevel()) {
		
			case "BEGINNER" -> 2.5;
			
			case "ADVANCED" -> 7.5;
			
			default -> 5.0;
		
		};
		
		// NewsQuiz 엔티티 생성
		NewsQuiz generated_news_quiz = new NewsQuiz();
		generated_news_quiz.setQuestion_text(question_line);
		generated_news_quiz.setChoice1(choice_list.get(0));
		generated_news_quiz.setChoice2(choice_list.get(1));
		generated_news_quiz.setChoice3(choice_list.get(2));
		generated_news_quiz.setChoice4(choice_list.get(3));
		generated_news_quiz.setCorrect_answer_index(correct_answer_index);
		generated_news_quiz.setExplanation(explanation);
		generated_news_quiz.setScore(score);
		generated_news_quiz.setNews_no(unquizzed_news.getNews_no());
		
		return generated_news_quiz;
	
	}

	// 문제 라인 추출
	private Optional<String> extractQuestionLine(String text) {

		return Arrays.stream(text.split("\n"))
				.map(String::trim)
				.filter(line -> line.matches("(?i)(문제|問題|question|Q)[\\s:：\\-]*.*") || line.endsWith("?"))
				.findFirst();

	}

	// 뉴스 문제 프롬프트 생성
	private String buildNewsQuizPrompt(String content, String language, String level) {
		
		return switch(language) {
		
			case "KOREAN" -> """
					아래 기사 내용을 기반으로 %s 난이도의 4지선다형 퀴즈를 1개 만들어주세요.
	                반드시 한국어로만 작성해주세요. 영어가 포함되면 무효입니다.
	                다음 형식을 엄격히 지켜주세요:
	
	                문제: <질문 내용>
	                A. 보기 A
	                B. 보기 B
	                C. 보기 C
	                D. 보기 D
	                정답: <A-D>
	                해설: <정답의 이유>
	
	                기사:
			""".formatted(level) + "\n" + content;
			
			case "JAPANESE" -> """
					以下の記事を基に、難易度「%s」の4択クイズを1問作成してください。
		            必ず日本語で回答し、英語を含めないでください。次の形式を厳守してください：
		
		            問題: <質問内容>
		            A. 選択肢A
		            B. 選択肢B
		            C. 選択肢C
		            D. 選択肢D
		            正解: <A-D>
		            解説: <正解の理由>
		
		            記事:		
			""".formatted(level) + "\n" + content;
			
			default -> """
					Based on the article below, generate exactly one %s multiple choice question (MCQ) with 4 distinct options. 
		            Please respond in English only. Use this format:
		
		            Question: <your question here>
		            A. Option A
		            B. Option B
		            C. Option C
		            D. Option D
		            Answer: [A-D]
		            Explanation: <brief explanation>
		
		            Article:
			""".formatted(level) + "\n" + content;
			
		};
	
	}

	// 문학 문제 생성
	public LiteratureQuiz generateCreativeLiteratureQuiz(LiteratureParagraph literature_paragraph) {
		log.warn("2");
		// 문학 문제 프롬프트 생성
		String literature_quiz_prompt = buildLiteratureQuizPrompt(literature_paragraph.getContent(), literature_paragraph.getLevel(), literature_paragraph.getLiterature().getType());
		log.warn("3");
		// Gemini API 요청
		String gemini_response = callGeminiApi(literature_quiz_prompt);
		log.warn("4");
		// 변환 후 반환
		return parseLiteratureQuizResponse(gemini_response, literature_paragraph);
		
	}

	// Gemini response 변환(문학 퀴즈)
	private LiteratureQuiz parseLiteratureQuizResponse(String gemini_response, LiteratureParagraph literature_paragraph) {
	
		String text = gemini_response.replace("\r\n", "\n").trim();
		
		// 지문
		String question_line = extractQuestionLine(text)
				.map(line -> line.replaceAll("(?i)(문제|問題|question|Q)[\\s:：\\-]*", "").trim())
				.orElseThrow(() -> new GeminiException(MessageCode.LITERATURE_QUIZ_LINE_MISSING));
		
		List<String> choice_list = new ArrayList<>();
		
		// 선택지
		Matcher option_matcher = Pattern.compile("(?m)^([A-Da-d]|[①-④]|\\d+)[.\\)]\\s*(.+)$").matcher(text);
		while(option_matcher.find()) {
			
			choice_list.add(option_matcher.group(2).trim());
			
		}
		if(choice_list.size() < 4) {
			
			throw new GeminiException(MessageCode.LITERATURE_QUIZ_OPTION_MISSING);
			
		}
		
		// 정답
		Matcher answer_matcher = Pattern.compile("(?i)(정답|Answer|正解)\\s*[:：]\\s*\\W*([A-Da-d1-4])").matcher(text);
		if(!answer_matcher.find()) {
			
			throw new GeminiException(MessageCode.LITERATURE_QUIZ_ANSWER_MISSING);
			
		}
		int correct_answer_index = "ABCD".indexOf(answer_matcher.group(2).toUpperCase());
		
		// 해설
		Matcher explanation_matcher = Pattern.compile("(?i)(해설|Explanation|解説):\\s*(.+)", Pattern.CASE_INSENSITIVE).matcher(text);
		String explanation = explanation_matcher.find() ? explanation_matcher.group(2).trim() : "";
		
		// 점수
		double score = switch(literature_paragraph.getLevel()) {
		
			case "BEGINNER" -> 2.5;
			
			case "ADVANCED" -> 7.5;
			
			default -> 5.0;
		
		};
		
		LiteratureQuiz literature_quiz = new LiteratureQuiz();
		literature_quiz.setQuestion_text(question_line);
		literature_quiz.setChoice1(choice_list.get(0));
		literature_quiz.setChoice2(choice_list.get(1));
		literature_quiz.setChoice3(choice_list.get(2));
		literature_quiz.setChoice4(choice_list.get(3));
		literature_quiz.setCorrect_answer_index(correct_answer_index);
		literature_quiz.setExplanation(explanation);
		literature_quiz.setScore(score);
		literature_quiz.setLiterature_paragraph_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_paragraph_no());
		literature_quiz.setLiterature_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_no());
	
		return literature_quiz;
		
	}

	// 문학 문제 프롬프트 생성
	private String buildLiteratureQuizPrompt(String content, String level, String type) {

		return """
				아래 문학 작품 단락을 바탕으로 쉬운 난이도의 독해 퀴즈를 1개 만들어주세요.
                기사나 보도 형식이 아닌 문학 작품으로서 접근해주세요. 다음 형식을 반드시 지켜주세요:

                문제: <질문>
                A. 보기 A
                B. 보기 B
                C. 보기 C
                D. 보기 D
                정답: <A-D>
                해설: <간단한 설명>

                단락:
		""".formatted(level) + "\n" + content;
	
	}
	
	
	
	
	
	
}
