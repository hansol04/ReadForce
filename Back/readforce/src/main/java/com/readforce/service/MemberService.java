package com.readforce.service;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.readforce.exception.JsonException;
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
	 public boolean existsByEmail(String email) {
	        return member_repository.findByEmailAndStatus(email, Status.ACTIVE).isPresent();
	    }
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
		get_member_object.setProvider(member.getSocial_provider());
		
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
		member.setWithdraw_date(LocalDateTime.now());
		
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
	public void passwordResetByLink(String temporal_token, String new_password, LocalDate birthday) {
		  log.info("[🔍 비밀번호 재설정 요청] token = {}, birthday = {}", temporal_token, birthday);
		// Redis에서 email 조회
		String member_email = redis_template.opsForValue().get(Prefix.PASSWORD_RESET_BY_LINK.getName() + temporal_token);
		
		if(member_email == null) {
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
		}
		
		// 회원 정보 불러오기
		Member member = 
				member_repository.findByEmailAndStatus(member_email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		
		// 생년월일 확인
		if(!birthday.equals(member.getBirthday())) {
			
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
			
		}
		
		// 비밀번호 재설정
		member.setPassword(password_encoder.encode(new_password));
		
		// 토큰 삭제
		redis_template.delete(Prefix.PASSWORD_RESET_BY_LINK.getName() + temporal_token);
		
		// 이메일 변경 알림 이메일 발송
		email_service.sendPasswordChangeNotification(member.getEmail());
		
	}

	// 소셜 회원가입
	@Transactional
	public void socialSignUp(@Valid MemberDto.SocialSignUp social_sign_up) {
		
		// Redis에서 소셜 정보 JSON 가져오기
		String social_info_json = redis_template.opsForValue().get(Prefix.SOCIAL_SIGN_UP.getName() + social_sign_up.getTemporal_token());

		if(social_info_json == null) {
			
			throw new AuthenticationException(MessageCode.TOKEN_ERROR);
			
		}
		
		// JSON을 Map으로 변환
		Map<String, String> social_info;
		try {
			
			social_info = new ObjectMapper().readValue(social_info_json, new TypeReference<Map<String, String>>() {});
			
		} catch(Exception exception) {
			
			throw new JsonException(MessageCode.JSON_PROCESSING_FAIL);
			
		}
		
		String email = social_info.get("email");
		String provider = social_info.get("provider");
		String provider_id = social_info.get("provider_id");
		
		// 닉네임 중복 확인
		if(member_repository.findByNickname(social_sign_up.getNickname()).isPresent()) {
			
			throw new DuplicateException(MessageCode.DUPLICATE_NICKNAME);
		
		}
		
		OAuthAttributesDto o_auth_attributes = OAuthAttributesDto.builder().email(email).build();
		Member new_member = o_auth_attributes.toEntity(social_sign_up.getNickname(), social_sign_up.getBirthday());
		
		new_member.setPassword(password_encoder.encode(new_member.getPassword()));
		
		// 소셜 정보 저장
		new_member.setSocial_provider(provider);
		new_member.setSocial_provider_id(provider_id);
		
		// 새로운 회원 추가
		member_repository.save(new_member);
		
		// 토큰 삭제
		redis_template.delete(Prefix.SOCIAL_SIGN_UP.getName() + social_sign_up.getTemporal_token());
		
	}

	// 비밀번호 변경
	@Transactional
	public void changePassword(String email, String new_password) {
		
		// 회원 정보 불러오기
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		member.setPassword(password_encoder.encode(new_password));
		
	}
	

	// 기존 회원과 소셜 계정 연동
	@Transactional
	public void linkSocialAccount(String signed_in_email, String provider, String provider_id, String social_email) {
		
		// 해당 소셜 계정이 다른 사용자에게 이미 연결되어있는지 확인
		member_repository.findBySocialProviderAndSocialProviderId(provider, provider_id)
			.ifPresent(member -> {
				if(!member.getEmail().equals(signed_in_email)) {
					throw new DuplicateException(MessageCode.SOCIAL_EMAIL_ALREADY_CONNECTED_WITH_OTHER_MEMBER);
				}
			});

		// 소셜 계정의 이메일이 다른 기존 회원의 이메일인지 확인
		member_repository.findByEmail(social_email)
			.ifPresent(member -> {
				if(!member.getEmail().equals(signed_in_email)) {
					throw new DuplicateException(MessageCode.SOCIAL_EMAIL_ALREADY_USE_BY_OTHER_MEMBER);
				}
			});
		
		// 현재 로그인된 사용자를 조회 및 소셜 정보 업데이트
		Member member = member_repository.findByEmailAndStatus(signed_in_email, Status.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));

		member.setSocial_provider(provider);
		member.setSocial_provider_id(provider_id);
		
	}

	// 관리자 - 모든 회원 조회
	@Transactional(readOnly = true)
	public List<GetMemberObject> getAllMemberList() {
		
		List<GetMemberObject> member_list = member_repository.getAllMemberList();
		
		if(member_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND);
			
		}
		
		return member_list;
	}

	// 관리자 - 계정 활성화
	@Transactional
	public void activateMember(String email) {
		
		Member member = member_repository.findByEmailAndStatus(email, Status.PENDING_DELETION)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.WITHDRAW_NOT_FOUND));
		
		member.setStatus(Status.ACTIVE);
		
	}
	
	
	
	
	
	
	
	
	
}
