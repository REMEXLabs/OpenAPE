package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.DatabaseObject;
import org.openape.api.resource.Resource;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.DatabaseConnection;
import org.openape.server.database.MongoCollectionTypes;
import org.openape.server.rest.ResourceRESTInterface;

/**
 * Class with methods to manage resources on the server. It is used by the rest
 * API {@link ResourceRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class ResourceRequestHandler {
    private static final MongoCollectionTypes REQUESTCOLLECTIONTOUSE = MongoCollectionTypes.RESOURCEREQUEST;
    private static final MongoCollectionTypes OFFERCOLLECTIONTOUSE = MongoCollectionTypes.RESOURCEOFFER;

    /**
     * Method to store a new resource into the server. It is used by the rest
     * API {@link ResourceRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param resource
     *            to be stored.
     * @return the ID of the stored resource.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete resource.
     */
    public String createResource(Object resource) throws IOException, IllegalArgumentException {
        // get database connection.
        DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseconnection.storeData(ResourceRequestHandler.OFFERCOLLECTIONTOUSE,
                    (DatabaseObject) resource);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Method to delete an existing resource from the server. It is used by the
     * rest API {@link ResourceRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the resource to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteResourceById(String id) throws IOException, IllegalArgumentException {
        // get database connection.
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        boolean success = databaseConnection.deleteData(
                ResourceRequestHandler.OFFERCOLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    /**
     * Method to get an existing resource from the server. It is used by the
     * rest API {@link ResourceRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested resource.
     * @return requested resource.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public Resource getResourceById(String id) throws IOException, IllegalArgumentException {
        // get database connection.
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        DatabaseObject result = databaseConnection.getData(
                ResourceRequestHandler.OFFERCOLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException();
        }

        // convert into correct type.
        UserContext returnObject;
        try {
            returnObject = (UserContext) result;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;

    }

}
