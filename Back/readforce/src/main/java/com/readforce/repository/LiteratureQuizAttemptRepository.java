package com.readforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.LiteratureQuizAttempt;
import com.readforce.id.LiteratureQuizAttemptId;

@Repository
public interface LiteratureQuizAttemptRepository extends JpaRepository<LiteratureQuizAttempt, LiteratureQuizAttemptId>{

}
