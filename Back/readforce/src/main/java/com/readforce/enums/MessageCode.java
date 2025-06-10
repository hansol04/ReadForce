package com.readforce.enums;

public final class MessageCode {
	
	public static final String MESSAGE_CODE = "MESSAGE_CODE";
	
	// 입력값 누락
	public static final String EMAIL_NOT_BLANK = "NO0001";
	public static final String CODE_NOT_BLANK = "NO0002";
	public static final String PASSWORD_NOT_BLANK = "NO0003";
	public static final String NICKNAME_NOT_BLANK = "NO0004";
	public static final String BIRTHDAY_NOT_NULL = "NO0005";
	public static final String TOKEN_NOT_BLANK = "NO0006";
	
	// 입력값 형식 불일치
	public static final String EMAIL_PATTERN_INVALID = "PA0001";
	public static final String PASSWORD_PATTERN_INVALID = "PA0002";
	public static final String NICKNAME_PATTERN_INVALID = "PA0003";

	// 입력값 크기/범위 불일치
	public static final String PASSWORD_SIZE_INVALID = "SI0001";
	public static final String NICKNAME_SIZE_INVALID = "SI0002";
	public static final String BIRTHDAY_RANGE_INVALID = "SI0003";
	
	// 서버 오류
	public static final String INTERNER_SERVER_ERROR = "SE0001";
	
	// 성공
	public static final String VERIFICATION_CODE_SEND_SUCCESS = "SU0001";
	public static final String VERIFICATION_CODE_VERIFY_SUCCESS = "SU0002";
	public static final String MEMBER_WITHDRAW_SUCCESS = "SU0003";
	public static final String SIGN_IN_SUCCESS = "SU0004";
	public static final String SIGN_UP_SUCCESS = "SU0005";
	public static final String MEMBER_INFO_MODIFY_SUCCESS = "SU0006";
	public static final String PASSWORD_RESET_SUCCESS = "SU0007";
	public static final String SEND_PASSWORD_RESET_LINK_SUCCESS = "SU0008";
	
	// 실패
	public static final String VERIFICATION_CODE_SEND_FAIL = "FA0001";
	public static final String VERIFICATION_CODE_VERIFY_FAIL = "FA0002";
	public static final String AUTHENTICATION_FAIL = "FA0003";
	
	// 데이터 없음
	public static final String MEMBER_NOT_FOUND_WITH_EMAIL = "NF0001";
	
	
	// 특정 상황
	public static final String DUPLICATE_EMAIL = "SP0001";
	public static final String EMAIL_CAN_USE = "SP0002";
	public static final String DUPLICATE_NICKNAME = "SP0003";
	public static final String NICKNAME_CAN_USE = "SP0004";
	public static final String VALUE_NOT_MATCH = "SP0005";
	public static final String TOKEN_ERROR = "SP0006";
	public static final String CHECK_JWT_SECERET_KEY = "SP0007";
	public static final String EMAIL_VERIFICATION_REQUIRED = "SP0008";
	
}
