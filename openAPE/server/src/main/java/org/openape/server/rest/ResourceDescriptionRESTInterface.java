package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.server.requestHandler.ResourceDescriptionRequestHandler;

import spark.Spark;

public class ResourceDescriptionRESTInterface extends SuperRestInterface {

    public ResourceDescriptionRESTInterface(final ResourceDescriptionRequestHandler requestHandler) {
        super();

        /**
         * Request 7.7.2 create resource description.
         */
        Spark.post("/api/resource-description", (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a
                        // resource description
                        // object.
                final ResourceDescription recievedResourceDescription = (ResourceDescription) this
                        .extractContentFromRequest(req, ResourceDescription.class);
                // Test the object for validity.
                if (!recievedResourceDescription.isValid()) {
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return "No valid context object";
                }
                // If the object is okay, save it and return the id.
                final String resourceDescriptionId = requestHandler
                        .createResourceDescription(recievedResourceDescription);
                res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                return resourceDescriptionId;
            } catch (JsonParseException | JsonMappingException e) {
                // If the parse is not successful return bad request
                // error code.
                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
        });

        /**
         * Request 7.7.3 get resource description. Used to get a specific
         * resource description identified by ID.
         */
        Spark.get("/api/resource-description/resource-description-id", //$NON-NLS-1$
                (req, res) -> {
                    final String resourceDescriptionId = req.params(":resource-description-id"); //$NON-NLS-1$
                try {
                    // if it is successful return resource description.
                    final ResourceDescription resourceDescription = requestHandler
                            .getResourceDescriptionById(resourceDescriptionId);
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    res.type("application/json");
                    final ObjectMapper mapper = new ObjectMapper();
                    final String jsonData = mapper.writeValueAsString(resourceDescription);
                    return jsonData;
                    // if not return corresponding error status.
                } catch (final IllegalArgumentException e) {
                    res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                    return e.getMessage();
                } catch (final IOException e) {
                    res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                    return e.getMessage();
                }

            });

        /**
         * Request 7.7.4 get resource description. Used to get a specific
         * resource description identified by Listing.
         */
        Spark.get("/api/resource-description?listing-id=listing-id&index=index", (req, res) -> {
            // TODO implement
                return "";
            });

        /**
         * Request 7.7.5 update resource description.
         */
        Spark.put(
                "/api/resource-description/resource-description-id",
                (req, res) -> {
                    final String resourceDescriptionId = req.params(":resource-description-id");
                    try {
                        final ResourceDescription recievedResourceDescription = (ResourceDescription) this
                                .extractContentFromRequest(req, ResourceDescription.class);
                        // Test the object for validity.
                        if (!recievedResourceDescription.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return "No valid context object";
                        }
                        // If the object is okay, update it.
                        requestHandler.updateResourceDescriptionById(resourceDescriptionId,
                                recievedResourceDescription);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return ""; // TODO return right statuscode
                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
                        // If the parse or update is not successful return bad
                        // request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         * Request 7.7.6 delete resource description.
         */
        Spark.delete("/api/resource-description/resource-description-id", (req, res) -> {
            final String resourceDescriptionId = req.params(":resource-description-id");
            try {
                // if it is successful return empty string.
                requestHandler.deleteResourceDescriptionById(resourceDescriptionId);
                res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                return "";
                // if not return corresponding error status.
            } catch (final IllegalArgumentException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
        });

    }

}
