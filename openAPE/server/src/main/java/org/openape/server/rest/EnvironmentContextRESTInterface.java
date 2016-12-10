package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;

import spark.Spark;

public class EnvironmentContextRESTInterface extends SuperRestInterface {

    public EnvironmentContextRESTInterface(final EnvironmentContextRequestHandler requestHandler) {
        super();

        /**
         * Request 7.5.2 create environment-context.
         */
        Spark.post(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a
                        // environmentContext
                        // object.
                        EnvironmentContext recievedEnvironmentContext = (EnvironmentContext) this
                                .extractContentFromRequest(req, EnvironmentContext.class);
                        // Test the object for validity.
                        if (!recievedEnvironmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        String environmentContextId = requestHandler
                                .createEnvironmentContext(recievedEnvironmentContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("EnvironmentContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        return environmentContextId;
                    } catch (JsonParseException | JsonMappingException e) {
                        // If the parse is not successful return bad request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }
                });

        /**
         * Request 7.5.3 get environment-context. Used to get a specific
         * environment context identified by ID.
         */
        Spark.get(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    String environmentContextId = req.params(Messages
                            .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return environment context.
                        EnvironmentContext environmentContext = requestHandler
                                .getEnvironmentContextById(environmentContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("EnvironmentContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        return environmentContext;
                        // if not return corresponding error status.
                    } catch (IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }

                });

        /**
         * Request 7.5.4 update environment-context.
         */
        Spark.put(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    String environmentContextId = req.params(Messages
                            .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        EnvironmentContext recievedEnvironmentContext = (EnvironmentContext) this
                                .extractContentFromRequest(req, EnvironmentContext.class);
                        // Test the object for validity.
                        if (!recievedEnvironmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        }
                        // If the object is okay, update it.
                        requestHandler.updateEnvironmentContextById(environmentContextId,
                                recievedEnvironmentContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
                        // If the parse or update is not successful return bad
                        // request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }
                });

        /**
         * Request 7.5.5 delete environment-context.
         */
        Spark.delete(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), (req, res) -> { //$NON-NLS-1$
                    String environmentContextId = req.params(Messages
                            .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return environment context.
                        requestHandler.deleteEnvironmentContextById(environmentContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        // if not return corresponding error status.
                    } catch (IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    }
                });

    }

}
