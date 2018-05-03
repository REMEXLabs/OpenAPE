package org.openape.api.usercontext;

import javax.xml.bind.annotation.XmlAttribute;

public class Preference {
    private String key;
    private String value;

    public Preference() {
        super();
    }

    public Preference(final String key, final String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @XmlAttribute(name = "key")
    public String getKey() {
        return this.key;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setValue(final String value) {
        this.value = value;
    }

	public void setValue(boolean b) {
		System.out.println(b);;
		
	}

	public void setValue(int i) {
		System.out.println(i);
		
	}

	public void setValue(double d) {
		System.out.println(d);
		
	}

}
