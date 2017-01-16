package org.openape.api.listing;

import javax.xml.bind.annotation.XmlAttribute;

import org.openape.api.DatabaseObject;

public class Listing extends DatabaseObject {
    private static final long serialVersionUID = 6662310079884980939L;

    private String query = "";

    @Override
    public boolean isValid() {
        return true;
    }

    public String getQuery() {
        return query;
    }

    @XmlAttribute(name = "query")
    public void setQuery(String query) {
        this.query = query;
    }
}
