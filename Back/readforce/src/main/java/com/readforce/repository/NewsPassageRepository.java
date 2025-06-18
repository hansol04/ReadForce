package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.dto.NewsDto.GetNewsPassage;
import com.readforce.entity.NewsPassage;

@Repository
public interface NewsPassageRepository extends JpaRepository<NewsPassage, Long>{

	@Query(value = "SELECT * FROM news_passage WHERE country = :country AND level = :level ORDER BY created_date DESC", nativeQuery = true)
	List<NewsPassage> findByCountryAndLevelOrderByCreatedDate(
			@Param("country") String country, 
			@Param("level") String level
	);

}
