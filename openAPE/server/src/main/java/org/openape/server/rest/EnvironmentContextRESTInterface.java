package org.openape.server.rest;

import java.io.IOException;

import org.openape.api.Messages;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;

import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnvironmentContextRESTInterface extends SuperRestInterface {

    public static void setupEnvironmentContextRESTInterface(
            final EnvironmentContextRequestHandler requestHandler, final AuthService auth) {

        // Authentication: Make sure only registered principals (users and
        // admins) can create a new context
        Spark.before(Messages
                .getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithoutID"), auth
                .authorize("user"));
        // Authentication: Everyone can access the route for a specific context
        Spark.before(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"),
                auth.authorize("anonymous"));

        /**
         * Request 7.5.2 create environment-context.
         */
        Spark.post(Messages
                .getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithoutID"), //$NON-NLS-1$
                (req, res) -> {
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                }
                try {
                    // Try to map the received json object to an
                    // EnvironmentContext object.
                    final EnvironmentContext receivedEnvironmentContext = (EnvironmentContext) SuperRestInterface
                            .extractObjectFromRequest(req, EnvironmentContext.class);
                    // Make sure to set the id of the authenticated user as
                    // the ownerId
                    receivedEnvironmentContext
                            .setOwner(auth.getAuthenticatedUser(req, res).getId());
                    // Test the object for validity.
                    if (!receivedEnvironmentContext.isValid()) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages
                                .getString("EnvironmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                    }
                    // If the object is okay, save it and return the id.
                    final String environmentContextId = requestHandler
                            .createEnvironmentContext(receivedEnvironmentContext);
                    res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                    return environmentContextId;
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
         * Request 7.5.3 get environment-context. Used to get a specific
         * environment context identified by ID.
         */
        Spark.get(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String environmentContextId = req.params(Messages
                            .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return environment context.
                        final EnvironmentContext environmentContext = requestHandler
                                .getEnvironmentContextById(environmentContextId);
                        // Make sure only admins or the owner can view the
                        // context, except if it is public
                        auth.allowAdminOwnerAndPublic(req, res, environmentContext.getOwner(),
                                environmentContext.isPublic());
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("EnvironmentContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        final ObjectMapper mapper = new ObjectMapper();
                        final String jsonData = mapper.writeValueAsString(environmentContext);
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
         * Request 7.5.4 update environment-context.
         */
        Spark.put(Messages
                .getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                }
                final String environmentContextId = req.params(Messages
                        .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                try {
                    final EnvironmentContext receivedEnvironmentContext = (EnvironmentContext) SuperRestInterface
                            .extractObjectFromRequest(req, EnvironmentContext.class);
                    // Test the object for validity.
                    if (!receivedEnvironmentContext.isValid()) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages
                                .getString("EnvironmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                    }
                    // Check if the user context does exist
                    final EnvironmentContext environmentContext = requestHandler
                            .getEnvironmentContextById(environmentContextId);
                    // Make sure only admins and the owner can update a context
                    auth.allowAdminAndOwner(req, res, environmentContext.getOwner());
                    receivedEnvironmentContext.setOwner(environmentContext.getOwner()); // Make
                                                                                        // sure
                                                                                        // the
                                                                                        // owner
                                                                                        // can't
                                                                                        // be
                                                                                        // changed
                    // Perform update
                    requestHandler.updateEnvironmentContextById(environmentContextId,
                            receivedEnvironmentContext);
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$ //TODO
                                                                                              // $NON-NLS-1$
                                                                                              // $NON-NLS-1$
                                                                                              //$NON-NLS-1$ return
                                                                                              // $NON-NLS-1$
                                                                                              // $NON-NLS-1$
                                                                                              //$NON-NLS-1$ right
                                                                                              // $NON-NLS-1$
                                                                                              // $NON-NLS-1$
                                                                                              //$NON-NLS-1$ statuscode
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
         * Request 7.5.5 delete environment-context.
         */
        Spark.delete(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String environmentContextId = req.params(Messages
                            .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // Check if the user context does exist
                        final EnvironmentContext environmentContext = requestHandler
                                .getEnvironmentContextById(environmentContextId);
                        // Make sure only admins and the owner can delete a
                        // context
                        auth.allowAdminAndOwner(req, res, environmentContext.getOwner());
                        // Perform delete and return empty string.
                        requestHandler.deleteEnvironmentContextById(environmentContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
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
