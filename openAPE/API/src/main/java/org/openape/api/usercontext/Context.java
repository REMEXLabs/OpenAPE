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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Context implements Serializable {
    private static final long serialVersionUID = -8602234372848554234L;

    /**
     * Checks if a compare context has the same preferences as a base context.
     * Does return true if it has MORE preferences.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same preferences as base, false if not.
     */
    private static boolean hasContextTheSamePreferences(Context base, Context compare) {
        final Set<String> baseKeySet = base.getPreferences().keySet();
        final Set<String> compareKeySet = compare.getPreferences().keySet();
        for (final String baseKey : baseKeySet) {
            // Match checks if for each preference in this there is one in
            // compare.
            boolean match = false;
            for (final String compareKey : compareKeySet) {
                // if key fits check if value fits.
                if (baseKey.equals(compareKey)) {
                    match = true;
                    if (!base.getPreferences().get(baseKey)
                            .equals(compare.getPreferences().get(compareKey))) {
                        return false;
                    }
                }
            }
            // no matching preference
            if (match != true) {
                return false;
            }
        }
        return true;
    }

    private String name;

    private Map<String, String> preferences = new HashMap<String, String>();

    /**
     * Default Constructor needed for json object mapper.
     */
    public Context() {

    }

    public Context(String name) {
        this.name = name;
    }

    public void addPreference(String key, String value) {
        this.preferences.put(key, value);
    }

    /**
     * Compares the values of key and value and preferences and returns true if
     * there are equal, false else.
     *
     * @param compare
     *            context to compare with.
     * @return true if compare context has the same values in key and value and
     *         the same preferences, false else.
     */
    @JsonIgnore
    public boolean equals(Context compare) {
        // check if preferences are equal
        return (Context.hasContextTheSamePreferences(compare, this) && Context
                .hasContextTheSamePreferences(this, compare));
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "preference")
    public Map<String, String> getPreferences() {
        return this.preferences;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

}
