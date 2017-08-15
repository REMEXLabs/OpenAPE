/**
 Copyright 2016 Hochschule der Medien - Stuttgart Media University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.openape.api.usercontext;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openape.api.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * User context object defined in 7.2.1
 */
@XmlRootElement(name = "user-context")
public class UserContext extends Resource {
    private static final long serialVersionUID = 5891055316807633786L;

    /**
     * Generate the user context from the json string used in the the front end.
     *
     * @return user context object.
     */
    @JsonIgnore
    public static UserContext getObjectFromJson(final String json) throws IllegalArgumentException {
        UserContext userContext = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode rootNode = mapper.readTree(json);
            // TODO manipulate tree
            userContext = mapper.treeToValue(rootNode, UserContext.class);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return userContext;
    }

    /**
     * Generate the user context from the xml string used in the the front end.
     *
     * @return user context object.
     */
    @JsonIgnore
    public static UserContext getObjectFromXml(final String xml) throws IllegalArgumentException {
        UserContext userContext = null;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false); // we will use schema instead of DTD
            factory.setNamespaceAware(true);
            final SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // get schema file from resource folder
            final URL url = UserContext.class.getClassLoader().getResource("UserContextSchema.xsd");
            final File file = new File(url.toURI());
            final Schema schema = schemaFactory.newSchema(file);
            factory.setSchema(schema);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            // Convert xml to xml doc.
            final Document document = builder.parse(new InputSource(new StringReader(xml)));

            // create JAXBContext which will be used to create a Binder
            final JAXBContext jc = JAXBContext.newInstance(UserContext.class);

            // create binder object
            final Binder<Node> binder = jc.createBinder();

            // set the property
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // get xml node
            final Node xmlNode = document.getDocumentElement();

            // set schema for binder
            binder.setSchema(schema);

            // unmarshaling xml to JAXB object
            userContext = (UserContext) binder.unmarshal(xmlNode);

        } catch (final JAXBException | ParserConfigurationException | SAXException | IOException
                | URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return userContext;
    }

    /**
     * Checks if a compare user context has the same contexts as a base context.
     * Does return true if it has MORE contexts.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same contexts as base, false if not.
     */
    private static boolean hasUserContextTheSameContexts(final UserContext base,
            final UserContext compare) {
        for (final Context baseContext : base.getContexts()) {
            // Match checks if for each context in this there is one in
            // compare.
            boolean match = false;
            for (final Context compareContext : compare.getContexts()) {
                // if id fits check if context fits.
                if (baseContext.getId().equals(compareContext.getId())) {
                    match = true;
                    if (!baseContext.equals(compareContext)) {
                        return false;
                    }
                }
            }
            // no matching context
            if (match != true) {
                return false;
            }
        }
        return true;
    }

    private List<Context> contexts;

    public UserContext() {
        this.contexts = new ArrayList<Context>();
    }

    @JsonIgnore
    public void addContext(final Context c) {
        this.contexts.add(c);

    }

    /**
     * Checks if user contexts are equal in field values.
     *
     * @param compare
     *            user context to compare with.
     * @return true if contexts are equal in field values, false else.
     */
    @JsonIgnore
    public boolean equals(final UserContext compare) {
        return (UserContext.hasUserContextTheSameContexts(compare, this) && UserContext
                .hasUserContextTheSameContexts(this, compare));
    }

    /**
     * @param id
     * @return null if not found.
     */
    @JsonIgnore
    public Context getContext(final String id) {
        for (final Context context : this.contexts) {
            if (context.getId().equals(id)) {
                return context;
            }
        }
        return null;
    }

    @XmlElement(name = "option")
    public List<Context> getContexts() {
        return this.contexts;
    }

    /**
     * Generate the json representation from the object used for the front end.
     *
     * @return json string.
     */
    @JsonIgnore
    public String getJson() throws IOException {
        String jsonString = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode rootNode = mapper.valueToTree(this);
            // TODO manipulate tree
            final StringWriter stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, rootNode);
            jsonString = stringWriter.toString();
        } catch (final JsonProcessingException e) {
            throw new IOException(e.getMessage());
        }
        return jsonString;
    }

    /**
     * Generate the xml representation from the object used for the front end.
     *
     * @return json string.
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
        } catch (final JAXBException e) {
            throw new IOException(e.getMessage());
        }
        return xmlString;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    public void setContexts(final List<Context> contexts) {
        this.contexts = contexts;
    }

}
