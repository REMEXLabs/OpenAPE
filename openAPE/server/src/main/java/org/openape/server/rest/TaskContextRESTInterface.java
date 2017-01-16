package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.taskcontext.TaskContext;
import org.openape.server.requestHandler.TaskContextRequestHandler;

import spark.Spark;

public class TaskContextRESTInterface extends SuperRestInterface {

    public TaskContextRESTInterface(final TaskContextRequestHandler requestHandler) {
        super();

        /**
         * Request 7.3.2 create task-context.
         */
        Spark.post(
                Messages.getString("TaskContextRESTInterface.TastContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a taskContext
                        // object.
                        final TaskContext recievedTaskContext = (TaskContext) this
                                .extractContentFromRequest(req, TaskContext.class);
                        // Test the object for validity.
                        if (!recievedTaskContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return "No valid context object";
                        }
                        // If the object is okay, save it and return the id.
                        final String taskContextId = requestHandler
                                .createTaskContext(recievedTaskContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        res.type(Messages.getString("TaskContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        return taskContextId;
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
                                .getTaskContextById(taskContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("TaskContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        return taskContext;
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
                        final TaskContext recievedTaskContext = (TaskContext) this
                                .extractContentFromRequest(req, TaskContext.class);
                        // Test the object for validity.
                        if (!recievedTaskContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return "No valid context object";
                        }
                        // If the object is okay, update it.
                        requestHandler.updateTaskContextById(taskContextId, recievedTaskContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("TaskContextRESTInterface.EmptyString"); //$NON-NLS-1$ //TODO return right statuscode
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
                        // if it is successful return task context.
                        requestHandler.deleteTaskContextById(taskContextId);
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

    }

}
