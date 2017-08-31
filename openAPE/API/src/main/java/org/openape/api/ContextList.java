package org.openape.api;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.usercontext.UserContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class ContextList<T extends DatabaseObject > {

    protected int totalContexts;
    
    private String contextTypeUri;
    
    @XmlElement(name =  "context-type-uri" )
    protected List<URI> contextUris;
    
    public List<URI> getContextUris() {
        return contextUris;
    }

    public void setContextUris(List<URI> contextUris) {
        this.contextUris = contextUris;
    }

    public int getTotalContexts() {
        return this.totalContexts;
    }

    public void setTotalContexts(final int totalContexts) {
        this.totalContexts = totalContexts;
    }
    
    /**
     * Generate the xml representation from the object used for the front end.
     *
     * @return xml string.
     */
    @JsonIgnore
    public String getXML() throws IOException {
        String xmlString = null;
        try {
            final JAXBContext context = JAXBContext.newInstance(UserContext.class);
            final Marshaller marshaller = context.createMarshaller();
            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);
            xmlString = stringWriter.toString();
            xmlString.replace("context-type-uri", contextTypeUri);
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        }
        return xmlString;
    }

    /**
     * Generates json string from Object.
     * @throws ClassCastException
     * @throws IOException
     *             , JsonProcessingException
     */
    private String getJson() throws ClassCastException, IOException {
        final JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);
        
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this).replace("context-type-uri", contextTypeUri);
    }

    public ContextList(final List<T> contexts, final String url, final String ContextTypeUri) {
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
