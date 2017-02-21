package org.openape.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.taskcontext.TaskContext;

/**
 * Property Object used by {@link ResourceDescription}, {@link TaskContext},
 * {@link EquipmentContext}, {@link EnvironmentContext}
 */
public class Property implements Serializable {
    private static final long serialVersionUID = -6041175371845997239L;

    /**
     * Checks if a compare property has the same descriptors as a base property.
     * Does return true if it has MORE preferences.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same preferences as base, false if not.
     */
    private static boolean hasPropertyTheSameDescriptors(Property base, Property compare) {
        for (final Descriptor baseDescriptor : base.getDescriptors()) {
            // Match checks if for each descriptor in this there is one in
            // compare.
            boolean match = false;
            for (final Descriptor compareDescriptor : compare.getDescriptors()) {
                // if name fits check if value fits.
                if (baseDescriptor.getName().equals(compareDescriptor.getName())) {
                    if (baseDescriptor.equals(compareDescriptor)) {
                        match = true;
                    }
                }
            }
            // no matching property
            if (match != true) {
                return false;
            }
        }
        return true;
    }

    private String name;
    private String value;

    private List<Descriptor> descriptors = new ArrayList<Descriptor>();

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void addDescriptor(String name, String value) {
        final Descriptor newDescriptor = new Descriptor(name, value);
        this.descriptors.add(newDescriptor);
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
    public boolean equals(Property compare) {
        // check if property attributes are equal.
        if (!(this.getName().equals(compare.getName()) && this.getValue()
                .equals(compare.getValue()))) {
            return false;
        } else {
            // check if descriptors are equal
            return (Property.hasPropertyTheSameDescriptors(compare, this) && Property
                    .hasPropertyTheSameDescriptors(this, compare));
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
    public String getValue() {
        return this.value;
    }

    public void setDescriptors(List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
