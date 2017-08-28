package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.Messages;
import org.openape.api.UserContextList;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.UserContextRESTInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;

/**
 * Class with methods to manage user context on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class UserContextRequestHandler {
    private static Logger logger = LoggerFactory.getLogger(UserContextRequestHandler.class);

    public static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.USERCONTEXT;

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
    public String createUserContext(final Object userContext) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseconnection.storeDatabaseObject(UserContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) userContext);
            UserContextRequestHandler.logger.debug("New user context in database with id \"" + id
                    + "\".");
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
    public boolean deleteUserContextById(final String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteDatabaseObject(
                UserContextRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    public UserContextList getAllUserContexts(final String url) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        final List<UserContext> contexts = new ArrayList<UserContext>();
        final List<DatabaseObject> result = databaseConnection.getDatabaseObjectsByQuery(
                MongoCollectionTypes.USERCONTEXT, null);
        // parse result from DatabaseObject to UserContext.
        try {
            for (final DatabaseObject databaseObject : result) {
                contexts.add((UserContext) databaseObject);
            }
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }
        return new UserContextList(contexts, url);
    }

    public UserContextList getMyContexts(final String userId, final String url) throws IOException {
        final BasicDBObject query = new BasicDBObject();
        query.put("owner", userId);
        return this.getUserContexts(query, url);
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
    public UserContext getUserContextById(final String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getDatabaseObjectById(
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

    public UserContextList getUserContexts(final BasicDBObject query, final String url)
            throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final List<DatabaseObject> result = databaseConnection.getDatabaseObjectsByQuery(
                MongoCollectionTypes.USERCONTEXT, query);
        final List<UserContext> contexts = new ArrayList<UserContext>();

        for (final DatabaseObject databaseObject : result) {
            contexts.add((UserContext) databaseObject);
        }
        return new UserContextList(contexts, url);

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
    public boolean updateUserContextById(final String id, final Object userContext)
            throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateDatabaseObject(
                    UserContextRequestHandler.COLLECTIONTOUSE, (DatabaseObject) userContext, id);
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
