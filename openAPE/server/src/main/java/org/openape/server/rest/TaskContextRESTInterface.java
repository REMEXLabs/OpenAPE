package org.openape.server.rest;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.openape.api.Messages;
import org.openape.api.TaskContextList;
import org.openape.api.taskcontext.TaskContext;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.TaskContextRequestHandler;

import spark.Request;
import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TaskContextRESTInterface extends ContextRestInterface {
    private static TaskContext createRequestObejct(final Request req)
            throws IllegalArgumentException, IOException {
        final String contentType = req.contentType();
        if (contentType.equals(MediaType.APPLICATION_JSON)) {
            return TaskContext.getObjectFromJson(req.body());
        } else if (contentType.equals(MediaType.APPLICATION_XML)) {
            return TaskContext.getObjectFromXml(req.body());
        } else {
            throw new IllegalArgumentException("wrong content-type");
        }
    }

    private static String createReturnString(final Request req, final TaskContext taskContext)
            throws IOException, IllegalArgumentException {
        final String contentType = req.contentType();

        if (contentType != null) {
            if (contentType.equals(MediaType.APPLICATION_JSON)) {
                return taskContext.getFrontendJson();
            } else if (contentType.equals(MediaType.APPLICATION_XML)) {
                return taskContext.getXML();
            } else {
                throw new IllegalArgumentException("wrong content-type");
            }
        } else {
            return taskContext.getXML();
        }

    }

    public static void setupTaskContextRESTInterface(
            final TaskContextRequestHandler requestHandler, final AuthService auth) {

        // Authentication: Make sure only registered principals (users and
        // admins) can create a new context
        Spark.before(Messages.getString("TaskContextRESTInterface.TastContextURLWithoutID"),
                auth.authorize("anonymous"));
        // Authentication: Everyone can access the route for a specific context
        Spark.before(Messages.getString("TaskContextRESTInterface.TastContextURLWithID"),
                auth.authorize("anonymous"));

        /**
         * Request 7.3.2 create task-context.
         */
        Spark.post(
                Messages.getString("TaskContextRESTInterface.TastContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a taskContext
                        // object.
                        final TaskContext receivedTaskContext = TaskContextRESTInterface
                                .createRequestObejct(req);
                        // Make sure to set the id of the authenticated user as
                        // the ownerId
                        receivedTaskContext.getImplementationParameters().setOwner(
                                auth.getAuthenticatedUser(req, res).getId());
                        // Test the object for validity.
                        if (!receivedTaskContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("TaskContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String taskContextId = requestHandler
                                .createContext(receivedTaskContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        return taskContextId;
                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
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
         * Request 7.3.3 get task-context. Used to get a specific task context
         * identified by ID.
         */
        Spark.get(
                Messages.getString("TaskContextRESTInterface.TastContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String taskContextId = req.params(Messages
                            .getString("TaskContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return task context.
                        final TaskContext taskContext = requestHandler
                                .getContextById(taskContextId);
                        // Make sure only admins or the owner can view the
                        // context, except if it is public
                        auth.allowAdminOwnerAndPublic(req, res, taskContext
                                .getImplementationParameters().getOwner(), taskContext
                                .getImplementationParameters().isPublic());
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("TaskContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        final String jsonData = TaskContextRESTInterface.createReturnString(req,
                                taskContext);
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
         * Request 7.3.4 update task-context.
         */
        Spark.put(
                Messages.getString("TaskContextRESTInterface.TastContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String taskContextId = req.params(Messages
                            .getString("TaskContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        final TaskContext receivedTaskContext = TaskContextRESTInterface
                                .createRequestObejct(req);
                        // Test the object for validity.
                        if (!receivedTaskContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("TaskContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // Check if the task context does exist
                        final TaskContext taskContext = requestHandler
                                .getContextById(taskContextId);
                        // Make sure only admins and the owner can update a
                        // context
                        auth.allowAdminAndOwner(req, res, taskContext.getImplementationParameters()
                                .getOwner());
                        receivedTaskContext.getImplementationParameters().setOwner(
                                taskContext.getImplementationParameters().getOwner()); // Make
                        // sure
                        // the
                        // owner
                        // can't
                        // be
                        // changed
                        // Perform update
                        requestHandler.updateContextById(taskContextId, receivedTaskContext);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("TaskContextRESTInterface.EmptyString"); //$NON-NLS-1$ 
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
         * Request 7.3.5 delete task-context.
         */
        Spark.delete(
                Messages.getString("TaskContextRESTInterface.TastContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String taskContextId = req.params(Messages
                            .getString("TaskContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // Check if the user context does exist
                        final TaskContext taskContext = requestHandler
                                .getContextById(taskContextId);
                        // Make sure only admins and the owner can delete a
                        // context
                        auth.allowAdminAndOwner(req, res, taskContext.getImplementationParameters()
                                .getOwner());
                        // if it is successful return empty string.
                        requestHandler.deleteContextById(taskContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("TaskContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        // if not return corresponding error status.
                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });
        createContextListRestEndpoint(
                Messages.getString("TaskContextRESTInterface.TastContextURLWithoutID"),
                requestHandler, auth, TaskContextList.class);

    }

}
