package org.openape.api;

public class PasswordChangeRequest {
	public String oldPassword;

	public String newPassword;

	public PasswordChangeRequest() {

	}

	public PasswordChangeRequest(final String oldPassword, final String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
}
