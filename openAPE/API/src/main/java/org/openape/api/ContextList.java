package org.openape.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.openape.api.usercontext.UserContext;

public abstract class ContextList<T > {

    protected int totalContexts;
    
    private static String contextTypeUri;
    
    @XmlElement(name = ""+ contextTypeUri)
    protected List<URI> contextUris;

    
    
    
    public int getTotalContexts() {
        return this.totalContexts;
    }

    public void setTotalContexts(final int totalContexts) {
        this.totalContexts = totalContexts;
    }



    public ContextList(final List<T> contexts, final String url, String ContextTypeUri) {
        this.contextUris = new LinkedList<URI>();
        for (T userContext : contexts) {
            try {
                this.contextUris.add(new URI(url + userContext.getId()));
            } catch (final URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.totalContexts = this.contextUris.size();
        this.contextTypeUri = contextTypeUri;
    }

}
