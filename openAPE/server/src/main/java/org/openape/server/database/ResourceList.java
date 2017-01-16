package org.openape.server.database;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.openape.server.requestHandler.ResourceRequestHandler;
import org.openape.server.rest.ResourceRESTInterface;

/**
 * {@link ResourceList} is a singleton class that contains a list holding all
 * names of resources stored on the file system. It allows get the List, store
 * resources and load resources. It is used by the
 * {@link ResourceRequestHandler} which is used by the
 * {@link ResourceRESTInterface}. The {@link ResourceRESTInterface} is used by
 * the outside user to store load and delete resources.
 *
 */
public class ResourceList {
    private static final String RESOURCEFOLDERPATH = "src/resources";

    // List containing all resources stored on the file system.
    private List<String> resourceNameList = new LinkedList<String>();

    /**
     * Singleton instance of this class.
     */
    private static ResourceList resourceListInstance;

    /**
     * Get the singleton database connection.
     *
     * @return the database connection.
     */
    public static ResourceList getInstance() {
        if (ResourceList.resourceListInstance == null) {
            ResourceList.resourceListInstance = new ResourceList();
        }
        return ResourceList.resourceListInstance;
    }

    /**
     * Private constructor, filling the resourceList with the filenames of the
     * resources already in the resource directory.
     */
    private ResourceList() {
        // Add all filenames of resources in the resource folder to resource
        // list.
        File folder = new File(RESOURCEFOLDERPATH);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            // There can be directories that would be listed, too. Therefore
            // check if file.
            if (listOfFiles[i].isFile()) {
                this.resourceNameList.add(listOfFiles[i].getName());
            }
        }
    }
    
    /**
     * 
     * @param resource
     * @return
     */
    public boolean addResource(Object resource) {
        
        return false;
    }

}
