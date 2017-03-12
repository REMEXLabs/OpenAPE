package org.openape.api.usercontext;

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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Preference implements Serializable {
    private static final long serialVersionUID = -8359653185563684514L;

    private String key;
    private String value;

    /**
     * Default Constructor needed for json object mapper.
     */
    public Preference() {

    }

    public Preference(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Compares the values of key and value and returns true if there are equal,
     * false else.
     *
     * @param compare
     *            preference to compare with.
     * @return true if compare preference has the same values in key and value,
     *         false else.
     */
    @JsonIgnore
    public boolean equals(Preference compare) {
        if (this.getKey().equals(compare.getKey()) && this.getValue().equals(compare.getValue())) {
            return true;
        } else {
            return false;
        }
    }

    @XmlAttribute(name = "key")
    public String getKey() {
        return this.key;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
