package org.openape.api.usercontext;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserContext {

    private List<Context> contexts;

    public UserContext() {
        contexts = new ArrayList<Context>();
    }

    @XmlElement(name = "context")
    public List<Context> getContexts() {
        return this.contexts;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }

    public void addContext(Context c) {
        this.contexts.add(c);

    }

    /**
     * check if a given object is a valid user context. Checks if at least one
     * context is available. TODO implement.
     * 
     * @return true if a context is found, false if its empty or attributes are
     *         missing.
     */
    public boolean isValid() {
        if (this.getContexts().isEmpty()) {
            return false;
        }
        Context firstContext = this.getContexts().get(0);
        if (firstContext.getPreferences().isEmpty() || firstContext.getName() == "" || firstContext.getName() == null) {
            return false;
        }
        return true;
    }
}
