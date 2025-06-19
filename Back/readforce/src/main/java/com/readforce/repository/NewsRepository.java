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
	
	@Query("SELECT n FROM News n WHERE n.language = :language ORDER BY created_date :order_by")
	List<GetNews> findByLanguageOrderByCreatedDate(
			@Param("language") String language, 
			@Param("order_by") String order_by		
	);

	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level ORDER BY created_date :order_by")
	List<GetNews> findByLanguageAndLevelOrderByCreatedDate(
			@Param("language") String language, 
			@Param("level") String level, 
			@Param("order_by") String order_by
	);

	@Query("SELECT n FROM News n WHERE n.language = :language AND n.level = :level AND n.category ORDER BY created_date :order_by")
	List<GetNews> findByLanguageAndLevelAndCategoryOrderByCreatedDate(
			@Param("language") String language, 
			@Param("level") String level, 
			@Param("category") String category,
			@Param("order_by") String order_by
	);


}
