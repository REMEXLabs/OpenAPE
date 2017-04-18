package org.openape.api;

import javax.ws.rs.DefaultValue;

/**
 * A database object that is owned by a user and can be public (viewable by other users).
 */

public class Resource extends DatabaseObject {

    private static final long serialVersionUID = 4077081454613480332L;

    private String owner;

    @DefaultValue("false")
    private boolean isPublic;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

}
