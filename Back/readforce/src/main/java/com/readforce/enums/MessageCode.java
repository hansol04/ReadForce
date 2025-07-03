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
	public static final String TEMPORAL_TOKEN_NOT_BLANK = "NO0009";
	public static final String NEWS_ARTICLE_LANGUAGE_NOT_BLANK = "NO0010";
	public static final String NEWS_ARTICLE_LEVEL_NOT_BLANK = "NO0011";
	public static final String NEWS_NO_NOT_NULL = "NO0012";
	public static final String NEWS_ARTICLE_ORDER_BY_NOT_BLANK = "NO0013";
	public static final String NEWS_ARTICLE_CATEGORY_NOT_BLANK = "NO0014";
	public static final String NEWS_QUIZ_CONTENT_NOT_BLANK = "NO0015";
	public static final String NEWS_QUIZ_LANGUAGE_NOT_BLANK = "NO0016";
	public static final String NEWS_QUIZ_LEVEL_NOT_BLANK = "NO0017";
	public static final String NEWS_ARTICLE_NOT_NULL = "NO0018";
	public static final String LITERATURE_TYPE_NOT_BLANK = "NO0019";
	public static final String LITERATURE_LEVEL_NOT_BLANK = "NO0020";
	public static final String LITERATURE_CATEGORY_NOT_BLANK = "NO0021";
	public static final String LITERATURE_PARAGRAPH_NO_NOT_NULL = "NO0022";
	public static final String LITERATURE_NO_NOT_NULL = "NO0023";
	public static final String NEWS_QUIZ_NO_NOT_NULL = "NO0024";
	public static final String SELECTED_OPTION_INDEX_NOT_NULL = "NO0025";
	public static final String LITERATURE_QUIZ_NO_NOT_NULL = "NO0026";
	public static final String CLASSIFICATION_NOT_BLANK = "NO0027";
	public static final String TYPE_AND_LANGUAGE_NOT_BLANK = "NO0028";
	public static final String LITERATURE_TITLE_NOT_BLANK = "NO0029";
	public static final String LITERATURE_PARAGRAPH_CONTENT_NOT_BLANK = "NO0030";
	public static final String LITERATURE_PARAGRAPH_CATEGORY_NOT_BLANK = "NO0031";
	public static final String LEVEL_NOT_BLANK = "NO0032";
	public static final String ATTENDANCE_NO_NOT_NULL = "NO0033";
	public static final String POINT_NOT_NULL = "NO0034";
	

	
	// 입력값 형식 불일치
	public static final String EMAIL_PATTERN_INVALID = "PA0001";
	public static final String PASSWORD_PATTERN_INVALID = "PA0002";
	public static final String NICKNAME_PATTERN_INVALID = "PA0003";
	public static final String FILE_TYPE_INVALID = "PA0004";
	public static final String NEWS_ARTICLE_LANGUAGE_PATTERN_INVALID = "PA0005";
	public static final String NEWS_ARTICLE_LEVEL_PATTERN_INVALID = "PA0006";
	public static final String NEWS_ARTICLE_ORDER_BY_INVALID = "PA0007";
	public static final String VALUE_INVALID = "PA0008";
	public static final String NEWS_ARTICLE_CATEGORY_INVALID = "PA0008";
	public static final String GEMINI_RESPONSE_PATTERN_INVALID = "PA0009";
	public static final String LITERATURE_TYPE_PATTERN_INVALID = "PA0010";
	public static final String LEVEL_PATTERN_INVALID = "PA0011";
	public static final String LITERATURE_CATEGORY_PATTERN_INVALID = "PA0012";
	public static final String CLASSIFICATION_PATTERN_INVALID = "PA0013";
	public static final String LITERATURE_PARAGRAPH_CATEGORY_INVALID = "PA0014";

	
	
	
	// 입력값 크기/범위 불일치
	public static final String PASSWORD_SIZE_INVALID = "SI0001";
	public static final String NICKNAME_SIZE_INVALID = "SI0002";
	public static final String BIRTHDAY_RANGE_INVALID = "SI0003";
	public static final String FILE_SIZE_INVALID = "SI0004";
		
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
	public static final String REISSUE_ACCESS_TOKEN_SUCCESS = "SU0011"; // 엑세스 토큰 재발급 성공
	public static final String SIGN_OUT_SUCCESS = "SU0012";
	public static final String GET_TOKENS_SUCCESS = "SU0013";
	public static final String MEMBER_DEACTIVATE_SUCCESS = "SU0014";
	public static final String MEMBER_ACTIVATE_SUCCESS = "SU0015";
	public static final String GENERATE_CREATIVE_NEWS_SUCCESS = "SU0016";
	public static final String GENERATE_CREATIVE_NEWS_QUIZ_SUCCESS = "SU0017";
	public static final String SAVE_MEMBER_SOLVED_NEWS_QUIZ = "SU0018";
	public static final String SAVE_MEMBER_SOLVED_LITERATURE_QUIZ = "SU0019";
	public static final String GENERATE_CREATIVE_LITERATURE_QUIZ_SUCCESS = "SU0020";
	public static final String DELETE_NEWS_AND_NEWS_QUIZ_SUCCESS = "SU0021";
	public static final String DELETE_NEWS_QUIZ_SUCCESS = "SU0022";
	public static final String ADD_LITERATURE_SUCCESS = "SU0023";
	public static final String DELETE_LITERATURE_AND_LITERATURE_PARAGRAPH_AND_LITERATURE_QUIZ_SUCCESS = "SU0024";
	public static final String ADD_LITERATURE_PARAGRAPH_SUCCESS = "SU0025";
	public static final String DELETE_LITERATURE_PARAGRAPH_AND_LITERATURE_QUIZ_SUCCESS = "SU0025";
	public static final String DELETE_LITERATURE_QUIZ_SUCCESS = "SU0026";
	public static final String UPDATE_POINT_SUCCESS = "SU0027";
	public static final String DELETE_NEWS_QUIZ_ATTEMPT_SUCCESS = "SU0028";
	public static final String DELETE_LITERATURE_QUIZ_ATTEMPT_SUCCESS = "SU0029";
	public static final String ADD_NEWS_QUIZ_ATTEMPT_SUCCESS = "SU0030";
	public static final String ADD_LITERATURE_QUIZ_ATTEMPT_SUCCESS = "SU0031";
	public static final String ADD_ATTENDANCE_SUCCESS = "SU0032";
	public static final String DELETE_MEMBER_SUCCESS = "SU0033";
	public static final String DELETE_ATTENDANCE_SUCCESS = "SU0034";
	public static final String ADD_POINT_SUCCESS = "SU0035";
	public static final String DELETE_POINT_SUCCESS = "SU0036";
	public static final String CHALLENGE_POINT_UPDATE_SUCCESS = "SU0037";
	

	// 실패
	public static final String VERIFICATION_CODE_SEND_FAIL = "FA0001";
	public static final String VERIFICATION_CODE_VERIFY_FAIL = "FA0002";
	public static final String AUTHENTICATION_FAIL = "FA0003";
	public static final String DIRECTORY_CREATION_FAIL = "FA0004";
	public static final String FILE_STORE_FAIL = "FA0005";
	public static final String FILE_DELETE_FAIL = "FA0006";
	public static final String TEMPORAL_TOKEN_AUTHENTICATION_FAIL = "FA0007";
	public static final String JSON_MAPPING_FAIL = "FA0008";
	public static final String JSON_PROCESSING_FAIL = "FA0009";
	public static final String GEMINI_REQUEST_FAIL = "FA0010";
	public static final String GET_CHALLENGE_QUIZ_FAIL = "FA0011";
	public static final String GET_RANKING_LIST_FAIL = "FA0012";
	
	
	
	// 데이터 없음
	public static final String MEMBER_NOT_FOUND_WITH_EMAIL = "NF0001";
	public static final String FILE_NOT_FOUND = "NF0002";
	public static final String PROFILE_IMAGE_URL_NOT_FOUND = "NF0003";
	public static final String NEWS_NOT_FOUND = "NF0004";
	public static final String NEWS_QUIZ_NOT_FOUND = "NF0005";
	public static final String ATTENDANCE_DATE_NOT_FOUND = "NF0006";
	public static final String MEMBER_NOT_FOUND = "NF0007";
	public static final String WITHDRAW_NOT_FOUND = "NF0008";
	public static final String BEGINNER_NEWS_NOT_FOUND = "NF0009";
	public static final String INTERMEDIATE_NEWS_NOT_FOUND = "NF0010";
	public static final String ADVANCED_NEWS_NOT_FOUND = "NF0011";
	public static final String BEGINNER_NEWS_QUIZ_NOT_FOUND = "NF0012";
	public static final String INTERMEDIATE_NEWS_QUIZ_NOT_FOUND = "NF0013";
	public static final String ADVANCED_NEWS_QUIZ_NOT_FOUND = "NF0014";
	public static final String LITERATURE_NOT_FOUND = "NF0015";
	public static final String NEWS_QUIZ_ATTEMPT_NOT_FOUND = "NF0016";
	public static final String LITERATURE_QUIZ_ATTEMPT_NOT_FOUND = "NF0017";
	public static final String LITERATURE_QUIZ_NOT_FOUND = "NF0018";
	public static final String LITERATURE_PARAGRAPH_NOT_FOUND = "NF0019";
	public static final String POINT_NOT_FOUND = "NF0020";	
	
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
	public static final String SOCIAL_EMAIL_ALREADY_CONNECTED_WITH_OTHER_MEMBER = "SP0011";
	public static final String SOCIAL_EMAIL_ALREADY_USE_BY_OTHER_MEMBER = "SP0012";
	public static final String IP_ADDRESS_REQUEST_LIMIT_EXCEEDED = "SP0013";
	public static final String EMAIL_REQUEST_LIMIT_EXCEEDED = "SP0014";
	public static final String NEWS_QUIZ_LINE_MISSING = "SP0015";
	public static final String NEWS_QUIZ_OPTION_MISSING = "SP0016";
	public static final String NEWS_QUIZ_ANSWER_MISSING = "SP0017";
	public static final String ONLY_ONE_BETWEEN_TYPE_OR_LANGUAGE = "SP0018";
	public static final String LACK_OF_BEGINNER_LITERATURE_QUIZ = "SP0019";
	public static final String LACK_OF_INTERMEDIATE_LITERATURE_QUIZ = "SP0020";
	public static final String LACK_OF_ADVANCED_LITERATURE_QUIZ = "SP0021";
	public static final String LACK_OF_BEGINNER_NEWS_QUIZ = "SP0022";
	public static final String LACK_OF_INTERMEDIATE_NEWS_QUIZ = "SP0023";
	public static final String LACK_OF_ADVANCED_NEWS_QUIZ = "SP0024";
	public static final String TOTAL_POINT_MUST_BE_POSITIVE_NUMBER = "SP0025";
	public static final String KOREAN_NEWS_POINT_MUST_BE_POSITIVE_NUMBER = "SP0026";
	public static final String ENGILISH_NEWS_POINT_MUST_BE_POSITIVE_NUMBER = "SP0027";
	public static final String JAPANESE_NEWS_POINT_MUST_BE_POSITIVE_NUMBER = "SP0028";
	public static final String NOVEL_POINT_MUST_BE_POSITIVE_NUMBER = "SP0029";
	public static final String FAIRYTALE_POINT_MUST_BE_POSITIVE_NUMBER = "SP0030";
	public static final String LITERATURE_QUIZ_LINE_MISSING = "SP0031";
	public static final String LITERATURE_QUIZ_OPTION_MISSING = "SP0032";
	public static final String LITERATURE_QUIZ_ANSWER_MISSING = "SP0033";
	public static final String KOREAN_NEWS_CHALLENGE_ALREADY_TAKEN_TODAY = "SP0034";
	public static final String JAPANESE_NEWS_CHALLENGE_ALREADY_TAKEN_TODAY = "SP0035";
	public static final String ENGLISH_NEWS_CHALLENGE_ALREADY_TAKEN_TODAY = "SP0036";
	public static final String NOVEL_CHALLENGE_ALREADY_TAKEN_TODAY = "SP0037";
	public static final String FAIRYTALE_CHALLENGE_ALREADY_TAKEN_TODAY = "SP0038";
	public static final String ATTENDANCE_DATE_DUPLICATE = "SP0039";
	
}
