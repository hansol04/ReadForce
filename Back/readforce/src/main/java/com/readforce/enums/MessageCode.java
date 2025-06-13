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
	public static final String FILE_NOT_NULL = "NO0007";
	public static final String REFRESH_TOKEN_NOT_BLANK = "NO0008";
	public static final String NEWS_ARTICLE_TITLE_NOT_BLANK = "NO0009";
	public static final String NEWS_ARTICLE_DESCRIPTION_NOT_BLANK = "NO0010";
	public static final String NEWS_ARTICLE_CONTENT_NOT_BLANK = "NO0011";
	public static final String NEWS_ARTICLE_LANGUAGE_NOT_BLANK = "NO0012";
	public static final String NEWS_API_URL_NOT_BLANK = "NO0013";
	public static final String QUIZ_ARTICLE_NOT_BLANK = "NO0014";
	public static final String QUIZ_LANGUAGE_NOT_BLANK = "NO0015";
	public static final String NEWS_LANGUAGE_NOT_BLANK = "NO0016";
	public static final String NEWS_PAGE_NOT_NULL = "NO0017";
	public static final String NEWS_SIZE_NOT_NULL = "NO0018";
	
	// 입력값 형식 불일치
	public static final String EMAIL_PATTERN_INVALID = "PA0001";
	public static final String PASSWORD_PATTERN_INVALID = "PA0002";
	public static final String NICKNAME_PATTERN_INVALID = "PA0003";
	public static final String FILE_TYPE_INVALID = "PA0004";
	public static final String GEMINI_RESPONSE_PATTERN_INVALID = "PA0005";
	public static final String QUIZ_ANSWER_PATTERN_INVALID = "PA0006"; // 정답 형식 오류 A~D 이외의 문자
	public static final String NEWS_LANGUAGE_PATTERN_INVALID = "PA0007"; // kr, jp, en 이외의 값이 입력됨
	
	// 입력값 크기/범위 불일치
	public static final String PASSWORD_SIZE_INVALID = "SI0001";
	public static final String NICKNAME_SIZE_INVALID = "SI0002";
	public static final String BIRTHDAY_RANGE_INVALID = "SI0003";
	public static final String FILE_SIZE_INVALID = "SI0004";
	public static final String QUIZ_ANSWER_INDEX_SIZE_INVALID = "SI0005"; // 정답 인덱스가 보기 개수를 초과
	
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
	public static final String PROFILE_IMAGE_UPLOAD_SUCCESS = "SU0009";
	public static final String PROFILE_IMAGE_DELETE_SUCCESS = "SU0010";
	public static final String TOKEN_SUCCESS = "SU0011"; // 엑세스 토큰 재발급 성공
	public static final String NEWS_TRANSFORM_SUCCESS = "SU0012";
	public static final String NEWS_SCRAPE_SUCCESS = "SU0013";
	
	// 실패
	public static final String VERIFICATION_CODE_SEND_FAIL = "FA0001";
	public static final String VERIFICATION_CODE_VERIFY_FAIL = "FA0002";
	public static final String AUTHENTICATION_FAIL = "FA0003";
	public static final String DIRECTORY_CREATION_FAIL = "FA0004";
	public static final String FILE_STORE_FAIL = "FA0005";
	public static final String FILE_DELETE_FAIL = "FA0006";
	public static final String GEMINI_RESPONSE_EMPTY_FAIL = "FA0007";
	public static final String GEMINI_API_CALL_FAIL = "FA0008";
	public static final String NEWS_ARTICLE_GET_FAIL_FROM_API = "FA0009";
	
	// 데이터 없음
	public static final String MEMBER_NOT_FOUND_WITH_EMAIL = "NF0001";
	public static final String FILE_NOT_FOUND = "NF0002";
	public static final String PROFILE_IMAGE_URL_NOT_FOUND = "NF0003";
	public static final String GEMINI_RESPONSE_NOT_FOUND = "NF0004";
	
	
	// 특정 상황
	public static final String DUPLICATE_EMAIL = "SP0001";
	public static final String EMAIL_CAN_USE = "SP0002";
	public static final String DUPLICATE_NICKNAME = "SP0003";
	public static final String NICKNAME_CAN_USE = "SP0004";
	public static final String VALUE_NOT_MATCH = "SP0005";
	public static final String TOKEN_ERROR = "SP0006";
	public static final String CHECK_JWT_SECERET_KEY = "SP0007";
	public static final String EMAIL_VERIFICATION_REQUIRED = "SP0008";
	public static final String DATA_INTEGRITY_VIOLATION = "SP0009";
	public static final String ACCESS_TOKEN_EXPIRED = "SP0010";

	

	
	
}
