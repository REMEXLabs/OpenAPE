package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.Messages;
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
                        final EnvironmentContext recievedEnvironmentContext = (EnvironmentContext) this
                                .extractContentFromRequest(req, EnvironmentContext.class);
                        // Test the object for validity.
                        if (!recievedEnvironmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages.getString("EnvironmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String environmentContextId = requestHandler
                                .createEnvironmentContext(recievedEnvironmentContext);
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
        Spark.put(
                Messages.getString("EnvironmentContextRESTInterface.EnvironmentContextsURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String environmentContextId = req.params(Messages
                            .getString("EnvironmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        final EnvironmentContext recievedEnvironmentContext = (EnvironmentContext) this
                                .extractContentFromRequest(req, EnvironmentContext.class);
                        // Test the object for validity.
                        if (!recievedEnvironmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages.getString("EnvironmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, update it.
                        requestHandler.updateEnvironmentContextById(environmentContextId,
                                recievedEnvironmentContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("EnvironmentContextRESTInterface.EmptyString"); //$NON-NLS-1$ //TODO return right statuscode
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
                        // if it is successful return empty string.
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
