package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.EnvironmentContextRESTInterface;

/**
 * Class with methods to manage environment context on the server. It is used by
 * the rest API {@link EnvironmentContextRESTInterface} and uses the server
 * database {@link DatabaseConnection}.
 */
public class EnvironmentContextRequestHandler {

    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.ENVIRONMENTCONTEXT;

    /**
     * Method to store a new environment context into the server. It is used by
     * the rest API {@link EnvironmentContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param environmentContext
     *            to be stored.
     * @return the ID of the stored environment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete environment context.
     */
    public String createEnvironmentContext(final Object environmentContext) throws IOException,
    IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseConnection.storeData(EnvironmentContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) environmentContext);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Method to delete an existing environment context from the server. It is
     * used by the rest API {@link EnvironmentContextRESTInterface} and uses the
     * server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the environment context to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteEnvironmentContextById(final String id) throws IOException,
    IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteData(
                EnvironmentContextRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("EnvironmentContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * Method to get an existing environment context from the server. It is used
     * by the rest API {@link EnvironmentContextRESTInterface} and uses the
     * server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested environment context.
     * @return requested environment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public EnvironmentContext getEnvironmentContextById(final String id) throws IOException,
    IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getData(
                EnvironmentContextRequestHandler.COLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(
                    Messages.getString("EnvironmentContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }

        // convert into correct type.
        EnvironmentContext returnObject;
        try {
            returnObject = (EnvironmentContext) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;
    }

    /**
     * Method to update an existing environment context on the server. It is
     * used by the rest API {@link EnvironmentContextRESTInterface} and uses the
     * server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the environment context to update.
     * @param environmentContext
     *            new version of the environment context.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id, not assigned or the environment
     *             context is not valid.
     */
    public boolean updateEnvironmentContextById(final String id, final Object environmentContext)
            throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateData(
                    EnvironmentContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) environmentContext, id);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("EnvironmentContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }
}
