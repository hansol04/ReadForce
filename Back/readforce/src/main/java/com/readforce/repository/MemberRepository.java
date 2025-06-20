package com.readforce.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Member;
import com.readforce.enums.Status;


@Repository
public interface MemberRepository extends JpaRepository<Member, String>{

	@Query(value = "SELECT * FROM member WHERE social_provider = :social_provider AND social_provider_id = :social_provider_id", nativeQuery = true)
	Optional<Member> findBySocialProviderAndSocialProviderId(
			@Param("social_provider") String social_provider, 
			@Param("social_provider_id") String social_provider_id
	);

	@Query(value = "SELECT * FROM member WHERE status = :status AND withdraw_date < :thirthy_days_ago", nativeQuery = true)
	List<Member> findAllByStatusAndWithdrawDateBefore(
			@Param("status") Status status, 
			@Param("thirthy_days_ago") LocalDateTime thirthy_days_ago
	);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByEmailAndStatus(String email, Status active);

	@Query(value = "SELECT * FROM member", nativeQuery = true)
	List<Member> getAllMemberList();

	
}
