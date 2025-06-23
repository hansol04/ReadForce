package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.NewsQuizAttempt;
import com.readforce.id.NewsQuizAttemptId;

@Repository
public interface NewsQuizAttemptRepository extends JpaRepository<NewsQuizAttempt, NewsQuizAttemptId>{

	@Query("SELECT nqa FROM NewsQuizAttempt nqa JOIN FETCH nqa.news_quiz WHERE nqa.news_quiz_attempt_id.email = :email")
	List<NewsQuizAttempt> findByEmailWithNewQuizList(
			@Param("email") String email
	);

	@Query("SELECT nqa FROM NewsQuizAttempt nqa JOIN FETCH nqa.news_quiz WHERE nqa.news_quiz_attempt_id.email = :email AND nqa.is_correct = false")
	List<NewsQuizAttempt> getIncorrectNewQuizAttemptList(
			@Param("email") String email
	);

	@Modifying
	@Query(value = "DELETE FROM news_quiz_attempt WHERE email = :email", nativeQuery = true)
	void deleteByEmail(
			@Param("email") String email
	);

	@Query(value = "SELECT * FROM news_quiz_attempt WHERE email = :email", nativeQuery = true)
	List<NewsQuizAttempt> findByEmail(
			@Param("email") String email
	);



}
