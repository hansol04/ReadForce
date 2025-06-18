package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.dto.NewsDto.GetNewsPassage;
import com.readforce.entity.News;

@Repository
public interface NewsPassageRepository extends JpaRepository<News, Long>{

	@Query("SELECT np FROM NewsPassage np WHERE np.country = :country AND np.level = :level ORDER BY np.created_date DESC")
	List<GetNewsPassage> findByCountryAndLevelOrderByCreatedDate(
			@Param("country") String country, 
			@Param("level") String level
	);

	@Query("SELECT np FROM NewsPassage np WHERE np.country = :country ORDER BY np.created_date DESC")
	List<GetNewsPassage> findByCountryOrderByCreatedDate(@Param("country") String country);

}
