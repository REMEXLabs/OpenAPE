package org.openape.api;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class UserContextList {
    private int totalContexts;

    @XmlElement("user-context-uris")
    private List<URI> userContextUris;
}
