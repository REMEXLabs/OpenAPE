package org.openape.server;

import java.io.IOException;

import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.resources.ResourceList;
import org.openape.server.rest.SuperRestInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
    public static void main(String[] args) {
        // Start rest api and database connection.
        DatabaseConnection.getInstance();
        try {
            ResourceList.getInstance();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new SuperRestInterface();
    }

}
