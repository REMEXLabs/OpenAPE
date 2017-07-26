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
    private ResourceDescription resourceDescription = null;

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlAttribute(name = "ownerID")
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @XmlElement(name = "resourceDescription")
    public ResourceDescription getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(ResourceDescription resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    /**
     * @return Storage path of the resource, including the filename.
     */
    @JsonIgnore
    public String getPath() {
        String resourceFolderPath = Messages.getString("ResourceList.rootFolder") + File.separator + Messages.getString("ResourceList.ResourceFolder"); //$NON-NLS-1$ //$NON-NLS-2$
        return resourceFolderPath + File.separator + this.getOwnerId() + File.separator
                + this.getFileName();
    }

}
