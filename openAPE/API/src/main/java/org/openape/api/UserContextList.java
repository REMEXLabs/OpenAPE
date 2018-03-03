package org.openape.api;

import java.net.URI;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.openape.api.usercontext.UserContext;
import com.fasterxml.jackson.annotation.JsonSetter;

@XmlRootElement(name = "response")
public class UserContextList extends ContextList<UserContext> {
    public UserContextList() {
        super();
    }

    public UserContextList(List<UserContext> contexts, String url) {
        super(contexts, url, "user-context-uri");
    }

    public List<URI> getUserContextUris() {
        return getContextUris();
    }

    @JsonSetter("user-context-uris")
    public void setUserContextUris(final List<URI> userContextUris) {
        setContextUris(userContextUris);
    }

}