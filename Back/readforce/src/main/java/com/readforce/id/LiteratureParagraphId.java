package com.readforce.id;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LiteratureParagraphId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long literature_paragraph_no;
	private Long literature_no;
	
	public LiteratureParagraphId() {
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(literature_no, literature_paragraph_no);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiteratureParagraphId other = (LiteratureParagraphId) obj;
		return Objects.equals(literature_no, other.literature_no)
				&& Objects.equals(literature_paragraph_no, other.literature_paragraph_no);
	}

}
