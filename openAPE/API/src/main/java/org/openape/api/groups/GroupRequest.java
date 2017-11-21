package org.openape.api.groups;

public class GroupRequest {

    private String groupname;
    private String description;
    private String entryRequirements;
    private boolean openAccess;

    public String getDescription() {
        return this.description;
    }

    public String getEntryRequirements() {
        return this.entryRequirements;
    }

    public String getGroupname() {
        return this.groupname;
    }

    public boolean isOpenAccess() {
        return this.openAccess;
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

    public void setOpenAccess(final boolean openAccess) {
        this.openAccess = openAccess;
    }
}
