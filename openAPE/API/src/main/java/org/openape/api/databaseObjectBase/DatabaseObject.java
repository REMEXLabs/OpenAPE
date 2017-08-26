/**
 Copyright 2016 Hochschule der Medien - Stuttgart Media University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.openape.api.databaseObjectBase;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract class used to identify object types that can be stored in the
 * database.
 */
public abstract class DatabaseObject implements Serializable {
    private static final long serialVersionUID = -404247926481623440L;

    /**
     * The id (field "_id" in MongoDB) of the object, which the database
     * assigned to it.
     */
    @JsonIgnore
    private String id;

    /**
     * The id of the object, which the database assigned to it.
     *
     * @return id of the object or null if it is not already stored in the
     *         database.
     */
    public String getId() {
        return this.id;
    }

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

    /**
     * Setter for the id.
     *
     * @param id
     *            the id of the object. It must not be null!
     */
    public void setId(final String id) {
        this.id = id;
    }

}
