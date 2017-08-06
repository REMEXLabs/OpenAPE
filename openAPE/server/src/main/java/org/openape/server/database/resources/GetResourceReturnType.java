package org.openape.server.database.resources;

import java.io.File;

/**
 * Loading a resource has to return a {@link File} and a mime type as string.
 * This class represents objects containing both.
 */
public class GetResourceReturnType {
    private File file;
    private String mimeType;

    public GetResourceReturnType(final File file, final String mimeType) {
        this.file = file;
        this.mimeType = mimeType;
    }

    public File getFile() {
        return this.file;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

}
