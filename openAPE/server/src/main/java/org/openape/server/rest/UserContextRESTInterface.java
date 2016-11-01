package org.openape.server.rest;

import org.openape.api.usercontext.UserContext;
import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;
import spark.Spark;
import spark.Request;
import spark.Route;
import spark.Response;

public class UserContextRESTInterface {

    public UserContextRESTInterface(
            final UserContextRequestHandler requestHandler) {
        get("/hello", (req, res) -> "Hello World");
        // get("/api/user-context/:user-context-id", (req, res) ->
        // requestHandler.);
    }
}
