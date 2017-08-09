package org.openape.api;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.openape.api.usercontext.UserContext;

public class UserContextList {

    private int totalContexts;

    @XmlElement(name = "user-context-uris")
    private List<URI> userContextUris;

    public UserContextList(final List<UserContext> contexts, final String url) {
        this.userContextUris = new LinkedList<URI>();
        for (final UserContext uc : contexts) {
            this.userContextUris.add(new URI(url + uc.getId));
        }
    }

    public int getTotalContexts() {
        return this.totalContexts;
    }

    public List<URI> getUserContextUris() {
        return this.userContextUris;
    }

    public void setTotalContexts(final int totalContexts) {
        this.totalContexts = totalContexts;
    }

    public void setUserContextUris(final List<URI> userContextUris) {
        this.userContextUris = userContextUris;
    }
}
