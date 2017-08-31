package org.openape.api;

import java.util.List;

import org.openape.api.environmentcontext.EnvironmentContext;

public class EnvironmentContextList extends ContextList<EnvironmentContext> {

    public EnvironmentContextList(List<EnvironmentContext> contexts, String url) {
        super(contexts, url, "environment-context-list");
    }
    
}
