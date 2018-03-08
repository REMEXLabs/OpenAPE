package org.openape.api;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement(name = "response")
@XmlType (propOrder={"totalContexts", "contextUris"})
public abstract class ContextList<T extends DatabaseObject > {

    private Logger logger = LoggerFactory.getLogger(ContextList.class); 
    private int totalContexts = 0;  

    private String contextTypeUri;
    
    @JsonIgnore
    private List<URI> contextUris;
    

    @XmlElementWrapper(name = "context-uris")
    @XmlElement(name =  "uri")
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
            final JAXBContext context = JAXBContext.newInstance(UserContextList.class);
            final Marshaller marshaller = context.createMarshaller();
            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);
            xmlString = stringWriter.toString();
            logger.info("Org. String:" + xmlString );
            
            xmlString = xmlString.replace("context-uri", contextTypeUri);

            

            
        } catch (final Exception e) {
            logger.warn(e.toString());
            
            throw new IOException(e.getMessage());
        }
        logger.info("blabla");
        return xmlString;
    }

    /**
     * Generates json string from Object.
     * @param uc 
     * @throws ClassCastException
     * @throws IOException
     *             , JsonProcessingException
     */
    @JsonIgnore
    public String getJson() throws ClassCastException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
                final String jsonString = mapper.writeValueAsString(this).replace("contextUri", contextTypeUri);
        return jsonString;
    }

    public ContextList(){

    }
    
    public ContextList(final List<T> contexts, final String url, final String contextTypeUri) {
        this.contextUris = new LinkedList<URI>();
                for (T context : contexts) {
            try {
                this.contextUris.add(new URI(url + "/"+ context.getId()));
            } catch (final URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.totalContexts = this.contextUris.size();
        this.contextTypeUri = contextTypeUri;
        
    }

}
