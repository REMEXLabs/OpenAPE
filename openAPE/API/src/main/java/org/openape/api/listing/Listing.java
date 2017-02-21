package org.openape.api.listing;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openape.api.DatabaseObject;
import org.openape.api.resourceDescription.ResourceDescription;

/**
 * Listing object defined in A.8.2
 */
@XmlRootElement
public class Listing extends DatabaseObject {
    private static final long serialVersionUID = 6662310079884980939L;

    List<String> userContextUris = new ArrayList<String>();
    List<String> taskContextUris = new ArrayList<String>();
    List<String> equipmentContextUris = new ArrayList<String>();
    List<String> environmantContextUris = new ArrayList<String>();
    ResourceDescription resourceDescriptionQurey = new ResourceDescription();

    @XmlElement(name = "environment-context-uri")
    public List<String> getEnvironmantContextUris() {
        return this.environmantContextUris;
    }

    @XmlElement(name = "equipment-context-uri")
    public List<String> getEquipmentContextUris() {
        return this.equipmentContextUris;
    }

    @XmlElement(name = "resource-description")
    public ResourceDescription getResourceDescriptionQurey() {
        return this.resourceDescriptionQurey;
    }

    @XmlElement(name = "task-context-uri")
    public List<String> getTaskContextUris() {
        return this.taskContextUris;
    }

    @XmlElement(name = "user-context-uri")
    public List<String> getUserContextUris() {
        return this.userContextUris;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    public void setEnvironmantContextUris(List<String> environmantContextUris) {
        this.environmantContextUris = environmantContextUris;
    }

    public void setEquipmentContextUris(List<String> equipmentContextUris) {
        this.equipmentContextUris = equipmentContextUris;
    }

    public void setResourceDescriptionQurey(ResourceDescription resourceDescriptionQurey) {
        this.resourceDescriptionQurey = resourceDescriptionQurey;
    }

    public void setTaskContextUris(List<String> taskContextUris) {
        this.taskContextUris = taskContextUris;
    }

    public void setUserContextUris(List<String> userContextUris) {
        this.userContextUris = userContextUris;
    }
}
