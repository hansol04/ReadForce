package com.readforce.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
	
	boolean existsByMemberEmailAndCreatedDateBetween(String email, LocalDateTime start_of_day, LocalDateTime end_of_day);

}
