package org.openape.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Property implements Serializable {
    private static final long serialVersionUID = -6041175371845997239L;

    private String name;
    private String value;
    private List<Descriptor> descriptors = new ArrayList<Descriptor>();

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void addDescriptor(String name, String value) {
        Descriptor newDescriptor = new Descriptor(name, value);
        this.descriptors.add(newDescriptor);
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return this.value;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "descriptor")
    public List<Descriptor> getDescriptors() {
        return this.descriptors;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescriptors(List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

}
