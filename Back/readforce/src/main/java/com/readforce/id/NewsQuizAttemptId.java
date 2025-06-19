package com.readforce.id;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class NewsQuizAttemptId implements Serializable{

	private static final long serialVersionUID = 1L;

	private String email;
	private Long news_quiz_no;
	
	public NewsQuizAttemptId() {
		
	}
	
	@Override
	public boolean equals(Object object) {
		
		if(this == object) return true;
		if(object == null || getClass() != object.getClass()) return false;
		NewsQuizAttemptId news_quiz_attempt_id = (NewsQuizAttemptId) object;
		return Objects.equals(email, news_quiz_attempt_id.email) && (Objects.equals(news_quiz_no, news_quiz_attempt_id.news_quiz_no));
		
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(email, news_quiz_no);
		
	}


}
