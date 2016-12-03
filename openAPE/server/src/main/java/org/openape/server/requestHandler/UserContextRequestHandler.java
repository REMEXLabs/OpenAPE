package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.DatabaseObject;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.DatabaseConnection;
import org.openape.server.database.MongoCollectionTypes;
import org.openape.server.rest.UserContextRESTInterface;

/**
 * Class with methods to manage user context on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class UserContextRequestHandler {

    /**
     * Method to store a new user context into the server. It is used by the
     * rest API {@link UserContextRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param userContext
     *            to be stored.
     * @return the ID of the stored user context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete user context.
     */
    public String createUserContext(Object userContext) throws IOException,
            IllegalArgumentException {
        // get database connection.
        DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseconnection.storeData(MongoCollectionTypes.USERCONTEXT,
                    (DatabaseObject) userContext);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Method to delete an existing user context from the server. It is used by
     * the rest API {@link UserContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the user context to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteUserContextById(String id) throws IOException, IllegalArgumentException {
        // get database connection.
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        boolean success = databaseConnection.deleteData(MongoCollectionTypes.USERCONTEXT, id);
        if (!success) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    /**
     * Method to get an existing user context from the server. It is used by the
     * rest API {@link UserContextRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested user context.
     * @return requested user context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public UserContext getUserContextById(String id) throws IOException, IllegalArgumentException {
        return null;
    }

    /**
     * Method to update an existing user context on the server. It is used by
     * the rest API {@link UserContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the user context to update.
     * @param userContext
     *            new version of the user context.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id, not assigned or the user context is
     *             not valid.
     */
    public boolean updateUserContextById(String id, Object userContext) throws IOException,
            IllegalArgumentException {
        return false;
    }
}
