package org.openape.server;

import org.openape.server.rest.SuperRestInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
    public static void main(String[] args) {
        // Start rest api.
        new SuperRestInterface();
    }

}
