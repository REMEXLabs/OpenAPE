package org.openape.server.rest;

import java.io.IOException;

import org.openape.api.Messages;
import org.openape.api.usercontext.UserContext;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.UserContextRequestHandler;

import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserContextRESTInterface extends SuperRestInterface {

    public static void setupUserContextRESTInterface(final UserContextRequestHandler requestHandler, final AuthService auth) {
        /**
         * Request 7.2.2 create user-context.
         */
        Spark.before(Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"), auth.protectWithRole("user"));
        Spark.post(
                Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                    }
                    try {
                        // Try to map the received json object to a userContext
                        // object.
                        final UserContext receivedUserContext = (UserContext) SuperRestInterface.extractObjectFromRequest(req, UserContext.class);
                        // Test the object for validity.
                        if (!receivedUserContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages.getString("UserContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String userContextId = requestHandler.createUserContext(receivedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        return userContextId;
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
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        Spark.get(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String userContextId = req.params(Messages.getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return user context.
                        final UserContext userContext = requestHandler.getUserContextById(userContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("UserContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        final ObjectMapper mapper = new ObjectMapper();
                        final String jsonData = mapper.writeValueAsString(userContext);
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
         * Request 7.2.4 update user-context.
         */
        Spark.put(Messages.getString("UserContextRESTInterface.UserContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                }
                final String userContextId = req.params(Messages
                        .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                try {
                    final UserContext recievedUserContext = (UserContext) SuperRestInterface
                            .extractObjectFromRequest(req, UserContext.class);
                    // Test the object for validity.
                    if (!recievedUserContext.isValid()) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages
                                .getString("UserContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                    }
                    // If the object is okay, update it.
                    requestHandler.updateUserContextById(userContextId, recievedUserContext);
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$ //TODO return right statuscode
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
         * Request 7.2.5 delete user-context.
         */
        Spark.delete(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return empty string.
                        requestHandler.deleteUserContextById(userContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
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
