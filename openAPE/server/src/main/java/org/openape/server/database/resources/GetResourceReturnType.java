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
    public GetResourceReturnType(final File file, final ResourceObject resourceObject) {
        this.file = file;
        this.resourceObject = resourceObject;
    }

    public File getFile() {
        return this.file;
    }

    public ResourceObject getResourceObject() {
        return this.resourceObject;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public void setResourceObject(final ResourceObject resourceObject) {
        this.resourceObject = resourceObject;
    }

}
