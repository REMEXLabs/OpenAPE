package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.UserContextRESTInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with methods to manage user context on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class UserContextRequestHandler {
private static Logger logger = LoggerFactory.getLogger(UserContextRequestHandler.class );
    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.USERCONTEXT;

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
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseconnection.storeData(UserContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) userContext);
            logger.debug("New user context in database with id \"" + id + "\".");
        } catch (final ClassCastException e) {
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
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteData(
                UserContextRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
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
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getData(
                UserContextRequestHandler.COLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(
                    Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }

        // convert into correct type.
        UserContext returnObject;
        try {
            returnObject = (UserContext) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;

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
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateData(UserContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) userContext, id);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }
}
