package org.openape.api.contexts;

public interface KeyValuePair {
public void setName(String name);

public void setValue(boolean asBoolean);

public void setValue(String textValue);

public void setValue(double asDouble);

public void setValue(int i);
}
