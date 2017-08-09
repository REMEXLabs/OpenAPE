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

package org.openape.api.usercontext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    private static boolean hasContextTheSamePreferences(final Context base, final Context compare) {
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    // Ignores field if null.
    private List<Condition> conditions = null;
    private String name;

    private Map<String, String> preferences = new HashMap<String, String>();

    /**
     * Default Constructor needed for json object mapper.
     */
    public Context() {

    }

    public Context(final String name) {
        this.name = name;
    }

    public void addCondition(final Condition condition) {
        if (this.getConditions() == null) {
            this.setConditions(new ArrayList<Condition>());
        }
        this.conditions.add(condition);
    }

    public void addPreference(final String key, final String value) {
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
    public boolean equals(final Context compare) {
        // check if preferences are equal
        return (Context.hasContextTheSamePreferences(compare, this) && Context
                .hasContextTheSamePreferences(this, compare));
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "preference")
    public Map<String, String> getPreferences() {
        return this.preferences;
    }

    public void setConditions(final List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPreferences(final Map<String, String> preferences) {
        this.preferences = preferences;
    }

}
