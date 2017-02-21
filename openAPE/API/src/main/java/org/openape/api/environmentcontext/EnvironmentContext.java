package org.openape.api.environmentcontext;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openape.api.DatabaseObject;
import org.openape.api.Property;

/**
 * Environment context object defined in 7.5.1
 */
@XmlRootElement
public class EnvironmentContext extends DatabaseObject {
    private static final long serialVersionUID = -1706959529432920842L;

    /**
     * Checks if a compare environment context has the same properties as a base
     * context. Does return true if it has MORE contexts.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same properties as base, false if not.
     */
    private static boolean hasEnvironmentContextTheSameProperties(EnvironmentContext base,
            EnvironmentContext compare) {
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

    public EnvironmentContext() {
        this.propertys = new ArrayList<Property>();
    }

    public void addProperty(Property property) {
        this.propertys.add(property);
    }

    /**
     * Checks if environment contexts are equal in field values.
     *
     * @param compare
     *            environment context to compare with.
     * @return true if contexts are equal in field values, false else.
     */
    @JsonIgnore
    public boolean equals(EnvironmentContext compare) {
        return (EnvironmentContext.hasEnvironmentContextTheSameProperties(compare, this) && EnvironmentContext
                .hasEnvironmentContextTheSameProperties(this, compare));

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
