package com.readforce.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.MemberDto;
import com.readforce.dto.OAuthAttributesDto;
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Prefix;
import com.readforce.enums.Status;
import com.readforce.exception.AuthenticationException;
import com.readforce.exception.DuplicateException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService{
	
	private final MemberRepository member_repository;
	private final PasswordEncoder password_encoder;
	private final EmailService email_service;
	private final StringRedisTemplate redis_template;
	private final FileService file_service;

	// 회원 찾기
	@Transactional(readOnly = true)
	public Member getMemberObjectById(String email) {
		return member_repository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
	}

	
	// 회원 탈퇴
	@Transactional
	public void withdrawMember(String email) {
		
		// 회원 존재 유무 확인
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		// 회원 상태 변경
		member.setStatus(Status.PENDING_DELETION);
		member.setWithdrawDate(LocalDateTime.now());

	}
	
	// 회원 삭제(매일 새벽 4시 회원 탈퇴 후 30일이 지난 회원 정보를 영구 삭제)
	@Scheduled(cron = "0 0 4 * * ?")
	@Transactional
	public void deleteMember() {
		
		log.info("탈퇴 회원 정보 삭제 스케쥴러를 실행합니다.");
		LocalDateTime thirthyDaysAgo = LocalDateTime.now().minusDays(30);
		
		// 회원 탈퇴 후 30일이 지난 회원 조회
		List<Member> pending_deletion_member_list = member_repository.findAllByStatusAndWithdrawDateBefore(Status.PENDING_DELETION, thirthyDaysAgo);
		
		if(pending_deletion_member_list.isEmpty()) {
			
			log.info("삭제할 탈퇴 회원이 없습니다.");
		
		}else {
			
			// 회원과 연관된 정보 삭제
			for(Member member : pending_deletion_member_list) {
				
				// 프로필 이미지 삭제
				file_service.deleteFile(member.getProfile_image_url());;
				
				
			}
			
			log.info("{}명의 탈퇴 회원 정보를 삭제합니다.", pending_deletion_member_list.size());
			member_repository.deleteAll(pending_deletion_member_list);
			log.info("탈퇴 회원 정보 삭제를 완료했습니다.");
		}
		
	}

	

	// 회원 가입
	@Transactional
	public void signUp(MemberDto.SignUp sign_up) {
		
		// 이메일 인증 확인
		String result_message = 
				redis_template.opsForValue().get(Prefix.COMPLETE_EMAIL_VERIFY + sign_up.getEmail());
		if(result_message == null || !result_message.equals(MessageCode.VERIFICATION_CODE_VERIFY_SUCCESS)) {
			throw new AuthenticationException(MessageCode.EMAIL_VERIFICATION_REQUIRED);
		}
		
		// 중복 확인
		if(member_repository.findById(sign_up.getEmail()).isPresent()) {
			throw new DuplicateException(MessageCode.DUPLICATE_EMAIL);
		}
		if(member_repository.findByNickname(sign_up.getNickname()).isPresent()) {
			throw new DuplicateException(MessageCode.DUPLICATE_NICKNAME);
		}
		
		Member member = new Member();
		member.setEmail(sign_up.getEmail());
		member.setPassword(password_encoder.encode(sign_up.getPassword()));
		member.setNickname(sign_up.getNickname());
		member.setBirthday(sign_up.getBirthday());
		
		member_repository.save(member);
		
		// 토큰 삭제
		redis_template.delete(Prefix.COMPLETE_EMAIL_VERIFY + sign_up.getEmail());
		
	}
	
	// 이메일 중복 확인
	@Transactional(readOnly = true)
	public void emailCheck(String email) {
		
		if(member_repository.findByEmail(email).isPresent()) {
			throw new DuplicateException(MessageCode.DUPLICATE_EMAIL);
		}
		
	}

	// 닉네임 중복 확인
	@Transactional(readOnly = true)
	public void nicknameCheck(String nickname) {
		
		if(member_repository.findByNickname(nickname).isPresent()) {
			throw new DuplicateException(MessageCode.DUPLICATE_NICKNAME);
		}
		
	}

	// 회원 정보 수정
	@Transactional
	public void modifyInfo(String current_member_email, MemberDto.Modify modify) {
		
		// 회원 정보 조회
		Member member = 
				member_repository.findByEmailAndStatus(current_member_email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		// 닉네임
		if(modify.getNickname() != null) {
			String nickname = modify.getNickname();
			
			// 닉네임 중복 확인
			if(member_repository.findByNickname(nickname).isPresent()) {
				throw new DuplicateException(MessageCode.DUPLICATE_NICKNAME);
			}

			member.setNickname(nickname);
		}
		
		// 생년월일
		if(modify.getBirthday() != null) {
			LocalDate birthday = modify.getBirthday();
			member.setBirthday(birthday);
		}
		
	}

	// 비밀번호 재설정
	@Transactional
	public void passwordResetByLink(String token, String new_password) {
		
		// Redis에서 id 조회
		String member_email = redis_template.opsForValue().get(Prefix.PASSWORD_RESET_BY_LINK.getName() + token);
		
		if(member_email == null) {
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
		}
		
		// 회원 정보 불러오기
		Member member = 
				member_repository.findByEmailAndStatus(member_email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		// 비밀번호 재설정
		member.setPassword(password_encoder.encode(new_password));
		
		// 토큰 삭제
		redis_template.delete(Prefix.PASSWORD_RESET_BY_LINK.getName() + token);
		
		// 이메일 변경 알림 이메일 발송
		email_service.sendPasswordChangeNotification(member.getEmail());
		
	}

	// 토큰 생성
	public String createSignUpToken(String email, String prefix, long expire_time) {
		
		// 토큰 생성
		String token = UUID.randomUUID().toString();
		
		// Redis 저장
		redis_template.opsForValue().set(
				prefix + email,
				token,
				Duration.ofMinutes(expire_time)
		);
		
		return token;
		
	}

	// 소셜 회원가입
	@Transactional
	public String socialSignUp(@Valid MemberDto.SocialSignUp social_sign_up) {
		
		// 토큰 검증
		String email = redis_template.opsForValue().get(Prefix.SOCIAL_SIGN_UP.getName() + social_sign_up.getToken());
		if(email == null) {
			throw new AuthenticationException(MessageCode.TOKEN_ERROR);
		}
		
		// 닉네임 중복 확인
		if(member_repository.findByNickname(social_sign_up.getNickname()).isPresent()) {
			throw new DuplicateException(MessageCode.DUPLICATE_NICKNAME);
		}
		
		OAuthAttributesDto o_auth_attributes = OAuthAttributesDto.builder().email(email).build();
		Member new_member = o_auth_attributes.toEntity(social_sign_up.getNickname(), social_sign_up.getBirthday());
		
		new_member.setPassword(password_encoder.encode(new_member.getPassword()));
		
		// 새로운 회원 추가
		member_repository.save(new_member);
		
		// 토큰 삭제
		redis_template.delete(Prefix.SOCIAL_SIGN_UP.getName() + social_sign_up.getToken());
		
		return email;
		
	}
	
	

	
	
	
	
	
	
	
	
	
}
