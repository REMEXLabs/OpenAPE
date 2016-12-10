package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.DatabaseObject;
import org.openape.api.taskcontext.TaskContext;
import org.openape.server.database.DatabaseConnection;
import org.openape.server.database.MongoCollectionTypes;
import org.openape.server.rest.TaskContextRESTInterface;

/**
 * Class with methods to manage task context on the server. It is used by the
 * rest API {@link TaskContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class TaskContextRequestHandlerTest {

//    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.TASKCONTEXT;
//
//    /**
//     * Method to store a new task context into the server. It is used by the
//     * rest API {@link TaskContextRESTInterface} and uses the server database
//     * {@link DatabaseConnection}.
//     *
//     * @param taskContext
//     *            to be stored.
//     * @return the ID of the stored task context.
//     * @throws IOException
//     *             if a storage problem still occurs, after to many tries.
//     * @throws IllegalArgumentException
//     *             if the parameter is not a complete task context.
//     */
//    public String createTaskContext(Object taskContext) throws IOException,
//    IllegalArgumentException {
//        // get database connection.
//        DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
//        // try to store data. Class cast exceptions will be thrown as illegal
//        // argument exceptions. IO exceptions will just be thrown through.
//        String id = null;
//        try {
//            id = databaseconnection.storeData(TaskContextRequestHandlerTest.COLLECTIONTOUSE,
//                    (DatabaseObject) taskContext);
//        } catch (ClassCastException e) {
//            throw new IllegalArgumentException(e.getMessage());
//        }
//        return id;
//    }
//
//    /**
//     * Method to delete an existing task context from the server. It is used by
//     * the rest API {@link TaskContextRESTInterface} and uses the server
//     * database {@link DatabaseConnection}.
//     *
//     * @param id
//     *            the ID of the task context to delete.
//     * @return true if successful. Else an exception is thrown.
//     * @throws IOException
//     *             if a storage problem still occurs, after to many tries.
//     * @throws IllegalArgumentException
//     *             if the id is no valid id or not assigned.
//     */
//    public boolean deleteTaskContextById(String id) throws IOException, IllegalArgumentException {
//        // get database connection.
//        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
//
//        boolean success = databaseConnection.deleteData(TaskContextRequestHandlerTest.COLLECTIONTOUSE,
//                id);
//        if (!success) {
//            throw new IllegalArgumentException();
//        }
//        return true;
//    }
//
//    /**
//     * Method to get an existing task context from the server. It is used by the
//     * rest API {@link TaskContextRESTInterface} and uses the server database
//     * {@link DatabaseConnection}.
//     *
//     * @param id
//     *            the ID of the requested task context.
//     * @return requested task context.
//     * @throws IOException
//     *             if a storage problem still occurs, after to many tries.
//     * @throws IllegalArgumentException
//     *             if the id is no valid id or not assigned.
//     */
//    public TaskContext getTaskContextById(String id) throws IOException, IllegalArgumentException {
//        // get database connection.
//        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
//
//        // Get the requested data.
//        DatabaseObject result = databaseConnection.getData(
//                TaskContextRequestHandlerTest.COLLECTIONTOUSE, id);
//
//        // If the result is null the id is not found.
//        if (result == null) {
//            throw new IllegalArgumentException();
//        }
//
//        // convert into correct type.
//        TaskContext returnObject;
//        try {
//            returnObject = (TaskContext) result;
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//            throw new IOException(e.getMessage());
//        }
//        return returnObject;
//    }
//
//    /**
//     * Method to update an existing task context on the server. It is used by
//     * the rest API {@link TaskContextRESTInterface} and uses the server
//     * database {@link DatabaseConnection}.
//     *
//     * @param id
//     *            the ID of the task context to update.
//     * @param taskContext
//     *            new version of the task context.
//     * @return true if successful. Else an exception is thrown.
//     * @throws IOException
//     *             if a storage problem still occurs, after to many tries.
//     * @throws IllegalArgumentException
//     *             if the id is no valid id, not assigned or the task context is
//     *             not valid.
//     */
//    public boolean updateTaskContextById(String id, Object taskContext) throws IOException,
//    IllegalArgumentException {
//        // get database connection.
//        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
//
//        // Update data. If a class cast exception occurs or the return value is
//        // false the parameters is not valid and an illegal argument exception
//        // is thrown. IO exceptions are thrown through.
//        boolean success;
//        try {
//            success = databaseConnection.updateData(TaskContextRequestHandlerTest.COLLECTIONTOUSE,
//                    (DatabaseObject) taskContext, id);
//        } catch (ClassCastException e) {
//            throw new IllegalArgumentException(e.getMessage());
//        }
//        if (!success) {
//            throw new IllegalArgumentException();
//        }
//        return true;
//    }
}
