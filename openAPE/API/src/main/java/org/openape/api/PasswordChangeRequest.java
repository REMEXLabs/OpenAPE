package org.openape.api;


public class PasswordChangeRequest {
	public PasswordChangeRequest(){
		
	}
	
public PasswordChangeRequest(String oldPassword, String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
public String oldPassword;
public  String newPassword;
}
