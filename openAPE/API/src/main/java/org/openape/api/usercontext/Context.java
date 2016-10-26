package org.openape.api.usercontext;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;
import java.util.List;

public class Context {

    private String id;
    private String name;
    private List<Preference> preferences = new ArrayList<Preference>();

    public Context(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public void addPreference(String key, String value) {
        Preference newPreference = new Preference(key, value);
        preferences.add(newPreference);
    }

}
