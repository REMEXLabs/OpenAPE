package org.openape.server;

import java.io.IOException;
import org.openape.server.database.mongoDB.DatabaseConnection;

import org.openape.server.rest.SuperRestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
	 static Logger logger = LoggerFactory.getLogger(Main.class	);

    public static void main(String[] args) {
    	logger.info("Starting openAPE application");
    	logger.debug("Working directory: " +             System.getProperty("user.dir"));

        if(Arrays.asList(args).contains("ensureIndexes")) {
            // Open database connection and make sure all indexes exist
            logger.info("Checking for indexes...");
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            databaseConnection.ensureIndexes();
            
        }
        // Start the REST API.
        new SuperRestInterface();
    }

}
