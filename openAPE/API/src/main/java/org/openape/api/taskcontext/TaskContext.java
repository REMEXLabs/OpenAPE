package org.openape.api.taskcontext;

import org.openape.api.DatabaseObject;

public class TaskContext extends DatabaseObject {
    private static final long serialVersionUID = 3325722856059287182L;

    /**
     * check if a given object is a valid user context. Checks if at least one
     * context is available. TODO implement.
     *
     * @return true if a context is found, false if its empty or attributes are
     *         missing.
     */
    public boolean isValid() {
        return false;
    }
}
