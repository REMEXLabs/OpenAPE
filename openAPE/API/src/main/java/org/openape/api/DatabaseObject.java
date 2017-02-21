package org.openape.api;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Abstract class used to identify object types that can be stored in the
 * database.
 */
public abstract class DatabaseObject implements Serializable {
    private static final long serialVersionUID = -404247926481623440L;

    /**
     * check if a given object is a valid object of its type. May be stub for
     * future implementation and always return true;
     *
     * @return true if valid.
     */
    @JsonIgnore
    public boolean isValid() {
        return false;
    }

}
