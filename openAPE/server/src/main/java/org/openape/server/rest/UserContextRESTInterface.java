package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.usercontext.UserContext;
import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;

public class UserContextRESTInterface {
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    public UserContextRESTInterface(
            final UserContextRequestHandler requestHandler) {

        /**
         * test request to test if the server runs. Invoke locally using:
         * http://localhost:8080/hello
         */
        get("/hello", (req, res) -> "Hello World");

        /**
         * Request 7.2.2 create user-context.
         */
        post("/api/user-context", (req, res) -> {
            try {
                // Try to map the received json object to a userContext
                // object.
                ObjectMapper mapper = new ObjectMapper();
                UserContext recievedUserContext = mapper.readValue(req.body(),
                        UserContext.class);
                // Test the object for validity.
                if (!recievedUserContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, save it and return the id.
                String userContextId = requestHandler
                        .createUserContext(recievedUserContext);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return userContextId;
            } catch (JsonParseException jpe) {
                // If the parse is not successful return bad request error code.
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            }
        });

        /**
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        get("/api/user-context/:user-context-id",
                (req, res) -> {
                    String userContextId = req.params(":user-context-id");

                    try {
                        UserContext userContext = requestHandler
                                .getUserContextById(userContextId);
                        res.status(HTTP_STATUS_OK);
                        res.type("application/json");
                        return userContext;
                    } catch (IllegalArgumentException e) {
                        res.status(HTTP_STATUS_BAD_REQUEST);
                        return "";
                    } catch (IOException e) {
                        res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return "";
                    }

                });

        /**
         * Request 7.2.4 update user-context.
         */
        put("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            Object useContext = req.params(":user-context");
            requestHandler.updateUserContextById(userContextId, useContext);
            return null;
        });

        /**
         * Request 7.2.5 delete user-context.
         */
        delete("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            requestHandler.deleteUserContextById(userContextId);
            return null;
        });

    }
}
