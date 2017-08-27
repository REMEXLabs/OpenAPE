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

package org.openape.api.taskcontext;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.ImplementationParameters;
import org.openape.api.databaseObjectBase.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Task context object defined in 7.3.1
 */
@XmlRootElement(name = "task-context")
public class TaskContext extends DatabaseObject {
    private static final long serialVersionUID = 3325722856059287182L;

    /**
     * Generate the task context from the xml string used in the the front end.
     *
     * @return task context object.
     */
    @JsonIgnore
    public static TaskContext getObjectFromXml(String xml) throws IllegalArgumentException {
        TaskContext taskContext = null;
        try {
            xml = ImplementationParameters.addPublicAttributeIfMissing(xml);
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false); // we will use schema instead of DTD
            factory.setNamespaceAware(true);
            final SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // get schema file from resource folder
            final URL url = TaskContext.class.getClassLoader().getResource("ContextsSchema.xsd");
            final File file = new File(url.toURI());
            final Schema schema = schemaFactory.newSchema(file);
            factory.setSchema(schema);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            // Convert xml to xml doc.
            final Document document = builder.parse(new InputSource(new StringReader(xml)));

            // create JAXBContext which will be used to create a Binder
            final JAXBContext jc = JAXBContext.newInstance(TaskContext.class);

            // create binder object
            final Binder<Node> binder = jc.createBinder();

            // set the property
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // get xml node
            final Node xmlNode = document.getDocumentElement();

            // set schema for binder
            binder.setSchema(schema);

            // unmarshaling xml to JAXB object
            taskContext = (TaskContext) binder.unmarshal(xmlNode);

        } catch (final Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return taskContext;
    }

    /**
     * Checks if a compare task context has the same properties as a base
     * context. Does return true if it has MORE contexts.
     *
     * @param base
     * @param compare
     * @return true, if compare has the same properties as base, false if not.
     */
    private static boolean hasTaskContextTheSameProperties(final TaskContext base,
            final TaskContext compare) {
        for (final Property baseProperty : base.getPropertys()) {
            // Match checks if for each property in this there is one in
            // compare.
            boolean match = false;
            for (final Property compareContext : compare.getPropertys()) {
                // if id fits check if property fits.
                if (baseProperty.getName().equals(compareContext.getName())) {
                    if (baseProperty.equals(compareContext)) {
                        match = true;
                    }
                }
            }
            // no matching property
            if (match != true) {
                return false;
            }
        }
        return true;
    }

    private ImplementationParameters implementationParameters = new ImplementationParameters();
    private List<Property> propertys = new ArrayList<Property>();

    public TaskContext() {
        this.propertys = new ArrayList<Property>();
    }

    public void addProperty(final Property property) {
        this.propertys.add(property);

    }

    /**
     * Checks if task contexts are equal in field values.
     *
     * @param compare
     *            task context to compare with.
     * @return true if contexts are equal in field values, false else.
     */
    @JsonIgnore
    public boolean equals(final TaskContext compare) {
        return (TaskContext.hasTaskContextTheSameProperties(compare, this) && TaskContext
                .hasTaskContextTheSameProperties(this, compare));

    }

    @JsonProperty(value = "implementation-parameters")
    @XmlElement(name = "implementation-parameters")
    public ImplementationParameters getImplementationParameters() {
        return this.implementationParameters;
    }

    @XmlElement(name = "property")
    public List<Property> getPropertys() {
        return this.propertys;
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
            final JAXBContext context = JAXBContext.newInstance(TaskContext.class);
            final Marshaller marshaller = context.createMarshaller();
            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);
            xmlString = stringWriter.toString();
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        }
        return xmlString;
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    public void setImplementationParameters(final ImplementationParameters implementationParameters) {
        this.implementationParameters = implementationParameters;
    }

    public void setPropertys(final List<Property> propertys) {
        this.propertys = propertys;
    }

}
