package org.openape.server.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
     */
    public static List<File> getResourcesFromListing(Listing listing) {
        String query = listing.getQuery();
        ResourceList resourceList = ResourceList.getInstance();
        List<String> allResourceNames = resourceList.getResourceNameList();
        List<File> resources = new ArrayList<File>();

        // get all resource names mentioned in the query and add the files into
        // the resources.
        for (String name : allResourceNames) {
            if (query.contains(name)) {
                try {
                    resources.add(resourceList.getResoureFile(name));
                } catch (IllegalArgumentException e) {
                    System.err.println("Resource contained in the resource list "
                            + "has been removed from the file system");
                }
            }
        }

        return resources;
    }
}
