package com.readforce.id;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class NewsQuizAttemptId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private Long news_quiz_no;
	
	public NewsQuizAttemptId() {
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, news_quiz_no);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsQuizAttemptId other = (NewsQuizAttemptId) obj;
		return Objects.equals(email, other.email) && Objects.equals(news_quiz_no, other.news_quiz_no);
	}
	
}
