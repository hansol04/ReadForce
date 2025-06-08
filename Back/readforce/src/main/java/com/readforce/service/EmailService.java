package com.readforce.service;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender java_mail_sender;
	private final RedisTemplate<String, String> redis_template;
	private static final String AUTH_CODE_PREFIX = "AuthCode_";
	private static final long AUTH_CODE_EXPIRATION = 5; // 5분
	
	// 인증코드 생성
	public String createAuthCode() {
		
		return String.valueOf(100000 + new Random().nextInt(900000));
	
	}
	
	// 인증코드 보내기
	public void sendVerificationCode(String email) {
		
		// 내용 작성
		String auth_code = createAuthCode();
		String subject = "[ReadForce] 이메일 인증 코드 안내";
		String text = 
				"ReadForce에 가입해 주셔서 감사합니다.\n"
				+ "인증 코드 : " + auth_code + "\n"
				+ "이 코드는 5분간 유효합니다.";
		
		// Redis에 인증 코드 저장
		redis_template.opsForValue().set(
				AUTH_CODE_PREFIX + email,
				auth_code,
				Duration.ofMinutes(AUTH_CODE_EXPIRATION) // 유효기간 설정
		);
		
		// 이메일 전송
		SimpleMailMessage simple_mail_message = new SimpleMailMessage();
		simple_mail_message.setTo(email);
		simple_mail_message.setSubject(subject);
		simple_mail_message.setText(text);
		
		java_mail_sender.send(simple_mail_message);
		
	}
	
	// 인증코드 확인
	public boolean verifyVerificationCode(String email, String code) {
		
		// Redis에서 인증 코드 가져오기
		String stored_code = redis_template.opsForValue().get(AUTH_CODE_PREFIX + email);
		if(stored_code != null && stored_code.equals(code)) {
			redis_template.delete(AUTH_CODE_PREFIX + email);
			return true;
		}
		return false;
	
	}
	
	
}
