package org.openape.api.usercontext;

import javax.xml.bind.annotation.*;

public class Preference {

    private String key;
    private String value;

    @XmlAttribute(name = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Preference(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
