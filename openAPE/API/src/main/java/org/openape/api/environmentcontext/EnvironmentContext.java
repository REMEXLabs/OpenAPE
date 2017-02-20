package org.openape.api.environmentcontext;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.DatabaseObject;
import org.openape.api.Property;

/**
 * Environment context object defined in 7.5.1
 */
@XmlRootElement
public class EnvironmentContext extends DatabaseObject {
    private static final long serialVersionUID = -1706959529432920842L;

    private List<Property> propertys = new ArrayList<Property>();

    public EnvironmentContext() {
        this.propertys = new ArrayList<Property>();
    }

    public void addProperty(Property property) {
        this.propertys.add(property);
    }

    @XmlElement(name = "property")
    public List<Property> getPropertys() {
        return this.propertys;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public void setPropertys(List<Property> propertys) {
        this.propertys = propertys;
    }

}
