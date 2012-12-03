package com.cts.ms.higheredu.imagecaptcha.util;

public class CaptchaSignUpResponse {
	private boolean companyError;

	public boolean getCompanyError() {
		return companyError;
	}

	public void setCompanyError(boolean companyError) {
		this.companyError = companyError;
	}

	private boolean captchaError;

	public boolean getCaptchaError() {
		return captchaError;
	}

	public void setCaptchaError(boolean captchaError) {
		this.captchaError = captchaError;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean getEmailIdError() {
		return emailIdError;
	}

	public void setEmailIdError(boolean emailIdError) {
		this.emailIdError = emailIdError;
	}

	public boolean getPasswordError() {
		return passwordError;
	}

	public void setPasswordError(boolean passwordError) {
		this.passwordError = passwordError;
	}

	public boolean getUsernameError() {
		return usernameError;
	}

	public void setUsernameError(boolean usernameError) {
		this.usernameError = usernameError;
	}

	private String username;
	private String companyName;
	private String emailId;
	private boolean emailIdError;
	private boolean passwordError;
	private boolean usernameError;

}
