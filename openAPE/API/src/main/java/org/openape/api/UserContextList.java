package org.openape.api;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.openape.api.usercontext.UserContext;

public class UserContextList {
    
    public UserContextList(List<UserContext> contexts, String url) {
    this.userContextUris= new LinkedList<URI>();    
    for (UserContext uc: contexts){
        userContextUris.add(new URI(      url + uc.getId));
    }
    }

    private int totalContexts;

    @XmlElement(name="user-context-uris")
    private List<URI> userContextUris;

    public int getTotalContexts() {
        return totalContexts;
    }

    public void setTotalContexts(int totalContexts) {
        this.totalContexts = totalContexts;
    }

    public List<URI> getUserContextUris() {
        return userContextUris;
    }

    public void setUserContextUris(List<URI> userContextUris) {
        this.userContextUris = userContextUris;
    }
}
