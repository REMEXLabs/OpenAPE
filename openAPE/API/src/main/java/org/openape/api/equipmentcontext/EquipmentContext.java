package org.openape.api.equipmentcontext;

import org.openape.api.DatabaseObject;

public class EquipmentContext extends DatabaseObject {
    private static final long serialVersionUID = 4810176872836108065L;

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
