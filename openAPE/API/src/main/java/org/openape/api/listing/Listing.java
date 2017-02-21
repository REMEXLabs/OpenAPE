package org.openape.api.listing;

import javax.xml.bind.annotation.XmlAttribute;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openape.api.DatabaseObject;

public class Listing extends DatabaseObject {
    private static final long serialVersionUID = 6662310079884980939L;

    private String query = "";

    public String getQuery() {
        return this.query;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    @XmlAttribute(name = "query")
    public void setQuery(String query) {
        this.query = query;
    }
}
