package org.openape.web;

import java.io.File;

import org.openape.server.MongoConfig;
import org.openape.server.admin.AdminInterface;
import org.openape.server.auth.AuthService;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.rest.SuperRestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.servlet.SparkApplication;

public class Init implements SparkApplication {
    Logger logger = LoggerFactory.getLogger(Init.class);

    @Override
    public void init() {
        MongoConfig.BUNDLE_NAME = ".."+File.separator+".."+File.separator+"config"+File.separator+"mongo";
        AuthService.BUNDLE_NAME= ".."+File.separator+".."+File.separator+"config"+File.separator+"auth";
        System.out.println(MongoConfig.BUNDLE_NAME);
        this.logger.info("Initialising openAPE");
        this.logger.debug("Working directory: " + System.getProperty("user.dir"));

        // start database connection for testing purposes
        DatabaseConnection.getInstance();
        
        // create a standard Admin user
        try {
            AdminInterface.createAdmin();
        } catch (final Exception e) {
            this.logger.error("Admin could not be created or no Admin found. System could not start.");
            return;
        }
        
        // Start rest api.
        new SuperRestInterface();
    }

}
