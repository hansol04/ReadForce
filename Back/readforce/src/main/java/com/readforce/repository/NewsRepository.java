package com.readforce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{
	
	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'BEGINNER' ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
	Optional<News> findByLanguageAndBeginnerRandom(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'INTERMEDIATE' ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
	List<News> findByLanguageAndIntermediateRandom(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'ADVANCED' ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
	List<News> findByLanguageAndAdvancedRandom(
			@Param("language") String language
	);
	
	@Query(value = "SELECT * FROM news WHERE language = :language ORDER BY created_date ASC", nativeQuery = true)
	List<News> findByLanguageOrderByCreatedDateAsc(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language ORDER BY created_date DESC", nativeQuery = true)
	List<News> findByLanguageOrderByCreatedDateDesc(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level ORDER BY created_date ASC", nativeQuery = true)
	List<News> findByLanguageAndLevelOrderByCreatedDateAsc(
			@Param("language") String language,
			@Param("level") String level
	);
	
	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level ORDER BY created_date DESC", nativeQuery = true)
	List<News> findByLanguageAndLevelOrderByCreatedDateDesc(
			@Param("language") String language,
			@Param("level") String level
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level AND category = :category ORDER BY created_date ASC", nativeQuery = true)
	List<News> findByLanguageAndLevelAndCategoryOrderByCreatedDateAsc(
			@Param("language") String language,
			@Param("level") String level,
			@Param("category") String category
	);
	
	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level AND category = :category ORDER BY created_date DESC", nativeQuery = true)
	List<News> findByLanguageAndLevelAndCategoryOrderByCreatedDateDesc(
			@Param("language") String language,
			@Param("level") String level,
			@Param("category") String category
	);

	@Query(value = "SELECT * FROM news WHERE news_no NOT IN (SELECT news_no FROM news_quiz)", nativeQuery = true)
	List<News> findUnquizzedNews();

	@Query(value = "SELECT * FROM news ORDER BY news_no DESC", nativeQuery = true)
	List<News> findAllOrderBy();



}