package com.readforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.NewsQuizAttempt;
import com.readforce.id.NewsQuizAttemptId;

@Repository
public interface NewsQuizAttemptRepository extends JpaRepository<NewsQuizAttempt, NewsQuizAttemptId>{

}
