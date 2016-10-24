package org.openape.usercontext;

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

}
