package com.readforce.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.entity.Member;
import com.readforce.enums.Status;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{

	Optional<Member> findBySocialProviderAndSocialProviderId(String social_provider, String social_provider_id);

	List<Member> findAllByStatusAndWithdrawDateBefore(Status pendingDeletion, LocalDateTime thirthyDaysAgo);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByEmailAndStatus(String email, Status active);
	
}
