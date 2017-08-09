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

package org.openape.api;

import javax.ws.rs.DefaultValue;

/**
 * A database object that is owned by a user and can be public (viewable by
 * other users).
 */

public class Resource extends DatabaseObject {

    private static final long serialVersionUID = 4077081454613480332L;

    private String owner;

    @DefaultValue("false")
    private boolean isPublic;

    public String getOwner() {
        return this.owner;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setPublic(final boolean aPublic) {
        this.isPublic = aPublic;
    }

}
