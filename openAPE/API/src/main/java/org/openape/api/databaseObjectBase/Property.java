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
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import org.openape.api.contexts.KeyValuePair;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.taskcontext.TaskContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Property Object used by {@link ResourceDescription}, {@link TaskContext},
 * {@link EquipmentContext}, {@link EnvironmentContext}
 */
public class Property  implements Serializable, KeyValuePair {
    private static final long serialVersionUID = -6041175371845997239L;

    /**
     * Checks if a compare property has the same descriptors as a base property.
     * Does return true if it has MORE preferences.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same preferences as base, false if not.
     */
    boolean hasPropertyTheSameDescriptors( final Property compare) {

    	System.out.println("neu aufgerufen");
    	List<Descriptor> compareDescriptors = compare.getDescriptors();
	if (this.descriptors.size() != compareDescriptors.size() ) {
    		System.out.println("direkt");
    		return false;
    	}
    	if (this.descriptors.size() == 0) {
    		return true;
    	}

        for (Descriptor baseDescriptor : this.descriptors) {
            // Match checks if for each descriptor in this there is one in         compare.
            boolean match = false;

            for (Descriptor compareDescriptor : compareDescriptors) {
                // if key fits check if value fits.
                if (baseDescriptor.getName().equals(compareDescriptor.getName() )  && baseDescriptor.getValue().equals(compareDescriptor.getValue())) {

                	match = true;
                    break;
                    }
                }

            // no matching preference
            if (match == false) {

            	return false;
            }
        }

        return true;
    }

    private String name;
    private Object value;

    private List<Descriptor> descriptors = new ArrayList<Descriptor>();

    public Property() {

    }

    public Property(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public Property(String name, boolean b) {
		this.name = name;
		setValue(b);
	}

	public Property(String name, int i) {
		this.name = name;
		setValue(i);
	}

	public Property(String name, double d) {
		this.name = name;
		setValue(d);
	}

	public void addDescriptor(final Descriptor descriptor) {
        this.descriptors.add(descriptor);
    }

    /**
     * Compares the values of name and value and descriptors and returns true if
     * there are equal, false else.
     *
     * @param compare
     *            property to compare with.
     * @return true if compare property has the same values in key and value and
     *         the same descriptors, false else.
     */
    @JsonIgnore
    public boolean equals(final Object o) {
        // check if property attributes are equal.
        if (o == null || !(o instanceof Property)) {
        	return false;
        }
Property compare = (Property)o;
        try {
        System.out.println(this.getName());
        System.out.println(compare.getName() );

        System.out.println(this.getValue()	);
        System.out.println(compare.getValue() );
        	if (!(this.getName().equals(compare.getName()) && this.getValue().equals(compare.getValue()))) {
                    	System.out.println("falscher Name");
        	return false;
        } else {
            // check if descriptors are equal
            return this.hasPropertyTheSameDescriptors((Property)compare);

        }
        } catch (NullPointerException e) {


        	return false;
		}

    }

    @XmlElement(name = "descriptor")
    public List<Descriptor> getDescriptors() {
        return this.descriptors;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlAttribute(name = "value")
    public String getValueAsString() {
    	return value.toString();
    }

    public void setValueAsString(String value) {
    	this. value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setDescriptors(final List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }


    public void setValue(final String value) {
        this.value = value;
    }

	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public void setValue(boolean value) {
		this.value = new Boolean(value);

	}

	@Override
	public void setValue(double value) {
		this.value = new Double(value);

	}

	public void setValue(int i) {
		value = new Integer(i);
	}
}
