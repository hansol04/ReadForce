package com.readforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.NeedAdminCheckFailedDeletionLog;

@Repository
public interface NeedAdminCheckFailedDeletionLogRepository extends JpaRepository<NeedAdminCheckFailedDeletionLog, Long>{

}
