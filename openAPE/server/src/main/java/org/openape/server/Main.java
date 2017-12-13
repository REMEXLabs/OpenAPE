package org.openape.server;

import java.util.Arrays;

import org.openape.server.admin.AdminInterface;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.rest.SuperRestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        Main.logger.info("Starting openAPE application");
        Main.logger.debug("Working directory: " + System.getProperty("user.dir"));

        if (Arrays.asList(args).contains("ensureIndexes")) {
            // Open database connection and make sure all indexes exist
            Main.logger.info("Checking for indexes...");
            final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            databaseConnection.ensureIndexes();
        }

        // create a standard Admin user
        try {
            AdminInterface.createAdmin();
        } catch (final Exception e) {
            Main.logger.error("Admin could not be created or no Admin found. System could not start.");
            return;
        }

        // Start the REST API.
        new SuperRestInterface();
    }

}
