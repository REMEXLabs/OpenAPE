package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.usercontext.UserContext;
import org.openape.server.UserContextRequestHandler;

import static spark.Spark.*;

public class UserContextRESTInterface extends SuperRestInterface {

    public UserContextRESTInterface(final UserContextRequestHandler requestHandler) {

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
                UserContext recievedUserContext = (UserContext) extractContentFromRequest(req, UserContext.class);
                // Test the object for validity.
                if (!recievedUserContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, save it and return the id.
                String userContextId = requestHandler.createUserContext(recievedUserContext);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return userContextId;
            } catch (JsonParseException | JsonMappingException e) {
                // If the parse is not successful return bad request error code.
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

        /**
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        get("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            try {
                // if it is successful return user context.
                UserContext userContext = requestHandler.getUserContextById(userContextId);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return userContext;
                // if not return corresponding error status.
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
            try {
                UserContext recievedUserContext = (UserContext) extractContentFromRequest(req, UserContext.class);
                // Test the object for validity.
                if (!recievedUserContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, update it.
                requestHandler.updateUserContextById(userContextId, recievedUserContext);
                res.status(HTTP_STATUS_OK);
                return "";
            } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
                // If the parse or update is not successful return bad request
                // error code.
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

        /**
         * Request 7.2.5 delete user-context.
         */
        delete("/api/user-context/:user-context-id", (req, res) -> {
            String userContextId = req.params(":user-context-id");
            try {
                // if it is successful return user context.
                requestHandler.deleteUserContextById(userContextId);
                res.status(HTTP_STATUS_OK);
                return "";
                // if not return corresponding error status.
            } catch (IllegalArgumentException e) {
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

    }

}
