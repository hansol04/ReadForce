package com.readforce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, String>{

	@Query(value = "SELECT * FROM point WHERE email = :email", nativeQuery = true)
	Optional<Point> findByEmail(
			@Param("email") String email
	);

	@Modifying
	@Query(value = "DELETE FROM point WHERE email = :email", nativeQuery = true)
	void deleteByEmail(
			@Param("email") String email
	);

	@Query(value = "SELECT * FROM point ORDER BY korean_news DESC LIMIT 50", nativeQuery = true)
	List<Point> findAllWithKoreanNews();

	@Query(value = "SELECT * FROM point ORDER BY english_news DESC LIMIT 50", nativeQuery = true)
	List<Point> findAllWithEnglishNews();

	@Query(value = "SELECT * FROM point ORDER BY japanese_news DESC LIMIT 50", nativeQuery = true)
	List<Point> findAllWithJapaneseNews();

	@Query(value = "SELECT * FROM point ORDER BY novel DESC LIMIT 50", nativeQuery = true)
	List<Point> findAllWithNovel();

	@Query(value = "SELECT * FROM point ORDER BY fairytale DESC LIMIT 50", nativeQuery = true)
	List<Point> findAllWithFairytale();

	@Query(value = "SELECT * FROM point ORDER BY created_date DESC", nativeQuery = true)
	List<Point> findAllOrderByCreatedDateDesc();

	@Modifying
	@Query("UPDATE Point p SET p.korean_news = 0.0, p.english_news = 0.0, p.japanese_news = 0.0, p.novel = 0.0, p.fairytale = 0.0, p.total = 0.0")
	void resetAllPoint();


}
