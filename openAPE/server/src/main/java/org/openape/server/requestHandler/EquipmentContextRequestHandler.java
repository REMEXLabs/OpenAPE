package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.EquipmentContextList;
import org.openape.api.Messages;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.EquipmentContextRESTInterface;

/**
 * Class with methods to manage equipment context on the server. It is used by
 * the rest API {@link EquipmentContextRESTInterface} and uses the server
 * database {@link DatabaseConnection}.
 */
public class EquipmentContextRequestHandler implements ContextRequestHandler {

    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.EQUIPMENTCONTEXT;

    /**
     * Method to store a new equipment context into the server. It is used by
     * the rest API {@link EquipmentContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param equipmentContext
     *            to be stored.
     * @return the ID of the stored equipment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete equipment context.
     */
    public String createEquipmentContext(final Object equipmentContext) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseconnection.storeDatabaseObject(
                    EquipmentContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) equipmentContext);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Method to delete an existing equipment context from the server. It is
     * used by the rest API {@link EquipmentContextRESTInterface} and uses the
     * server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the equipment context to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteEquipmentContextById(final String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteDatabaseObject(
                EquipmentContextRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("EquipmentContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * Method to get an existing equipment context from the server. It is used
     * by the rest API {@link EquipmentContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested equipment context.
     * @return requested equipment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public EquipmentContext getEquipmentContextById(final String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getDatabaseObjectById(
                EquipmentContextRequestHandler.COLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(
                    Messages.getString("EquipmentContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }

        // convert into correct type.
        EquipmentContext returnObject;
        try {
            returnObject = (EquipmentContext) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;
    }

    /**
     * Method to update an existing equipment context on the server. It is used
     * by the rest API {@link EquipmentContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the equipment context to update.
     * @param equipmentContext
     *            new version of the equipment context.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id, not assigned or the equipment
     *             context is not valid.
     */
    public boolean updateEquipmentContextById(final String id, final Object equipmentContext)
            throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateDatabaseObject(
                    EquipmentContextRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) equipmentContext, id);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("EquipmentContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    @Override
    public EquipmentContextList getAllContexts(String url) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EquipmentContextList getMyContexts(String userId, String url) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EquipmentContextList getPublicContexts(String url) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}
