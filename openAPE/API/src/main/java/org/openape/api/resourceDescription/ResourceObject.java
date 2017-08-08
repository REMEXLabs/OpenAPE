package org.openape.api.resourceDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.openape.api.Messages;
import org.openape.api.Resource;
import org.openape.api.group.GroupAccessRight;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Resource object describing resource files. Including the storage path and the
 * owner id. It is stored into the database, when the corresponding resource is
 * stored onto the file system.
 */
public class ResourceObject extends Resource {
    private static final long serialVersionUID = -5926306380613419041L;

    private String id = null;
    private String fileName = null;
    private String ownerId = null;
    private String mimeType = null;
    private GroupAccessRight groupAccessRight = null;
    private List<ResourceDescription> resourceDescriptions = null;

    public ResourceObject() {
        this.resourceDescriptions = new ArrayList<ResourceDescription>();
    }

    public ResourceObject(final String fileName, final String ownerId, final String mimeType,
            final GroupAccessRight groupAccessRight) {
        super();
        this.fileName = fileName;
        this.ownerId = ownerId;
        this.mimeType = mimeType;
        this.groupAccessRight = groupAccessRight;
    }

    @JsonIgnore
    public void addResourceDescription(final ResourceDescription resourceDescription) {
        this.resourceDescriptions.add(resourceDescription);
    }

    @XmlAttribute(name = "fileName")
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @return Storage path of the resource, excluding the filename.
     */
    @JsonIgnore
    public String getFolder() {
        final String resourceFolderPath = Messages.getString("ResourceList.rootFolder") + File.separator + Messages.getString("ResourceList.ResourceFolder"); //$NON-NLS-1$ //$NON-NLS-2$
        return resourceFolderPath + File.separator + this.getOwnerId();
    }

    @XmlElement(name = "groupAccessRight")
    public GroupAccessRight getGroupAccessRight() {
        return this.groupAccessRight;
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
    public List<ResourceDescription> getResourceDescriptions() {
        return this.resourceDescriptions;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public void setGroupAccessRight(final GroupAccessRight groupAccessRight) {
        this.groupAccessRight = groupAccessRight;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public void setResourceDescriptions(final List<ResourceDescription> resourceDescriptions) {
        this.resourceDescriptions = resourceDescriptions;
    }
}
