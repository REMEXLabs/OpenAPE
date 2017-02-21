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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonIgnore;

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
        for (final Preference basePreference : base.getPreferences()) {
            // Match checks if for each preference in this there is one in
            // compare.
            boolean match = false;
            for (final Preference comparePreference : compare.getPreferences()) {
                // if key fits check if value fits.
                if (basePreference.getKey().equals(comparePreference.getKey())) {
                    match = true;
                    if (!basePreference.equals(comparePreference)) {
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

    private String id;
    private String name;

    private List<Preference> preferences = new ArrayList<Preference>();

    /**
     * Default Constructor needed for json object mapper.
     */
    public Context() {

    }

    public Context(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void addPreference(String key, String value) {
        final Preference newPreference = new Preference(key, value);
        this.preferences.add(newPreference);
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
        // check if context attributes are equal.
        if (!(this.getId().equals(compare.getId()) && this.getName().equals(compare.getName()))) {
            return false;
        } else {
            // check if preferences are equal
            return (Context.hasContextTheSamePreferences(compare, this) && Context
                    .hasContextTheSamePreferences(this, compare));
        }
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return this.id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "preference")
    public List<Preference> getPreferences() {
        return this.preferences;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

}
