package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.LiteratureParagraph;
import com.readforce.id.LiteratureParagraphId;

@Repository
public interface LiteratureParagraphRepository extends JpaRepository<LiteratureParagraph, LiteratureParagraphId>{

	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature l WHERE l.type = :type ORDER BY lp.created_date ASC")
	List<LiteratureParagraph> getLiteratureParagraphListByTypeOrderByAsc(
			@Param("type") String type
	);

	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature l WHERE l.type = :type ORDER BY lp.created_date DESC")
	List<LiteratureParagraph> getLiteratureParagraphListByTypeOrderByDesc(
			@Param("type") String type
	);

	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature l WHERE l.type = :type AND lp.level = :level ORDER BY lp.created_date ASC")
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelOrderByAsc(
			@Param("type") String type, 
			@Param("level") String level
	);

	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature l WHERE l.type = :type AND lp.level = :level ORDER BY lp.created_date DESC")
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelOrderByDesc(
			@Param("type") String type, 
			@Param("level") String level
	);

	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature l WHERE l.type = :type AND lp.level = :level AND lp.category = :category ORDER BY lp.created_date ASC")
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByAsc(
			@Param("type") String type,
			@Param("level") String level,
			@Param("category") String category
	);
	
	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature l WHERE l.type = :type AND lp.level = :level AND lp.category = :category ORDER BY lp.created_date DESC")
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByDesc(
			@Param("type") String type,
			@Param("level") String level,
			@Param("category") String category
	);

	@Query("SELECT lp FROM LiteratureParagraph lp JOIN FETCH lp.literature LEFT JOIN LiteratureQuiz lq ON lq.literature_paragraph = lp WHERE lq.literature_quiz_no IS NULL")
	List<LiteratureParagraph> findLiteratureParagraphListWithoutLiteratureQuiz();

	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no = :literature_no", nativeQuery = true)
	LiteratureParagraph findByLiterature_no(
			@Param("literature_no") Long literature_no
	);

	@Query(value = "SELECT MAX(lp.literature_paragraph_no) FROM literature_paragraph lp WHERE lp.literature_no = literature_no", nativeQuery = true)
	Long findLastLiteratureParagraphNoByLiteratureNo(
			@Param("literature_no") Long literature_no
	);

	@Query(value = "SELECT * FROM literature_paragraph ORDER BY literature_paragraph_no Asc", nativeQuery = true)
	List<LiteratureParagraph> findAllOrderByLiteratureParagraphNoAsc();
	
}
