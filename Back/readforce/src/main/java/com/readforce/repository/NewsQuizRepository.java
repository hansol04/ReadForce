package com.readforce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	@Query("SELECT nq.news_quiz_no FROM NewsQuiz nq JOIN nq.news n WHERE n.level = :level AND n.language = :language")
	List<Long> findNewsQuizNoByLevelAndLanguage(
			@Param("level") String level,
			@Param("language") String language
	);

	@Modifying
	@Query(value = "DELETE FROM news_quiz WHERE news_no = :news_no", nativeQuery = true)
	void deleteByNewsNo(
			@Param("news_no") Long news_no
	);

	@Query(value = "SELECT * FROM news_quiz ORDER BY news_quiz_no DESC", nativeQuery = true)
	List<NewsQuiz> findAllOrderByNewsQuizNoDesc();

}
