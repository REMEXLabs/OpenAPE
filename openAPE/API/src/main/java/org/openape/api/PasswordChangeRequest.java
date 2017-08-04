package org.openape.api;

public class PasswordChangeRequest {
	public PasswordChangeRequest() {

	}

	public PasswordChangeRequest(final String oldPassword, final String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String oldPassword;
	public String newPassword;
}
