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

package org.openape.api.listing;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.resourceDescription.ResourceDescription;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Listing object defined in A.8.2
 */
@XmlRootElement
public class Listing extends DatabaseObject {
    private static final long serialVersionUID = 6662310079884980939L;

    private List<String> userContextUris = new ArrayList<String>();
    private List<String> taskContextUris = new ArrayList<String>();
    private List<String> equipmentContextUris = new ArrayList<String>();
    private List<String> environmantContextUris = new ArrayList<String>();
    private ResourceDescription resourceDescriptionQurey = new ResourceDescription();

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

    public void setEnvironmantContextUris(final List<String> environmantContextUris) {
        this.environmantContextUris = environmantContextUris;
    }

    public void setEquipmentContextUris(final List<String> equipmentContextUris) {
        this.equipmentContextUris = equipmentContextUris;
    }

    public void setResourceDescriptionQurey(final ResourceDescription resourceDescriptionQurey) {
        this.resourceDescriptionQurey = resourceDescriptionQurey;
    }

    public void setTaskContextUris(final List<String> taskContextUris) {
        this.taskContextUris = taskContextUris;
    }

    public void setUserContextUris(final List<String> userContextUris) {
        this.userContextUris = userContextUris;
    }
}
