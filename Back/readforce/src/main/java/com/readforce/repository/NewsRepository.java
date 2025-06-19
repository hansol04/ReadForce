package com.readforce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.dto.NewsDto.GetNews;
import com.readforce.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{
	
	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'beginner' ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Optional<GetNews> findByLanguageAndBeginnerRandom(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'intermediate' ORDER BY RAND() LIMIT 2", nativeQuery = true)
	List<GetNews> findByLanguageAndIntermediateRandom(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'advanced' ORDER BY RAND() LIMIT 2", nativeQuery = true)
	List<GetNews> findByLanguageAndAdvancedRandom(
			@Param("language") String language
	);
	
	@Query("SELECT n FROM News n WHERE n.language = :language ORDER BY n.created_date ASC")
	List<GetNews> findByLanguageOrderByCreatedDateAsc(
			@Param("language") String language
	);

	@Query("SELECT n FROM News n WHERE n.language = :language ORDER BY n.created_date DESC")
	List<GetNews> findByLanguageOrderByCreatedDateDesc(
			@Param("language") String language
	);

	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level ORDER BY n.created_date ASC")
	List<GetNews> findByLanguageAndLevelOrderByCreatedDateAsc(
			@Param("language") String language, 
			@Param("level") String level
	);
	
	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level ORDER BY n.created_date DESC")
	List<GetNews> findByLanguageAndLevelOrderByCreatedDateDesc(
			@Param("language") String language, 
			@Param("level") String level
	);

	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level AND n.category = :category ORDER BY n.created_date ASC")
	List<GetNews> findByLanguageAndLevelAndCategoryOrderByCreatedDateAsc(
			@Param("language") String language, 
			@Param("level") String level,
			@Param("category") String category
	);
	
	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level AND n.category = :category ORDER BY n.created_date DESC")
	List<GetNews> findByLanguageAndLevelAndCategoryOrderByCreatedDateDesc(
			@Param("language") String language, 
			@Param("level") String level,
			@Param("category") String category
	);

	
	
	


}
