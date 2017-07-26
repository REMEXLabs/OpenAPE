package org.openape.server.database.resources;

import java.io.File;

import org.openape.api.resourceDescription.ResourceObject;

/**
 * Loading a resource has to return a {@link File} and a resource object
 * containing the mime type. This class represents objects containing both.
 */
public class GetResourceReturnType {
    private File file;
    private ResourceObject resourceObject;

    public ResourceObject getResourceObject() {
        return resourceObject;
    }

    public void setResourceObject(ResourceObject resourceObject) {
        this.resourceObject = resourceObject;
    }

    public GetResourceReturnType(File file, ResourceObject resourceObject) {
        this.file = file;
        this.resourceObject = resourceObject;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
