package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.LiteratureQuizAttempt;
import com.readforce.id.LiteratureQuizAttemptId;

@Repository
public interface LiteratureQuizAttemptRepository extends JpaRepository<LiteratureQuizAttempt, LiteratureQuizAttemptId>{

	@Query("SELECT lqa FROM LiteratureQuizAttempt lqa JOIN FETCH lqa.literature_quiz WHERE lqa.literature_quiz_attempt_id.email = :email")
	List<LiteratureQuizAttempt> getMemberSolvedLiteratureQuizList(
			@Param("email") String email
	);

	@Query("SELECT lqa FROM LiteratureQuizAttempt lqa JOIN FETCH lqa.literature_quiz WHERE lqa.literature_quiz_attempt_id.email = :email AND is_correct = false")
	List<LiteratureQuizAttempt> getIncorrectLiteratureQuizAttemptList(
			@Param("email") String email
	);

	@Modifying
	@Query(value = "DELETE FROM literature_quiz_attempt WHERE email = :email", nativeQuery = true)
	void deleteByEmail(
			@Param("email") String email
	);

	@Query(value = "SELECT * FROM literature_quiz_attempt WHERE email = :email ORDER BY created_date DESC", nativeQuery = true)
	List<LiteratureQuizAttempt> findByEmailOrderByCreatedDateDesc(
			@Param("email") String email
	);

	@Query(value = "SELECT " +
				   " t.literature_quiz_no, " + 
				   " lq.question_text, " +
				   " t.incorrect_count, " +
				   " total.total_count " +
				   "FROM " +
				   " (SELECT literature_quiz_no, COUNT(*) as incorrect_count " +
				   " FROM literature_quiz_attempt " +
				   " WHERE is_correct = false " +
				   " GROUP BY literature_quiz_no) AS t " +
				   "LEFT JOIN " + 
				   " (SELECT literature_quiz_no, COUNT(*) as total_count " +
				   " FROM literature_quiz_attempt " +
				   " GROUP BY literature_quiz_no) AS total ON t.literature_quiz_no = total.literature_quiz_no " +
				   "JOIN " +
				   " literature_quiz lq ON t.literature_quiz_no = lq.literature_quiz_no ",
				   nativeQuery = true
	)
	List<Object[]> findIncorrectLiteratureQuizStatus();
	
}
