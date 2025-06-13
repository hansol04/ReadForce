package com.readforce.service;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.MemberDto;
import com.readforce.dto.MemberDto.GetMemberObject;
import com.readforce.dto.OAuthAttributesDto;
import com.readforce.entity.Member;
import com.readforce.entity.NeedAdminCheckFailedDeletionLog;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Prefix;
import com.readforce.enums.Status;
import com.readforce.exception.AuthenticationException;
import com.readforce.exception.DuplicateException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;
import com.readforce.repository.NeedAdminCheckFailedDeletionLogRepository;

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
	private final NeedAdminCheckFailedDeletionLogRepository need_admin_check_failed_deletion_log_repository;
	private final MemberRepository memberRepository; //김기찬 관리자 페이짖
	
	@Value("${file.image.profile.upload-dir}")
	private String profile_image_upload_dir;

	// 회원 찾기
	@Transactional(readOnly = true)
	public MemberDto.GetMemberObject getMemberObjectByEmail(String email) {
		
		// 회원 조회
		Member member = member_repository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		// DTO에 옮기기
		GetMemberObject get_member_object = new GetMemberObject();
		get_member_object.setEmail(member.getEmail());
		get_member_object.setNickname(member.getNickname());
		get_member_object.setBirthday(member.getBirthday());
		
		return get_member_object;
		
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
		
		// 리프레쉬 토큰 삭제
		redis_template.delete(Prefix.REFRESH_TOKEN.getName() + email);

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
			return;
			
		}

		// 파일 삭제 시도 후 실패시 로그만 생성
		for(Member member : pending_deletion_member_list) {
			
			// 프로필 이미지가 비어있으면 성공 리스트에 추가
			if(member.getProfile_image_url() == null || member.getProfile_image_url().isEmpty()) {
				continue;
			}
			
			try {
				
				// 파일 삭제 시도
				file_service.deleteFile(member.getProfile_image_url(), profile_image_upload_dir);
				
			} catch(Exception exception) {
				
				// 관리자 확인 필요 삭제 실패 로그 테이블 생성
				log.error("프로필 이미지 삭제 실패. Member DB 레코드는 삭제됩니다. Member email : {}, File : {}", member.getEmail(), member.getProfile_image_url(), exception);
				NeedAdminCheckFailedDeletionLog need_admin_check_failed_deletion_log = new NeedAdminCheckFailedDeletionLog(
						member.getEmail(),
						member.getProfile_image_url(),
						exception.getMessage()
				);
				need_admin_check_failed_deletion_log_repository.save(need_admin_check_failed_deletion_log);
			}
		}
		
		// 탈퇴 회원 DB에서 삭제
		log.info("{}명의 탈퇴 회원 정보를 DB에서 삭제합니다.", pending_deletion_member_list.size());
		member_repository.deleteAllInBatch(pending_deletion_member_list);
		log.info("회원 정보 DB 삭제를 성공했습니다.");
	
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
	public void passwordResetByLink(String temporal_token, String new_password) {
		
		// Redis에서 id 조회
		String member_email = redis_template.opsForValue().get(Prefix.PASSWORD_RESET_BY_LINK.getName() + temporal_token);
		
		if(member_email == null) {
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
		}
		
		// 회원 정보 불러오기
		Member member = 
				member_repository.findByEmailAndStatus(member_email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		// 비밀번호 재설정
		member.setPassword(password_encoder.encode(new_password));
		
		// 토큰 삭제
		redis_template.delete(Prefix.PASSWORD_RESET_BY_LINK.getName() + temporal_token);
		
		// 이메일 변경 알림 이메일 발송
		email_service.sendPasswordChangeNotification(member.getEmail());
		
	}

	// 소셜 회원가입
	@Transactional
	public String socialSignUp(@Valid MemberDto.SocialSignUp social_sign_up) {
		
		// 토큰 검증
		String email = redis_template.opsForValue().get(Prefix.SOCIAL_SIGN_UP.getName() + social_sign_up.getTemporal_token());
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
		redis_template.delete(Prefix.SOCIAL_SIGN_UP.getName() + social_sign_up.getTemporal_token());
		
		return email;
		
	}

	// 비밀번호 변경
	@Transactional
	public void changePassword(String email, String new_password) {
		
		// 회원 정보 불러오기
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		member.setPassword(password_encoder.encode(new_password));
		
	}
	
	// 김기찬 관리잗ㅈㄹ더지ㅏㅜ
	public List<GetMemberObject> getAllMemberObjects() {
	    return memberRepository.findAll().stream()
	            .map(MemberDto::from)
	            .collect(Collectors.toList());
	}

	
	
	
	
	
	
	
	
	
}
