package org.openape.api.usercontext;

import javax.xml.bind.annotation.XmlAttribute;

import org.openape.api.contexts.KeyValuePair;

public class Preference implements KeyValuePair {
	private String key;
	private Object value;

	public Preference() {
		super();
	}

	public Preference(final String key, final String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public Preference(String key, boolean b) {
		this.key = key;
		setValue(b);
	}

	public Preference(String key, double d) {
		this.key = key;
		setValue(d);
	}
	
	public Preference(String key, int i) {
		this.key = key;
		setValue(i);
	}


	@XmlAttribute(name = "key")
	public String getKey() {
		return this.key;
	}

	@XmlAttribute(name = "value")
	public Object getValue() {
		return this.value;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public void setValue(boolean b) {

		value = new Boolean(b);
	}

	public void setValue(int i) {

		value = new Integer(i);
		System.out.println("int");
	}

	public void setValue(double d) {
		value = new Double(d);
System.out.println("double");
	}

	@Override
	public void setName(String name) {
		setKey(name);
		
	}

}
