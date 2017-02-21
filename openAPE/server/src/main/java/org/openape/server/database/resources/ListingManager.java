package org.openape.server.database.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.Messages;
import org.openape.api.listing.Listing;

/**
 * Stub class containing a simple implementation of the algorithm to find
 * fitting resources to a {@link Listing}.
 */
public class ListingManager {

    /**
     * @param listing
     * @return a list containing all resources which names where contained in
     *         the query string.
     * @throws IOException
     *             if unable to create resource folder.
     */
    public static List<File> getResourcesFromListing(Listing listing) throws IOException {

        ResourceList resourceList = ResourceList.getInstance();
        List<String> allResourceNames = resourceList.getResourceNameList();
        List<File> resources = new ArrayList<File>();

//        String query = listing.getQuery();
//        // get all resource names mentioned in the query and add the files into
//        // the resources.
//        for (String name : allResourceNames) {
//            if (query.contains(name)) {
//                try {
//                    resources.add(resourceList.getResoureFile(name));
//                } catch (IllegalArgumentException e) {
//                    System.err.println(Messages.getString("ListingManager.ResourceNotFoundErrorMsgPart1") //$NON-NLS-1$
//                            + Messages.getString("ListingManager.ResourceNotFoundErrorMsgPart2")); //$NON-NLS-1$
//                }
//            }
//        }

        return resources;
    }
}
