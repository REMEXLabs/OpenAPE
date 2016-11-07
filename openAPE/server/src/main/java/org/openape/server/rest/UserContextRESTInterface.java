package org.openape.server.rest;

import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;

public class UserContextRESTInterface {

    public UserContextRESTInterface(final UserContextRequestHandler requestHandler) {
        
        /**
         * test request to test if the server runs. Invoke locally using:
         * http://localhost:4567/hello
         */
        get("/hello", (req, res) -> "Hello World");

        /**
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        get("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            return requestHandler.getUserContextById(userContextId);
        });

    }
}
