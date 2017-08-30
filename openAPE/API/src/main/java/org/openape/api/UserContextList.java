package org.openape.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.usercontext.UserContext;

public class UserContextList extends ContextList<UserContextList> {

    public UserContextList(List<UserContext> contexts, String url) {
        super(contexts, url, "user-context-type");
    }
    
    public List<URI> getUserContextUris() {
        return this.contextUris;
    }

    public void setUserContextUris(final List<URI> userContextUris) {
        this.contextUris = userContextUris;
    }
}
