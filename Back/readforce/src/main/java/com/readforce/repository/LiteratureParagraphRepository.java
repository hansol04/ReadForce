package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
import com.readforce.entity.LiteratureParagraph;
import com.readforce.id.LiteratureParagraphId;

@Repository
public interface LiteratureParagraphRepository extends JpaRepository<LiteratureParagraph, LiteratureParagraphId>{

//	@Query(value = "SELECT lp FROM LiteratureParagraph lp WHERE (SELECT l.literature_no FROM Literature l WHERE type = :type)")
//	List<GetLiteratureParagraph> getLiteratureParagraphListByTypeOrderByAsc(@Param("type") String type);
//
//	
//	List<GetLiteratureParagraph> getLiteratureParagraphListByTypeOrderByDesc(String type);

}
