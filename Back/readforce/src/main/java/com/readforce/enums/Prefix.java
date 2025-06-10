package com.readforce.enums;

public enum Prefix {
	
	SIGN_UP("SignUp_"),
	MODIFY_INFO("ModifyInfo_"),
	PASSWORD_RESET_BY_LINK("PasswordResetByLink_"),
	COMPLETE_EMAIL_VERIFY("CompleteEmailVerify_");

	private final String prefix;
	
	Prefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getName() {
		return prefix;
	}
	
}
