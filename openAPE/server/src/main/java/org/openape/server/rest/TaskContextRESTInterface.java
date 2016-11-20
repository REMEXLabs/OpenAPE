package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.taskcontext.TaskContext;
import org.openape.server.TaskContextRequestHandler;

import static spark.Spark.*;

public class TaskContextRESTInterface extends SuperRestInterface {

    public TaskContextRESTInterface(
            final TaskContextRequestHandler requestHandler) {
        super();

        /**
         * Request 7.3.2 create task-context.
         */
        post("/api/task-context", (req, res) -> {
            try {
                // Try to map the received json object to a taskContext
                // object.
                TaskContext recievedTaskContext = (TaskContext) extractContentFromRequest(
                        req, TaskContext.class);
                // Test the object for validity.
                if (!recievedTaskContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, save it and return the id.
                String taskContextId = requestHandler
                        .createTaskContext(recievedTaskContext);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return taskContextId;
            } catch (JsonParseException | JsonMappingException e) {
                // If the parse is not successful return bad request error code.
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

        /**
         * Request 7.3.3 get task-context. Used to get a specific task context
         * identified by ID.
         */
        get("/api/task-context/:task-context-id",
                (req, res) -> {
                    String taskContextId = req.params(":task-context-id");
                    try {
                        // if it is successful return task context.
                        TaskContext taskContext = requestHandler
                                .getTaskContextById(taskContextId);
                        res.status(HTTP_STATUS_OK);
                        res.type("application/json");
                        return taskContext;
                        // if not return corresponding error status.
                    } catch (IllegalArgumentException e) {
                        res.status(HTTP_STATUS_BAD_REQUEST);
                        return "";
                    } catch (IOException e) {
                        res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return "";
                    }

                });

        /**
         * Request 7.3.4 update task-context.
         */
        put("/api/task-context/:task-context-id",
                (req, res) -> {
                    String taskContextId = req.params(":task-context-id");
                    try {
                        TaskContext recievedTaskContext = (TaskContext) extractContentFromRequest(
                                req, TaskContext.class);
                        // Test the object for validity.
                        if (!recievedTaskContext.isValid()) {
                            res.status(HTTP_STATUS_BAD_REQUEST);
                            return "";
                        }
                        // If the object is okay, update it.
                        requestHandler.updateTaskContextById(taskContextId,
                                recievedTaskContext);
                        res.status(HTTP_STATUS_OK);
                        return "";
                    } catch (JsonParseException | JsonMappingException
                            | IllegalArgumentException e) {
                        // If the parse or update is not successful return bad
                        // request
                        // error code.
                        res.status(HTTP_STATUS_BAD_REQUEST);
                        return "";
                    } catch (IOException e) {
                        res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return "";
                    }
                });

        /**
         * Request 7.3.5 delete task-context.
         */
        delete("/api/task-context/:task-context-id", (req, res) -> {
            String taskContextId = req.params(":task-context-id");
            try {
                // if it is successful return task context.
                requestHandler.deleteTaskContextById(taskContextId);
                res.status(HTTP_STATUS_OK);
                return "";
                // if not return corresponding error status.
            } catch (IllegalArgumentException e) {
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

    }

}