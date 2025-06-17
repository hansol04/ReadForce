package com.readforce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.dto.NewsDto.GetNewsQuiz;
import com.readforce.entity.NewsQuiz;

@Repository
public interface NewsQuizRepository extends JpaRepository<NewsQuiz, Long>{

	@Query("SELECT nq FROM NewsQuiz nq WHERE nq.news_passage_no = :news_passage_no")
	Optional<GetNewsQuiz> findByNewsPassageNo(@Param("news_passage_no") Long news_passage_no);

}
