package org.openape.server;

import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.rest.SuperRestInterface;

import java.util.Arrays;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("ensureIndexes")) {
            // Open database connection and make sure all indexes exist
            System.out.println("Checking for indexes...");
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            databaseConnection.ensureIndexes();
        }
        // Start the REST API.
        new SuperRestInterface();
    }

}
