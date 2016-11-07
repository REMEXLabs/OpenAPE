package org.openape.server.rest;

import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;

public class UserContextRESTInterface {

    public UserContextRESTInterface(final UserContextRequestHandler requestHandler) {
        
        // Change port to default port.
        port(8080);
        
        /**
         * test request to test if the server runs. Invoke locally using:
         * http://localhost:8080/hello
         */
        get("/hello", (req, res) -> "Hello World");

        /**
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        get("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            res.type("application/json");
            return requestHandler.getUserContextById(userContextId);
        });

    }
}
