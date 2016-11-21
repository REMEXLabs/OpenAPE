package org.openape.server.rest;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.server.EnvironmentContextRequestHandler;

public class EnvironmentContextRESTInterface extends SuperRestInterface {

    public EnvironmentContextRESTInterface(final EnvironmentContextRequestHandler requestHandler) {
        super();

        /**
         * Request 7.5.2 create environment-context.
         */
        post("/api/environment-context", (req, res) -> {
            try {
                // Try to map the received json object to a environmentContext
                // object.
                EnvironmentContext recievedEnvironmentContext = (EnvironmentContext) this.extractContentFromRequest(req,
                        EnvironmentContext.class);
                // Test the object for validity.
                if (!recievedEnvironmentContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, save it and return the id.
                String environmentContextId = requestHandler.createEnvironmentContext(recievedEnvironmentContext);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return environmentContextId;
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
         * Request 7.5.3 get environment-context. Used to get a specific
         * environment context identified by ID.
         */
        get("/api/environment-context/:environment-context-id", (req, res) -> {
            String environmentContextId = req.params(":environment-context-id");
            try {
                // if it is successful return environment context.
                EnvironmentContext environmentContext = requestHandler.getEnvironmentContextById(environmentContextId);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return environmentContext;
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
         * Request 7.5.4 update environment-context.
         */
        put("/api/environment-context/:environment-context-id", (req, res) -> {
            String environmentContextId = req.params(":environment-context-id");
            try {
                EnvironmentContext recievedEnvironmentContext = (EnvironmentContext) this.extractContentFromRequest(req,
                        EnvironmentContext.class);
                // Test the object for validity.
                if (!recievedEnvironmentContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, update it.
                requestHandler.updateEnvironmentContextById(environmentContextId, recievedEnvironmentContext);
                res.status(HTTP_STATUS_OK);
                return "";
            } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
                // If the parse or update is not successful return bad
                // request
                // error code.
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

        /**
         * Request 7.5.5 delete environment-context.
         */
        delete("/api/environment-context/:environment-context-id", (req, res) -> {
            String environmentContextId = req.params(":environment-context-id");
            try {
                // if it is successful return environment context.
                requestHandler.deleteEnvironmentContextById(environmentContextId);
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
