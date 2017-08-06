package org.openape.server.admin;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.user.User;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.ProfileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminInterface {

    static Logger logger = LoggerFactory.getLogger(AdminInterface.class);
    private static String ADMIN_NAME = "admin";
    private static String ADMIN_PASSWORD = "admin";
    private static final List<String> ADMIN_ROLES = new LinkedList<>();

    public static void setupAdminRestInterface(final AuthService authService) {

    }

    public static void createAdmin() throws IllegalArgumentException, IOException {
        if (AdminInterface.adminExists()) {
            AdminInterface.logger.info("Found Admin in database");
            return;
        }

        AdminInterface.logger.info("No Admin found in database");
        final User admin = new User();
        admin.setUsername(AdminInterface.ADMIN_NAME);
        admin.setPassword(AdminInterface.ADMIN_PASSWORD);
        admin.setEmail("admin@admin.de");
        AdminInterface.ADMIN_ROLES.add("admin");
        AdminInterface.ADMIN_ROLES.add("user");

        admin.setRoles(AdminInterface.ADMIN_ROLES);
        ProfileHandler.createUser(admin);
    }

    private static boolean adminExists() throws IOException {

        final User response = ProfileHandler.getUser(AdminInterface.ADMIN_NAME);
        if (response == null) {
            return false;
        }
        return true;
    }

}
