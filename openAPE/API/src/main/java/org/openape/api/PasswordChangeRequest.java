package org.openape.api;

public class PasswordChangeRequest {
    private String oldPassword;

    private String newPassword;

    public PasswordChangeRequest() {

    }

    public PasswordChangeRequest(final String oldPassword, final String newPassword) {
        this.setOldPassword(oldPassword);
        this.setNewPassword(newPassword);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
