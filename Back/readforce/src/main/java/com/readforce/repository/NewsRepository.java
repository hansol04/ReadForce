package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.dto.NewsDto.GetNews;
import com.readforce.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{

	@Query("SELECT n FROM News n WHERE n.language = :language ORDER BY created_date DESC")
	List<GetNews> findByLanguageOrderByCreatedDateDesc(@Param("language") String language);

	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level ORDER BY created_date DESC")
	List<GetNews> findByLanguageAndLevelOrderByCreatedDateDesc(
			@Param("language") String language, 
			@Param("level") String level
	);

}
