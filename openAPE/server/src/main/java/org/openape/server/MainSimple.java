package org.openape.server;

import org.openape.server.rest.ResourceManagerRESTInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class MainSimple {

    public static void main(String[] args) {
        // Start rest api
        // new UserContextRESTInterface(new UserContextRequestHandler());
        // new EnvironmentContextRESTInterface(new
        // EnvironmentContextRequestHandler());
        // new EquipmentContextRESTInterface(new
        // EquipmentContextRequestHandler());
        // new TaskContextRESTInterface(new TaskContextRequestHandler());
        new ResourceManagerRESTInterface();

    }

}
