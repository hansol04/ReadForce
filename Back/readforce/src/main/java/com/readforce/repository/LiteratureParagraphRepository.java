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

	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no IN (SELECT literature_no FROM literature WHERE type = :type) ORDER BY created_date ASC", nativeQuery = true)
	List<LiteratureParagraph> getLiteratureParagraphListByTypeOrderByAsc(
			@Param("type") String type
	);

	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no IN (SELECT literature_no FROM literature WHERE type = :type) ORDER BY created_date DESC", nativeQuery = true)
	List<LiteratureParagraph> getLiteratureParagraphListByTypeOrderByDesc(
			@Param("type") String type
	);

	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no IN (SELECT literature_no FROM literature WHERE type = :type) AND level = :level ORDER BY created_date ASC", nativeQuery = true)
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelOrderByAsc(
			@Param("type") String type, 
			@Param("level") String level
	);

	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no IN (SELECT literature_no FROM literature WHERE type = :type) AND level = :level ORDER BY created_date DESC", nativeQuery = true)
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelOrderByDesc(
			@Param("type") String type, 
			@Param("level") String level
	);

	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no IN (SELECT literature_no FROM literature WHERE type = :type) AND level = :level AND category = :category ORDER BY created_date ASC", nativeQuery = true)
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByAsc(
			@Param("type") String type,
			@Param("level") String level,
			@Param("category") String category
	);
	
	@Query(value = "SELECT * FROM literature_paragraph WHERE literature_no IN (SELECT literature_no FROM literature WHERE type = :type) AND level = :level AND category = :category ORDER BY created_date DESC", nativeQuery = true)
	List<LiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByDesc(
			@Param("type") String type,
			@Param("level") String level,
			@Param("category") String category
	);
	
	
}
