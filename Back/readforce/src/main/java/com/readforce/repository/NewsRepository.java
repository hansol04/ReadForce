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

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'beginner' ORDER BY RANDOM() LIMIT 1", nativeQuery = true)

	Optional<GetNews> findByLanguageAndBeginnerRandom(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'intermediate' ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
	List<GetNews> findByLanguageAndIntermediateRandom(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = 'advanced' ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
	List<GetNews> findByLanguageAndAdvancedRandom(
			@Param("language") String language
	);
	
	@Query(value = "SELECT * FROM news WHERE language = :language ORDER BY created_date ASC", nativeQuery = true)

	List<GetNews> findByLanguageOrderByCreatedDateAsc(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language ORDER BY created_date DESC", nativeQuery = true)
	List<GetNews> findByLanguageOrderByCreatedDateDesc(
			@Param("language") String language
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level ORDER BY created_date ASC", nativeQuery = true)
	List<GetNews> findByLanguageAndLevelOrderByCreatedDateAsc(
			@Param("language") String language,
			@Param("level") String level
	);
	
	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level ORDER BY created_date DESC", nativeQuery = true)

	List<GetNews> findByLanguageAndLevelOrderByCreatedDateDesc(
			@Param("language") String language,
			@Param("level") String level
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level AND category = :category ORDER BY created_date ASC", nativeQuery = true)
	List<GetNews> findByLanguageAndLevelAndCategoryOrderByCreatedDateAsc(
			@Param("language") String language,
			@Param("level") String level,
			@Param("category") String category
	);

	@Query(value = "SELECT * FROM news WHERE language = :language AND level = :level AND category = :category ORDER BY created_date DESC", nativeQuery = true)

	List<GetNews> findByLanguageAndLevelAndCategoryOrderByCreatedDateDesc(
			@Param("language") String language,
			@Param("level") String level,
			@Param("category") String category
	);

	@Query(value = "SELECT * FROM news WHERE news_no NOT IN (SELECT news_no FROM news_quiz)", nativeQuery = true)
	List<News> findUnquizzedNews();


}