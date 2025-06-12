package com.readforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.FailedDeletionLog;

@Repository
public interface FailedDeletionLogRepository extends JpaRepository<FailedDeletionLog, Long>{
	
}
