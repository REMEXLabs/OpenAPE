package org.openape.server.database.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceDescription;

/**
 * Stub class containing a simple implementation of the algorithm to find
 * fitting resources to a {@link Listing}.
 */
public class ListingManager {
    protected static String query = Messages.getString("EmptyString"); //$NON-NLS-1$

    /**
     * Looks for resource names that are represented in the resource description
     * descriptor values and returns those resources.
     *
     * @param listing
     * @return a list containing all resources which names where contained in
     *         the resource description descriptor values. Or an empty list if
     *         no matches occurred.
     * @throws IOException
     *             if unable to create resource folder.
     */
    public static List<GetResourceReturnType> getResourcesFromListing(final Listing listing) throws IOException {
        // reset query
        ListingManager.query = Messages.getString("EmptyString"); //$NON-NLS-1$
        final ResourceList resourceList = ResourceList.getInstance();
        final List<String> allResourceNames = resourceList.getResourceNameList();
        final List<GetResourceReturnType> resources = new ArrayList<GetResourceReturnType>();
        final ResourceDescription resourceDescription = listing.getResourceDescriptionQurey();
        // if no resource description if found return empty list.
        if (resourceDescription == null) {
            return resources;
        }
        // add all values of descriptors of the resource description to the
        // query.
        resourceDescription.getPropertys().forEach(property -> property.getDescriptors().keySet()
                .forEach(key -> ListingManager.query += property.getDescriptors().get(key)));
        // get all resource names mentioned in the query and add the files into
        // the resources.
        for (final String name : allResourceNames) {
            if (ListingManager.query.contains(name)) {
                try {
                    resources.add(resourceList.getResoureFile(name));
                } catch (final IllegalArgumentException e) {
                    System.err.println(Messages.getString("ListingManager.ResourceNotFoundErrorMsgPart1") //$NON-NLS-1$
                            + Messages.getString("ListingManager.ResourceNotFoundErrorMsgPart2")); //$NON-NLS-1$
                }
            }
        }

        return resources;
    }
}
