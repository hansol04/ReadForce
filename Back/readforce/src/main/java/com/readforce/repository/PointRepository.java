package com.readforce.repository;

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


}
