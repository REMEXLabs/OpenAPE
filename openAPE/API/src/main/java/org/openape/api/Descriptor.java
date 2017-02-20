package org.openape.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class Descriptor implements Serializable {
    private static final long serialVersionUID = 567050163908521098L;

    private String name;
    private String value;

    public Descriptor(String name, String value) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
