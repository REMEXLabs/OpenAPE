package org.openape.server.requestHandler;

import java.io.File;
import java.io.IOException;

import javassist.NotFoundException;

import javax.servlet.http.Part;

import org.openape.api.listing.Listing;
import org.openape.server.database.DatabaseConnection;
import org.openape.server.database.ResourceList;
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
    public Object getResourceByListing(Object listing) throws IOException,
    IllegalArgumentException, NotFoundException {
        final Object returnObject = null;
        return returnObject;
    }

}
