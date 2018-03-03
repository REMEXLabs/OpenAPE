package org.openape.api;

import java.net.URI;
import java.util.List;

import org.openape.api.environmentcontext.EnvironmentContext;

import com.fasterxml.jackson.annotation.JsonSetter;

public class EnvironmentContextList extends ContextList<EnvironmentContext> {

    public EnvironmentContextList(List<EnvironmentContext> contexts, String url) {
        super(contexts, url, "environment-context-uri");
    }
    
    public EnvironmentContextList() {
        super();
    }
    
    public List<URI> getEnvironmentContextUris() {
        return getContextUris();
    }

    @JsonSetter("environment-context-uris")
    public void setEnvironmentContextUris(final List<URI> environmentContextUris) {
        setContextUris(environmentContextUris);
    }

    
}
