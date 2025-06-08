package com.readforce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Member;
import com.readforce.enums.Status;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{

	Optional<Member> findByIdAndStatus(String id, Status active);
	
}
