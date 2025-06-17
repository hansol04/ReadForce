package com.readforce.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.readforce.enums.ExpireTime;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Prefix;
import com.readforce.enums.Status;
import com.readforce.exception.AuthenticationException;
import com.readforce.exception.RateLimitExceededException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final MemberRepository member_repository;
	private final JavaMailSender java_mail_sender;
	private final StringRedisTemplate redis_template;
	private final String SIGN_UP_MESSAGE = "ReadForce에 가입하신 것을 환영합니다.";
	private final String DEFAULT_MESSAGE = "ReadForce을 이용해주셔서 감사합니다.";
	
	@Value("${rate-limiting.email-verification.max-requests}")
	private int email_verification_max_requests;

	@Value("${rate-limiting.email-verification.per-hour}")
	private int email_verification_per_hour;
	
	@Value("${custom.fronted.password-reset-link-url}")
	private String password_reset_url;
	
	
	// 인증 번호 생성
	private String createAuthCode() {
		
		return String.valueOf(100000 + new SecureRandom().nextInt(900000));
	
	}
	
	// 인증 코드 보내기
	private void sendVerificationCode(
			String email, 
			String message, 
			String auth_code, 
			long expired_time,
			String prefix
	) {
		
		// 내용 작성
		String subject = "[ReadForce] 이메일 인증 안내";
		String text = 
				message + "\n"
				+ "인증 번호 : " + auth_code + "\n"
				+ "이 코드는 " + expired_time +"분간 유효합니다.";
		
		// Redis에 인증 번호 저장
		redis_template.opsForValue().set(
				prefix + email,
				auth_code,
				Duration.ofMinutes(expired_time) // 유효기간 설정
		);
		
		// 이메일 전송
		SimpleMailMessage simple_mail_message = new SimpleMailMessage();
		simple_mail_message.setTo(email);
		simple_mail_message.setSubject(subject);
		simple_mail_message.setText(text);
		
		java_mail_sender.send(simple_mail_message);
	}
	
	// 인증 코드 확인
	private void VerifyVerificationCode(
			String email, 
			String code, 
			String prefix
	) {
		
		// Redis에서 인증 코드 가져오기
		String stored_code = redis_template.opsForValue().get(prefix + email);
		if(stored_code == null || !stored_code.equals(code)) {
			throw new AuthenticationException(MessageCode.VERIFICATION_CODE_VERIFY_FAIL);
		}
		// 인증 코드 삭제
		redis_template.delete(prefix + email);
	
	}
	
	
	// 회원 가입 인증 번호 전송
	public void sendVerificationCodeSignUp(String email) {
		
		checkAndIncrementVerificationAttempts(email);
		
		sendVerificationCode(
				email, 
				SIGN_UP_MESSAGE,
				createAuthCode(),
				ExpireTime.DEFAULT.getTime(),
				Prefix.SIGN_UP.getName()
		);
		
	}

	// 회원 가입 인증 번호 확인
	public void verifyVerificationCodeSignUp(String email, String code) {

		VerifyVerificationCode(
				email,
				code,
				Prefix.SIGN_UP.getName()
		);
		
	}
	
	// 비밀번호 재설정 링크 전송
	public void sendPasswordResetLink(String email) {
		
		// 가입된 이메일인지 확인
		member_repository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		// 토큰 생성
		String temporal_token = UUID.randomUUID().toString();
		
		// 내용 작성
		String subject = "[ReadForce] 비밀번호 재설정 안내";
		String text = 
				DEFAULT_MESSAGE + "\n"
				+ "비밀번호를 재설정 하시려면 아래의 링크를 눌러주세요.\n"
				+ password_reset_url + "?token=" + temporal_token;
		
		redis_template.opsForValue().set(
				Prefix.PASSWORD_RESET_BY_LINK.getName() + temporal_token,
				email,
				Duration.ofMinutes(ExpireTime.DEFAULT.getTime())
		);
		
		// 이메일 전송
		SimpleMailMessage simple_mail_message = new SimpleMailMessage();
		simple_mail_message.setTo(email);
		simple_mail_message.setSubject(subject);
		simple_mail_message.setText(text);
		
		java_mail_sender.send(simple_mail_message);
		
	};
	
	
	// 비밀번호 재설정 완료 알림
	public void sendPasswordChangeNotification(String email) {
		
		String subject = "[ReadForce] 비밀번호 재설정 안내";
		String text = 
				DEFAULT_MESSAGE + "\n"
				+ "비밀번호 변경이 완료되었습니다.";
		
		// 이메일 전송
		SimpleMailMessage simple_mail_message = new SimpleMailMessage();
		simple_mail_message.setTo(email);
		simple_mail_message.setSubject(subject);
		simple_mail_message.setText(text);
		
		java_mail_sender.send(simple_mail_message);
		
	}

	// 이메일 인증 완료 상태 저장
	public void markEmailAsVerified(String email) {
		
		redis_template.opsForValue().set(
				Prefix.COMPLETE_EMAIL_VERIFY + email, 
				MessageCode.VERIFICATION_CODE_VERIFY_SUCCESS,
				Duration.ofMinutes(ExpireTime.VIERIFIED_TIME.getTime())
		);
		
	}

	// 이메일 인증 요청 횟수 제한
	private void checkAndIncrementVerificationAttempts(String email) {
		
		ValueOperations<String, String> operations = redis_template.opsForValue();
		
		// Redis key 생성
		String key = Prefix.EMAIL_VERIFY_ATTEMPT_PREFIX.getName() + email;
		
		// 현재 요청 횟수 확인
		String current_attempts_string = operations.get(key);
		int current_attempts = (current_attempts_string == null) ? 0 : Integer.parseInt(current_attempts_string);
		
		if(current_attempts >= email_verification_max_requests) {
			
			throw new RateLimitExceededException(MessageCode.EMAIL_REQUEST_LIMIT_EXCEEDED);
			
		}
		
		// 횟수 증가
		operations.increment(key);
		
		// 키가 새로 생성된 경우
		if(current_attempts == 0) {
			
			redis_template.expire(key, email_verification_per_hour, TimeUnit.HOURS);
			
		}
		
		
	}
	
	
	
}
