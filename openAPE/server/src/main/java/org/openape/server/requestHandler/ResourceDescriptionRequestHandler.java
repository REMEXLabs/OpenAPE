package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.DatabaseObject;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.ResourceDescriptionRESTInterface;

/**
 * Class with methods to manage resource descriptions on the server. It is used by
 * the rest API {@link ResourceDescriptionRESTInterface} and uses the server
 * database {@link DatabaseConnection}.
 */
public class ResourceDescriptionRequestHandler {

    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.RESOURCEDESCRIPTION;

    /**
     * Method to store a new resource description into the server. It is used by
     * the rest API {@link ResourceDescriptionRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param resourceDescription
     *            to be stored.
     * @return the ID of the stored resource description.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete environment context.
     */
    public String createResourceDescription(Object resourceDescription) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseConnection.storeData(ResourceDescriptionRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) resourceDescription);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Method to delete an existing resource description from the server. It is
     * used by the rest API {@link ResourceDescriptionRESTInterface} and uses
     * the server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the resource description to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteResourceDescriptionById(String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteData(
                ResourceDescriptionRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(Messages.getString("ResourceDescriptionRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * Method to get an existing resource description from the server. It is used
     * by the rest API {@link ResourceDescriptionRESTInterface} and uses the
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
    public ResourceDescription getResourceDescriptionById(String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getData(
                ResourceDescriptionRequestHandler.COLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(Messages.getString("ResourceDescriptionRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }

        // convert into correct type.
        ResourceDescription returnObject;
        try {
            returnObject = (ResourceDescription) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;
    }

    /**
     * TODO implement
     * @param listing
     * @return
     */
    public ResourceDescription getResourceDescriptionFromListing(Listing listing) {
        return null;
    }

    /**
     * Method to update an existing resource description on the server. It is
     * used by the rest API {@link ResourceDescriptionRESTInterface} and uses
     * the server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the resource description to update.
     * @param resourceDescription
     *            new version of the resource description.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id, not assigned or the environment
     *             context is not valid.
     */
    public boolean updateResourceDescriptionById(String id, Object resourceDescription)
            throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateData(
                    ResourceDescriptionRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) resourceDescription, id);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!success) {
            throw new IllegalArgumentException(Messages.getString("ResourceDescriptionRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }
}
