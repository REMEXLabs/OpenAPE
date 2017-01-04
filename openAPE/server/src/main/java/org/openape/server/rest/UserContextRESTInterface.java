package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.usercontext.UserContext;
import org.openape.server.requestHandler.UserContextRequestHandler;

import spark.Spark;

public class UserContextRESTInterface extends SuperRestInterface {

    public UserContextRESTInterface(final UserContextRequestHandler requestHandler) {
        super();

        /**
         * test request to test if the server runs. Invoke locally using:
         * http://localhost:4040/hello todo Remove
         */
        Spark.get("/hello", (req, res) -> "Hello World");

        /**
         * Request 7.2.2 create user-context.
         */
        Spark.post(
                Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a userContext
                        // object.
                        UserContext recievedUserContext = (UserContext) this
                                .extractContentFromRequest(req, UserContext.class);
                        // Test the object for validity.
                        if (!recievedUserContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        String userContextId = requestHandler
                                .createUserContext(recievedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        res.type(Messages.getString("UserContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        return userContextId;
                    } catch (JsonParseException | JsonMappingException e) {
                        // If the parse is not successful return bad request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }
                });

        /**
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        Spark.get(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return user context.
                        UserContext userContext = requestHandler.getUserContextById(userContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("UserContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        return userContext;
                        // if not return corresponding error status.
                    } catch (IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }

                });

        /**
         * Request 7.2.4 update user-context.
         */
        Spark.put(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        UserContext recievedUserContext = (UserContext) this
                                .extractContentFromRequest(req, UserContext.class);
                        // Test the object for validity.
                        if (!recievedUserContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        }
                        // If the object is okay, update it.
                        requestHandler.updateUserContextById(userContextId, recievedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
                        // If the parse or update is not successful return bad
                        // request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }
                });

        /**
         * Request 7.2.5 delete user-context.
         */
        Spark.delete(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return user context.
                        requestHandler.deleteUserContextById(userContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        // if not return corresponding error status.
                    } catch (IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }
                });

    }

}
