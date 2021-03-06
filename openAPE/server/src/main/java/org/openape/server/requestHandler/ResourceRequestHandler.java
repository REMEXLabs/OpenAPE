package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.List;

import javassist.NotFoundException;

import org.apache.commons.fileupload.FileItem;
import org.openape.api.group.GroupAccessRights;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceObject;
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
     * @param groupAccessRight
     * @return the ID of the stored resource.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the resource name is already taken.
     */
    public String createResource(final FileItem resource, final String mimeType, final User user,
            GroupAccessRights groupAccessRights) throws IOException, IllegalArgumentException {
        return ResourceList.getInstance().addResource(resource, mimeType, user, groupAccessRights);
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
    public boolean deleteResourceById(final String id, final CommonProfile profile)
            throws IOException, IllegalArgumentException, UnauthorizedException {
        final boolean success = ResourceList.getInstance().deleteResource(id, profile);
        final ResourceDescriptionRequestHandler resourceDescriptionRequestHandler = new ResourceDescriptionRequestHandler();
        resourceDescriptionRequestHandler.deleteAllDescriptionsOfAResource(id);
        return success;
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
    public Listing getListingById(final String id) throws IOException, IllegalArgumentException {
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
    public GetResourceReturnType getResourceById(final String id) throws IllegalArgumentException,
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
    public List<GetResourceReturnType> getResourceByListing(final Listing listing)
            throws IOException, IllegalArgumentException, NotFoundException {
        return ListingManager.getResourcesFromListing(listing);
    }
    
    /**
     * Method to update an existing resource on the server. Note that the resource's id cannot be changed and that the
     * file, which is associated with the resource will not be updated. Thus you should not try to update the file
     * itself, its name or owner id with this method!
     * 
     * @param resourceObject
     *            the resource which contains the updates.
     * @param profile
     *            of the user who requests to update the resource.
     * @throws IllegalArgumentException
     *             if there exists no resource with the given id.
     * @throws IOException
     *             if a problem with the database occurs.
     * @throws UnauthorizedException
     *             if the user has no right to update the resource.
     */
    public void updateResourceById(final ResourceObject resourceObject, final CommonProfile profile)
            throws IllegalArgumentException, IOException, UnauthorizedException {
        ResourceList.getInstance().updateResource(resourceObject, profile);
    }

}
