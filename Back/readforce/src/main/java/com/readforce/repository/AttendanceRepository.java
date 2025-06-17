package com.readforce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
	
	@Query(
			"SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Attendance a " +
			"WHERE a.email = :email AND a.created_date BETWEEN :start_of_day AND :end_of_day"
	)
	boolean existsByEmailAndCreatedDateBetween(
			@Param("email") String email, 
			@Param("start_of_day") LocalDateTime start_of_day, 
			@Param("end_of_day") LocalDateTime end_of_day);

	List<Attendance> findAllByEmail(String email);

	Long countByEmail(String email);

}
