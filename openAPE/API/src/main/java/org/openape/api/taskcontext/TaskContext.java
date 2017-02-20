package org.openape.api.taskcontext;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.DatabaseObject;
import org.openape.api.Property;

/**
 * Task context object defined in 7.3.1
 */
@XmlRootElement
public class TaskContext extends DatabaseObject {
    private static final long serialVersionUID = 3325722856059287182L;

    private List<Property> propertys = new ArrayList<Property>();

    public TaskContext() {
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
