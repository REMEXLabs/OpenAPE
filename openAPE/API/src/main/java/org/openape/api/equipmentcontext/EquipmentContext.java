package org.openape.api.equipmentcontext;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.DatabaseObject;
import org.openape.api.Property;

/**
 * Equipment context object defined in 7.4.1
 */
@XmlRootElement
public class EquipmentContext extends DatabaseObject {
    private static final long serialVersionUID = 4810176872836108065L;

    private List<Property> propertys = new ArrayList<Property>();

    public EquipmentContext() {
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
