package org.openape.api.resourceDescription;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openape.api.DatabaseObject;
import org.openape.api.Property;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Resource description object defined in 7.7.1
 */
@XmlRootElement
public class ResourceDescription extends DatabaseObject {
    private static final long serialVersionUID = -3341210067495347309L;

    /**
     * Checks if a compare resource description has the same properties as a
     * base context. Does return true if it has MORE contexts.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same properties as base, false if not.
     */
    private static boolean hasResourceDescriptionTheSameProperties(ResourceDescription base,
            ResourceDescription compare) {
        for (final Property baseProperty : base.getPropertys()) {
            // Match checks if for each property in this there is one in
            // compare.
            boolean match = false;
            for (final Property compareContext : compare.getPropertys()) {
                // if id fits check if property fits.
                if (baseProperty.getName().equals(compareContext.getName())) {
                    if (baseProperty.equals(compareContext)) {
                        match = true;
                    }
                }
            }
            // no matching property
            if (match != true) {
                return false;
            }
        }
        return true;
    }

    private List<Property> propertys = new ArrayList<Property>();

    public ResourceDescription() {
        this.propertys = new ArrayList<Property>();
    }

    public void addProperty(Property property) {
        this.propertys.add(property);

    }

    /**
     * Checks if resource descriptions are equal in field values.
     *
     * @param compare
     *            resource description to compare with.
     * @return true if contexts are equal in field values, false else.
     */
    @JsonIgnore
    public boolean equals(ResourceDescription compare) {
        return (ResourceDescription.hasResourceDescriptionTheSameProperties(compare, this) && ResourceDescription
                .hasResourceDescriptionTheSameProperties(this, compare));

    }

    @XmlElement(name = "property")
    public List<Property> getPropertys() {
        return this.propertys;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    public void setPropertys(List<Property> propertys) {
        this.propertys = propertys;
    }

}
