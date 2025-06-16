package com.readforce.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Member;
import com.readforce.enums.Status;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{

	@Query("SELECT m FROM Member m WHERE m.social_provider = :social_provider AND m.social_provider_id = :social_procider_id")
	Optional<Member> findBySocialProviderAndSocialProviderId(@Param("social_provider") String socialProvider, @Param("social_procider_id") String socialProviderId);

	@Query("SELECT m FROM Member m WHERE m.status = status AND m.withdraw_date < :thirthy_days_ago")
	List<Member> findAllByStatusAndWithdrawDateBefore(@Param("status") Status status, @Param("thirthy_days_ago") LocalDateTime thirthy_days_ago);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByEmailAndStatus(String email, Status active);
	
}
