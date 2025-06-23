package com.readforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Literature;

@Repository
public interface LiteratureRepository extends JpaRepository<Literature, Long> {

	@Query(value = "SELECT * FROM literature ORDER BY literature_no Desc", nativeQuery = true)
	List<Literature> findAllOrderByLiteratureNoDesc();

}
