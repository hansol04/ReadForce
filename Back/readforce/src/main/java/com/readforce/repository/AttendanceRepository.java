package com.readforce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
	
	List<Attendance> findByEmail(String email); // 김기찬 추
	
	boolean existsByMemberEmailAndCreatedDateBetween(String email, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
