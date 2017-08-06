package org.openape.web;

import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.rest.SuperRestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.servlet.SparkApplication;

public class Init implements SparkApplication {
    Logger logger = LoggerFactory.getLogger(Init.class);

    @Override
    public void init() {
        this.logger.info("Initialising openAPE");
        this.logger.debug("Working directory: " + System.getProperty("user.dir"));

        // start database connection for testing purposes
        DatabaseConnection.getInstance();

        // Start rest api.
        new SuperRestInterface();
    }

}
