package org.openape.server.rest;

import java.io.IOException;

import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.requestHandler.ResourceDescriptionRequestHandler;

import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceDescriptionRESTInterface extends SuperRestInterface {

    public static void setupResourceDescriptionRESTInterface(
            final ResourceDescriptionRequestHandler requestHandler, final AuthService auth) {

        // Authentication: Make sure only registered principals (users and
        // admins) can create a new resource description
        Spark.before(Messages
                .getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithoutID"),
                auth.authorize("user"));
        // Authentication: Everyone can access the route for a specific resource
        // description
        Spark.before(
                Messages.getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithID"),
                auth.authorize("anonymous"));

        /**
         * Request 7.7.2 create resource description.
         */
        Spark.post(
                Messages.getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                        }
                        // Try to map the received json object to a resource
                        // description object.
                        final ResourceDescription receivedResourceDescription = (ResourceDescription) SuperRestInterface
                                .extractObjectFromRequest(req, ResourceDescription.class);
                        // Make sure to set the id of the authenticated user as
                        // the ownerId
                        receivedResourceDescription.setOwner(auth.getAuthenticatedUser(req, res)
                                .getId());
                        // Test the object for validity.
                        if (!receivedResourceDescription.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("ResourceDescriptionRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String resourceDescriptionId = requestHandler
                                .createResourceDescription(receivedResourceDescription);
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
                    } catch (final UnauthorizedException e) {
                        // Only authorized users may post resource descriptions
                        res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                        return e.getMessage();
                    }
                });

        /**
         * Request 7.7.3 get resource description. Used to get a specific
         * resource description identified by ID.
         */
        Spark.get(Messages
                .getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String resourceDescriptionId = req.params(":resource-description-id"); //$NON-NLS-1$
                try {
                    // if it is successful return resource description.
                    final ResourceDescription resourceDescription = requestHandler
                            .getResourceDescriptionById(resourceDescriptionId);
                    // Make sure only admins or the owner can view the resource
                    // description, except if it is public
                    auth.allowAdminOwnerAndPublic(req, res, resourceDescription.getOwner(),
                            resourceDescription.isPublic());
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
                } catch (final UnauthorizedException e) {
                    res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
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
                        final Listing listing = requestHandler.getListingById(listingId);
                        // get resource description from listing. If no listing
                        // is found an exception will be thrown.
                        final ResourceDescription resourceDescription = listing
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
        Spark.put(Messages
                .getString("ResourceDescriptionRESTInterface.ResourceDescriptionURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                }
                final String resourceDescriptionId = req.params(Messages
                        .getString("ResourceDescriptionRESTInterface.IDParam")); //$NON-NLS-1$
                try {
                    final ResourceDescription receivedResourceDescription = (ResourceDescription) SuperRestInterface
                            .extractObjectFromRequest(req, ResourceDescription.class);
                    // Test the object for validity.
                    if (!receivedResourceDescription.isValid()) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages
                                .getString("ResourceDescriptionRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                    }
                    // Check if the resource description does exist
                    final ResourceDescription resourceDescription = requestHandler
                            .getResourceDescriptionById(resourceDescriptionId);
                    // Make sure only admins and the owner can update a context
                    auth.allowAdminAndOwner(req, res, resourceDescription.getOwner());
                    receivedResourceDescription.setOwner(resourceDescription.getOwner()); // Make
                                                                                          // sure
                                                                                          // the
                                                                                          // owner
                                                                                          // can't
                                                                                          // be
                                                                                          // changed
                    // Perform the update
                    requestHandler.updateResourceDescriptionById(resourceDescriptionId,
                            receivedResourceDescription);
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
                } catch (final UnauthorizedException e) {
                    // Only authorized users may edit resource descriptions
                    res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
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
                        // Check if the resource description does exist
                        final ResourceDescription resourceDescription = requestHandler
                                .getResourceDescriptionById(resourceDescriptionId);
                        // Make sure only admins and the owner can delete a
                        // context
                        auth.allowAdminAndOwner(req, res, resourceDescription.getOwner());
                        // Perform delete and return empty string.
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
                    } catch (final UnauthorizedException e) {
                        // Only authorized users may delete resource
                        // descriptions
                        res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                        return e.getMessage();
                    }
                });

    }

}
