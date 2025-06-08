package com.readforce.enums;

public final class MessageCode {
	
	public static final String MESSAGE_CODE = "MESSAGE_CODE";
	
	// 입력값 누락
	public static final String EMAIL_NOT_BLANK = "NO0001";
	public static final String CODE_NOT_BLANK = "NO0002";
	public static final String ID_NOT_BLANK = "NO0003";
	public static final String PASSWORD_NOT_BLANK = "NO0004";
	public static final String NICKNAME_NOT_BLANK = "NO0005";
	public static final String NAME_NOT_BLANK = "NO0006";
	public static final String BIRTHDAY_NOT_NULL = "NO0007";
	
	// 입력값 형식 불일치
	public static final String EMAIL_PATTERN_INVALID = "PA0001";
	public static final String ID_PATTERN_INVALID = "PA0002";
	public static final String PASSWORD_PATTERN_INVALID = "PA0003";
	public static final String NICKNAME_PATTERN_INVALID = "PA0004";
	public static final String NAME_PATTERN_INVALID = "PA0005";
	
	// 입력값 크기/범위 불일치
	public static final String ID_SiZE_INVALID = "SI0001";
	public static final String PASSWORD_SIZE_INVALID = "SI0002";
	public static final String NICKNAME_SIZE_INVALID = "SI0003";
	public static final String NAME_SIZE_INVALID = "SI0004";
	public static final String BIRTHDAY_RANGE_INVALID = "SI0005";
	
	// 서버 오류
	public static final String INTERNER_SERVER_ERROR = "SE0001";
	
	// 성공
	public static final String VERIFICATION_CODE_SEND_SUCCESS = "SU0001";
	public static final String VERIFICATION_CODE_VERIFY_SUCCESS = "SU0002";
	public static final String MEMBER_WITHDRAW_SUCCESS = "SU0003";
	
	// 실패
	public static final String VERIFICATION_CODE_SEND_FAIL = "FA0001";
	public static final String VERIFICATION_CODE_VERIFY_FAIL = "FA0002";
	
	// 데이터 없음
	public static final String MEMBER_NOT_FOUND_WITH_ID = "SP0001";
	
	// 특정 오류
	public static final String ID_PASSWORD_NOT_MATCH = "IP0001";
	
}
