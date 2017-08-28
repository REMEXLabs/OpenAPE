package org.openape.api;

import java.net.URI;
import java.net.URISyntaxException;
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
        for (UserContext userContext : contexts) {
            try {
                this.userContextUris.add(new URI(url + userContext.getId()));
            } catch (final URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.totalContexts = this.userContextUris.size();
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
