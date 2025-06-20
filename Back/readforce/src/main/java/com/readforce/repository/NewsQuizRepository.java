package com.readforce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.NewsQuiz;

@Repository
public interface NewsQuizRepository extends JpaRepository<NewsQuiz, Long>{
	
	@Query(value = "SELECT * FROM news_quiz WHERE news_no = :news_no", nativeQuery = true)
	Optional<NewsQuiz> findByNewsNo(
			@Param("news_no") Long news_no
	);

}
