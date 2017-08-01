package org.openape.ui.velocity.models;

import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class deleteUser {
	private AdminSectionRequestHandler adminSectionRequestHandler = new AdminSectionRequestHandler();

	public AdminSectionRequestHandler getAdminSectionRequestHandler() {
		return adminSectionRequestHandler;
	}

	public void setAdminSectionRequestHandler(AdminSectionRequestHandler adminSectionRequestHandler) {
		this.adminSectionRequestHandler = adminSectionRequestHandler;
	}
	 
}
