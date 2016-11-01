package org.openape.server.rest;

import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;

public class UserContextRESTInterface {

    public UserContextRESTInterface(
            final UserContextRequestHandler requestHandler) {
        get("/hello", (req, res) -> "Hello World");// test, to be removed

        /**
         * 
         */
        get("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            return requestHandler.getUserContextById(userContextId);
        });

    }
}
