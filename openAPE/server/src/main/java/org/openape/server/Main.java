package org.openape.server;

import static spark.Spark.port;

import org.openape.server.rest.UserContextRESTInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {

    public static void main(String[] args) {
        // Change port to default port.
        port(8080);
        // Start user context rest api.
        new UserContextRESTInterface(new UserContextRequestHandler());

    }

}
