package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.LiteratureQuiz;

@Repository
public interface LiteratureQuizRepository extends JpaRepository<LiteratureQuiz, Long>{

	@Query("SELECT lq FROM LiteratureQuiz lq JOIN FETCH lq.literature_paragraph lp JOIN FETCH lp.literature  WHERE lq.literature_no = :literature_no AND lq.literature_paragraph_no = :literature_paragraph_no")
	LiteratureQuiz findByLiteratureParagraphNoAndLiteratureNo(
			@Param("literature_paragraph_no") Long literature_paragraph_no, 
			@Param("literature_no") Long literature_no);

	@Query("SELECT lq.literature_quiz_no FROM LiteratureQuiz lq JOIN lq.literature_paragraph lp JOIN lp.literature l WHERE lp.level = :level AND l.type = :type")
	List<Long> findLiteratureQuizNoByLevelAndType(
			@Param("level") String level,
			@Param("type") String type
	);

	@Modifying
	@Query(value = "DELETE FROM literature_quiz WHERE literature_paragraph_no = :literature_paragraph_no AND literature_no = :literature_no", nativeQuery = true)
	void deleteByLiteratureParagraphNoAndLiteratureNo(
			@Param("literature_paragraph_no") Long literature_paragraph_no, 
			@Param("literature_no") Long literature_no
	);

	@Query(value = "SELECT * FROM literature_quiz ORDER BY literature_quiz_no DESC", nativeQuery = true)
	List<LiteratureQuiz> findAllOrderByLiteratureQuizNoDesc();

}
