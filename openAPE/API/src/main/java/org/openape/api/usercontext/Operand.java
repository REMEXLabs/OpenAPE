package org.openape.api.usercontext;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Operand implements Serializable {
    private static final long serialVersionUID = 5069543268247907871L;
    private String value;

    public Operand() {
        super();
    }

    public Operand(final String value) {
        super();
        this.value = value;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
