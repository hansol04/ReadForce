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
	
	@Query(value = "SELECT news_quiz_no, question_text, choice1, choice2, choice3, choice4, correct_answer_index, explanation, score, news_no FROM news_quiz WHERE news_no = :news_no", nativeQuery = true)
	Optional<GetNewsQuiz> findByNewsNo(
			@Param("news_no") Long news_no
	);

}
