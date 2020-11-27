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
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@XmlType(propOrder = { "name", "preferences", "conditions" })
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
        for (final Preference basePreference : base.getPreferences()) {
            // Match checks if for each preference in this there is one in
            // compare.
            boolean match = false;
            for (final Preference comparePreference : compare.getPreferences()) {
                // if key fits check if value fits.
                if (basePreference.getKey().equals(comparePreference.getKey())) {
                    match = true;
                    if (!basePreference.getValue().equals(comparePreference.getValue())) {
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
    private String id;
    private List<Preference> preferences = new ArrayList<Preference>();

    /**
     * Default Constructor needed for json object mapper.
     */
    public Context() {

    }

    public Context(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public void addCondition(final Condition condition) {
        if (this.getConditions() == null) {
            this.setConditions(new ArrayList<Condition>());
        }
        this.conditions.add(condition);
    }

    public void addPreference(final Preference preference) {
        this.preferences.add(preference);
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
        return (Context.hasContextTheSamePreferences(compare, this)
                && Context.hasContextTheSamePreferences(this, compare));
    }

    @XmlElement(name = "condition")
    public List<Condition> getConditions() {
        return this.conditions;
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

    /**
     * validates conditions recursively.
     *
     * @param operands
     */
    private void recursiveConditionValidation(final List<Operand> operands) {
        for (final Operand operand : operands) {
            if (operand instanceof Condition) {
                final Condition condition = (Condition) operand;
                condition.validate();
                final List<Operand> subOperands = condition.getOperands();
                this.recursiveConditionValidation(subOperands);
            }
        }
    }

    public void setConditions(final List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPreferences(final List<Preference> preferences) {
        this.preferences = preferences;
    }

    /**
     * validate all conditions
     *
     * @throws IllegalArgumentException
     */
    public void vaidate() throws IllegalArgumentException {
        if (this.conditions != null) {
            for (final Condition condition : this.conditions) {
                condition.validate();
                final List<Operand> operands = condition.getOperands();
                this.recursiveConditionValidation(operands);
            }
        }
    }
@JsonIgnore
	public Preference getPreference(String preferenceTerm) {

	for (Preference p : preferences) {

		if (p.getKey().equals(preferenceTerm) ) {
			return p;

		}
	}

		return null;
	}

}
