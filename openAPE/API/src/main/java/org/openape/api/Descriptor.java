package org.openape.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.taskcontext.TaskContext;

/**
 * Descriptor Object used by {@link ResourceDescription}, {@link TaskContext},
 * {@link EquipmentContext}, {@link EnvironmentContext}
 */
public class Descriptor implements Serializable {
    private static final long serialVersionUID = 567050163908521098L;

    private String name;
    private String value;

    public Descriptor(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public Descriptor() {
	
    }

    /**
     * Compares the values of name and value and returns true if there are
     * equal, false else.
     *
     * @param compare
     *            descriptor to compare with.
     * @return true if compare descriptor has the same values in name and value,
     *         false else.
     */
    @JsonIgnore
    public boolean equals(Descriptor compare) {
        if (this.getName().equals(compare.getName()) && this.getValue().equals(compare.getValue())) {
            return true;
        } else {
            return false;
        }
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
