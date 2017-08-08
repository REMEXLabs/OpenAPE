package org.openape.api.group;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Collection of all {@link GroupAccessRight} objects of a resource.
 */
public class GroupAccessRights {
    List<GroupAccessRight> groupAccessRights = new ArrayList<GroupAccessRight>();

    public GroupAccessRights() {
        super();
    }

    public GroupAccessRights(final List<GroupAccessRight> groupAccessRights) {
        super();
        this.groupAccessRights = groupAccessRights;
    }

    @JsonIgnore
    public void addGroupAccessRight(final GroupAccessRight groupAccessRight) {
        this.getGroupAccessRights().add(groupAccessRight);
    }

    @XmlElement(name = "groupAccessRight")
    public List<GroupAccessRight> getGroupAccessRights() {
        return this.groupAccessRights;
    }

    public void setGroupAccessRights(final List<GroupAccessRight> groupAccessRights) {
        this.groupAccessRights = groupAccessRights;
    }

}
