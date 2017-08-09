package org.openape.api.groups;

public class GroupRequest {

    private String groupname;
    private String description;
    private String entryRequirements;

    public String getDescription() {
        return this.description;
    }

    public String getEntryRequirements() {
        return this.entryRequirements;
    }

    public String getGroupname() {
        return this.groupname;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEntryRequirements(final String entryRequirements) {
        this.entryRequirements = entryRequirements;
    }

    public void setGroupname(final String groupname) {
        this.groupname = groupname;
    }
}
