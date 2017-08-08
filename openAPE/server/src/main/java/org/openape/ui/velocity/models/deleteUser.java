package org.openape.ui.velocity.models;

import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class deleteUser {
    private AdminSectionRequestHandler adminSectionRequestHandler = new AdminSectionRequestHandler();

    public AdminSectionRequestHandler getAdminSectionRequestHandler() {
        return this.adminSectionRequestHandler;
    }

    public void setAdminSectionRequestHandler(final AdminSectionRequestHandler adminSectionRequestHandler) {
        this.adminSectionRequestHandler = adminSectionRequestHandler;
    }

}
