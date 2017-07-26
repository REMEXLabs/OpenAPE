package org.openape.api.resourceDescription;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.openape.api.Messages;
import org.openape.api.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Resource object describing resource files. Including the storage path and the
 * owner id. It is stored into the database, when the corresponding resource is
 * stored onto the file system.
 */
public class ResouceObject extends Resource {
    private static final long serialVersionUID = -5926306380613419041L;

    private String id = null;
    private String fileName = null;
    private String ownerId = null;
    private String mimeType = null;
    private ResourceDescription resourceDescription = null;

    public ResouceObject(String fileName, String ownerId, String mimeType) {
        super();
        this.fileName = fileName;
        this.ownerId = ownerId;
        this.mimeType = mimeType;
    }

    @XmlAttribute(name = "fileName")
    public String getFileName() {
        return this.fileName;
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return this.id;
    }

    @XmlAttribute(name = "mimeType")
    public String getMimeType() {
        return this.mimeType;
    }

    @XmlAttribute(name = "ownerID")
    public String getOwnerId() {
        return this.ownerId;
    }

    /**
     * @return Storage path of the resource, including the filename.
     */
    @JsonIgnore
    public String getPath() {
        final String resourceFolderPath = Messages.getString("ResourceList.rootFolder") + File.separator + Messages.getString("ResourceList.ResourceFolder"); //$NON-NLS-1$ //$NON-NLS-2$
        return resourceFolderPath + File.separator + this.getOwnerId() + File.separator
                + this.getFileName();
    }

    @XmlElement(name = "resourceDescription")
    public ResourceDescription getResourceDescription() {
        return this.resourceDescription;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setResourceDescription(ResourceDescription resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

}
