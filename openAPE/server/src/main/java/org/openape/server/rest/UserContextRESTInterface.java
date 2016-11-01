package org.openape.server.rest;

import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;

public class UserContextRESTInterface {

    public UserContextRESTInterface(
            final UserContextRequestHandler requestHandler) {
        get("/hello", (req, res) -> "Hello World");// test, to be removed

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
