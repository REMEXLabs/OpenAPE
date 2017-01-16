package org.openape.server.requestHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javassist.NotFoundException;

import javax.servlet.http.Part;

import org.openape.api.DatabaseObject;
import org.openape.api.listing.Listing;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.database.resources.ListingManager;
import org.openape.server.database.resources.ResourceList;
import org.openape.server.rest.ResourceRESTInterface;

/**
 * Class with methods to manage resources on the server. It is used by the rest
 * API {@link ResourceRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class ResourceRequestHandler {

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
     *             if the resource name is already taken.
     */
    public String createResource(Part resource) throws IOException, IllegalArgumentException {
        return ResourceList.getInstance().addResource(resource);
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
        return ResourceList.getInstance().deleteResource(id);
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
    public File getResourceById(String id) throws IOException, IllegalArgumentException {
        return ResourceList.getInstance().getResoureFile(id);
    }

    /**
     * Method to get an existing resource from the server using a listing. It is
     * used by the rest API {@link ResourceRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param listing
     *            listing used to choose a fitting resource. Has to be a valid
     *            {@link Listing}.
     * @return requested resource.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the listing is no valid listing.
     * @throws NotFoundException
     *             if no fitting resource is found.
     */
    public List<File> getResourceByListing(Listing listing) throws IOException,
            IllegalArgumentException, NotFoundException {
        return ListingManager.getResourcesFromListing(listing);
    }

    /**
     * Method to get an existing listing from the server. It is used by the rest
     * API {@link ResourceRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested listing.
     * @return requested listing.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public Listing getListingById(String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getData(MongoCollectionTypes.LISTING, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException();
        }

        // convert into correct type.
        Listing returnObject;
        try {
            returnObject = (Listing) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;

    }

}
