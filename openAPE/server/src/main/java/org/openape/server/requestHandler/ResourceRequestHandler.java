package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.List;

import javassist.NotFoundException;

import org.apache.commons.fileupload.FileItem;
import org.openape.api.listing.Listing;
import org.openape.api.user.User;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.resources.GetResourceReturnType;
import org.openape.server.database.resources.ListingManager;
import org.openape.server.database.resources.ResourceList;
import org.openape.server.rest.ResourceRESTInterface;
import org.pac4j.core.profile.CommonProfile;

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
     * @param mimeType
     *            mime type of the data to store
     * @param user
     *            owner of the resource
     * @return the ID of the stored resource.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the resource name is already taken.
     */
    public String createResource(FileItem resource, String mimeType, User user) throws IOException,
            IllegalArgumentException {
        return ResourceList.getInstance().addResource(resource, mimeType, user);
    }

    /**
     * Method to delete an existing resource from the server. It is used by the
     * rest API {@link ResourceRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the resource to delete.
     * @param profile
     *            of the user who requests to delete the resource.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteResourceById(String id, CommonProfile profile) throws IOException,
            IllegalArgumentException, UnauthorizedException {
        return ResourceList.getInstance().deleteResource(id, profile);
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
        final ListingRequestHandler listingRequestHandler = new ListingRequestHandler();
        return listingRequestHandler.getListingById(id);
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
    public GetResourceReturnType getResourceById(String id) throws IllegalArgumentException,
            IOException {
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
    public List<GetResourceReturnType> getResourceByListing(Listing listing) throws IOException,
            IllegalArgumentException, NotFoundException {
        return ListingManager.getResourcesFromListing(listing);
    }

}
