package com.readforce.id;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LiteratureQuizAttemptId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String email;
	private Long literature_quiz_no;
	
	public LiteratureQuizAttemptId() {

	}

	@Override
	public int hashCode() {
		return Objects.hash(email, literature_quiz_no);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiteratureQuizAttemptId other = (LiteratureQuizAttemptId) obj;
		return Objects.equals(email, other.email) && Objects.equals(literature_quiz_no, other.literature_quiz_no);
	}
	
}
