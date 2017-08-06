package org.openape.server.database.mongoDB;

import org.openape.api.DatabaseObject;

/**
 * The class is used to represent a mime type string of a stored resource within
 * the database.
 */
public class MimeTypeDatabaseObject extends DatabaseObject {
    private static final long serialVersionUID = -3507423199805025440L;

    private String mimeType;

    public MimeTypeDatabaseObject() {
    }

    public MimeTypeDatabaseObject(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
