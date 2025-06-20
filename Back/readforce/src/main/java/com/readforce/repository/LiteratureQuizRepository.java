package com.readforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.LiteratureQuiz;

@Repository
public interface LiteratureQuizRepository extends JpaRepository<LiteratureQuiz, Long>{

	@Query(value = "SELECT * FROM literature_quiz WHERE literature_paragraph_no = :literature_paragraph_no AND literature_no = :literature_no", nativeQuery = true)
	LiteratureQuiz findByLiteratureParagraphNoAndLiteratureNo(
			@Param("literature_paragraph_no") Long literature_paragraph_no, 
			@Param("literature_no") Long literature_no);

}
