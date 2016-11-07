package org.openape.server;

import org.openape.server.rest.UserContextRESTInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {

    public static void main(String[] args) {
        new UserContextRESTInterface(new UserContextRequestHandler());

    }

}
