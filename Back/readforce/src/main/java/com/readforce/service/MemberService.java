package com.readforce.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Status;
import com.readforce.exception.PasswordNotMatchException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository member_repository;
	private final PasswordEncoder password_encoder;

	// 회원 찾기

	
	// 회원 탈퇴
	@Transactional
	public void withdraw(String id) {
		
		// 회원 존재 유무 확인
		Member member = 
				member_repository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_ID));
		
		// 회원 상태 변경
		member.setStatus(Status.PENDING_DELETION);

	}

	// 아이디, 비밀번호 확인
	@Transactional
	public void idAndPasswordCheck(String id, String password) {
		
		// 회원 존재 유무 확인
		Member member =
				member_repository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_ID));
		
		Optional.ofNullable(password_encoder.matches(member.getPassword(), password)).orElseThrow(() -> new PasswordNotMatchException(MessageCode.ID_PASSWORD_NOT_MATCH));
		
	}
}
