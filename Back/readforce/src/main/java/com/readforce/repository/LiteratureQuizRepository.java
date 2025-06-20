package com.readforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.LiteratureQuiz;

@Repository
public interface LiteratureQuizRepository extends JpaRepository<LiteratureQuiz, Long>{

}
