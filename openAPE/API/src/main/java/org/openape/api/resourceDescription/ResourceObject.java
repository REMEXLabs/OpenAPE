package org.openape.api.resourceDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import org.openape.api.Messages;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.ImplementationParameters;
import org.openape.api.group.GroupAccessRights;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resource object describing resource files. Including the storage path and the
 * owner id. It is stored into the database, when the corresponding resource is
 * stored onto the file system.
 */
public class ResourceObject extends DatabaseObject {
    private static final long serialVersionUID = -5926306380613419041L;
    private ImplementationParameters implementationParameters;

    private String id = null;
    private String fileName = null;
    private String ownerId = null;
    private String mimeType = null;
    private GroupAccessRights groupAccessRights = null;
    private List<ResourceDescription> resourceDescriptions = null;

    public ResourceObject() {
        this.resourceDescriptions = new ArrayList<ResourceDescription>();
    }

    public ResourceObject(final String fileName, final String ownerId, final String mimeType,
            final GroupAccessRights groupAccessRights) {
        super();
        this.fileName = fileName;
        this.ownerId = ownerId;
        this.mimeType = mimeType;
        this.groupAccessRights = groupAccessRights;
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
        final String resourceFolderPath = Messages.getString("ResourceList.rootFolder") + File.separator //$NON-NLS-1$
                + Messages.getString("ResourceList.ResourceFolder");  //$NON-NLS-1$
        return resourceFolderPath + File.separator + this.getOwnerId();
    }

    @XmlElement(name = "groupAccessRights")
    public GroupAccessRights getGroupAccessRights() {
        return this.groupAccessRights;
    }

    @Override
    @XmlAttribute(name = "id")
    public String getId() {
        return this.id;
    }

    @JsonProperty(value = "implementation-parameters")
    @XmlElement(name = "implementation-parameters")
    public ImplementationParameters getImplementationParameters() {
        return this.implementationParameters;
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
        final String resourceFolderPath = Messages.getString("ResourceList.rootFolder") + File.separator //$NON-NLS-1$
                + Messages.getString("ResourceList.ResourceFolder"); //$NON-NLS-1$
        return resourceFolderPath + File.separator + this.getOwnerId() + File.separator + this.getFileName();
    }

    @XmlElement(name = "resourceDescription")
    public List<ResourceDescription> getResourceDescriptions() {
        return this.resourceDescriptions;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public void setGroupAccessRights(final GroupAccessRights groupAccessRights) {
        this.groupAccessRights = groupAccessRights;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public void setImplementationParameters(final ImplementationParameters implementationParameters) {
        this.implementationParameters = implementationParameters;
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
