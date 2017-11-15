package org.openape.api.databaseObjectBase;

import javax.xml.bind.annotation.XmlAttribute;

public class Descriptor {
    private String name;
    private String value;

    public Descriptor() {
        super();
    }

    public Descriptor(final String name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setValue(final String value) {
        this.value = value;
    }

}
