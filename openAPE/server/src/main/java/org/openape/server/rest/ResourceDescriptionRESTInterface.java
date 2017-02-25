package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.server.requestHandler.ResourceDescriptionRequestHandler;

import spark.Spark;

public class ResourceDescriptionRESTInterface extends SuperRestInterface {

    public static void setupResourceDescriptionRESTInterface(
            final ResourceDescriptionRequestHandler requestHandler) {
        /**
         * Request 7.7.2 create resource description.
         */
        Spark.post("/api/resource-description", (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a
                        // resource description
                        // object.
                final ResourceDescription recievedResourceDescription = (ResourceDescription) SuperRestInterface
                        .extractObjectFromRequest(req, ResourceDescription.class);
                // Test the object for validity.
                if (!recievedResourceDescription.isValid()) {
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages
                            .getString("ResourceDescriptionRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
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
                    res.type(Messages.getString("ResourceDescriptionRESTInterface.jsonMimeType")); //$NON-NLS-1$
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
        Spark.get(
                Messages.getString("ResourceDescriptionRESTInterface.ResourceDescriptionFromListingURL"), (req, res) -> { //$NON-NLS-1$

                    // Currently not used parameter.
                    // final String index = req.params(":index");
                    final String listingId = req.params(Messages
                            .getString("ResourceDescriptionRESTInterface.listingIDParam")); //$NON-NLS-1$

                    try {
                        // get listing from id.
                        Listing listing = requestHandler.getListingById(listingId);
                        // get resource description from listing. If no listing
                        // is found an exception will be thrown.
                        ResourceDescription resourceDescription = listing
                                .getResourceDescriptionQurey();
                        // json map the resource description, set return status
                        // and mime type and return the resource description.
                        final ObjectMapper mapper = new ObjectMapper();
                        final String jsonData = mapper.writeValueAsString(resourceDescription);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages
                                .getString("ResourceDescriptionRESTInterface.jsonMimeType"));//$NON-NLS-1$
                        return jsonData;
                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         * Request 7.7.5 update resource description.
         */
        Spark.put(
                Messages.getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String resourceDescriptionId = req.params(Messages
                            .getString("ResourceDescriptionRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        final ResourceDescription recievedResourceDescription = (ResourceDescription) SuperRestInterface
                                .extractObjectFromRequest(req, ResourceDescription.class);
                        // Test the object for validity.
                        if (!recievedResourceDescription.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("ResourceDescriptionRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, update it.
                        requestHandler.updateResourceDescriptionById(resourceDescriptionId,
                                recievedResourceDescription);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("ResourceDescriptionRESTInterface.EmptyString"); // TODO return right statuscode //$NON-NLS-1$
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
        Spark.delete(
                Messages.getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String resourceDescriptionId = req.params(Messages
                            .getString("ResourceDescriptionRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return empty string.
                        requestHandler.deleteResourceDescriptionById(resourceDescriptionId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("ResourceDescriptionRESTInterface.EmptyString"); //$NON-NLS-1$
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
