package com.readforce.enums;

public enum Prefix {
	
	SIGN_UP("SignUp_"),
	MODIFY_INFO("ModifyInfo_"),
	PASSWORD_RESET_BY_LINK("PasswordResetByLink_"),
	COMPLETE_EMAIL_VERIFY("CompleteEmailVerify_"),
	SOCIAL_SIGN_UP("SocialSignUp_"),
	ROLE("ROLE_"),
	REFRESH_TOKEN("RefreshToken_"),
	BEARER("Bearer "),
	X_REFRESH_TOKEN("X-Refresh-Token "),
	TEMPORAL_TOKEN("TemporalToken "),
	EXIST_MEMBER_SOCIAL_LOGIN("ExistMemberSocialLogin_"),
	EMAIL_VERIFY_ATTEMPT_PREFIX("EmailVerifyAttempt_"),
	RATE_LIMIT_IP("RateLimitIp:"),
	RATE_LIMIT_EMAIL("RateLimitEmail:"),
	SOCIAL_LINK_STATE("SocialLinkState_"),
	CHALLENGE_LIMIT("ChallengeLimit_")
	;

	private final String prefix;
	
	Prefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getName() {
		return prefix;
	}
	
}
