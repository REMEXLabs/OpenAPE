package org.openape.server;

import org.openape.server.database.DatabaseConnection;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.openape.server.rest.EnvironmentContextRESTInterface;
import org.openape.server.rest.EquipmentContextRESTInterface;
import org.openape.server.rest.TaskContextRESTInterface;
import org.openape.server.rest.UserContextRESTInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class MainTest {

//    public static void main(String[] args) {
//        // Start rest api and database connection.
//        DatabaseConnection.getInstance();
//        new UserContextRESTInterface(new UserContextRequestHandler());
//        new EnvironmentContextRESTInterface(new EnvironmentContextRequestHandler());
//        new EquipmentContextRESTInterface(new EquipmentContextRequestHandler());
//        new TaskContextRESTInterface(new TaskContextRequestHandler());
//
//    }

}
