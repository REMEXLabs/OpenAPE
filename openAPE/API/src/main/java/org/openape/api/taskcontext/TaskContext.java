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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openape.api.contexts.ContextObject;
import org.openape.api.contexts.KeyValuePair;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.Descriptor;
import org.openape.api.databaseObjectBase.ImplementationParameters;
import org.openape.api.databaseObjectBase.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utility.ContextParsingHelpers;

/**
 * Task context object defined in 7.3.1
 */
@XmlRootElement(name = "task-context")
public class TaskContext extends ContextObject {
    private static final String CONTEXTS_SCHEMA_XSD = "ContextsSchema.xsd";

    private static final String TASK_CONTEXT = "task-context";

    private static final long serialVersionUID = 3325722856059287182L;

    public TaskContext() {
		super(TASK_CONTEXT);
	}
    
    /**
     * Generate the user context from the json string used in the front or back
     * end. Sets public: false and owner: null.
     *
     * @return context object.
     */
    @JsonIgnore
//    TODO rename
    public static ContextObject getObjectFromJson(final String json) throws IllegalArgumentException {
        // Context to build from tree
        final ContextObject context = new TaskContext();
        try {
            // Get tree from json.
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode rootNode = mapper.readTree(json);
            final ObjectNode rootObject = (ObjectNode) rootNode;

            // get owner and public if available.
            final JsonNode implemParams = rootObject.get(ContextObject.IMPLEMENTATION_PARAMETERS);
            if ((implemParams != null) && !(implemParams instanceof NullNode)) {
                final ObjectNode implemParamsNode = (ObjectNode) implemParams;
                context.getImplementationParameters().setPublic(
                        implemParamsNode.get(ContextObject.PUBLIC).booleanValue());
            }

            // get root node
            final JsonNode contextNode = rootObject.get(TaskContext.TASK_CONTEXT);
            final ArrayNode contextArray = (ArrayNode) contextNode;
            final Iterator<JsonNode> propertyIterator = contextArray.iterator();

            // get property arrays from context array.
            while (propertyIterator.hasNext()) {
                final JsonNode propertyNode = propertyIterator.next();
                final ArrayNode propertyArray = (ArrayNode) propertyNode;

                // set property name and value,
                final Property property = new Property();
                context.addProperty(property);
                property.setName(propertyArray.get(0).textValue());
                ContextParsingHelpers.parseNode((KeyValuePair) property, propertyArray.get(1));
                propertyArray.get(1);

                // for each descriptor of available, add them to property.
                for (int i = 2; i < propertyArray.size(); i++) {
                    final JsonNode descriptorNode = propertyArray.get(i);
                    final ArrayNode descriptorArray = (ArrayNode) descriptorNode;
                    final Descriptor descriptor = new Descriptor();
                    property.addDescriptor(descriptor);
                    descriptor.setName(descriptorArray.get(0).textValue());
                    descriptor.setValue(descriptorArray.get(1).textValue());
                }

            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return context;
    }

    /**
     * Generate the task context from the xml string used in the the front end.
     *
     * @return task context object.
     */
    @JsonIgnore
    public static ContextObject getObjectFromXml(String xml) throws IllegalArgumentException {
        ContextObject taskContext = null;
        try {
            xml = ImplementationParameters.addPublicAttributeIfMissing(xml);
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false); // we will use schema instead of DTD
            factory.setNamespaceAware(true);
            final SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // get schema file from resource folder
            final URL url = TaskContext.class.getClassLoader().getResource(
                    TaskContext.CONTEXTS_SCHEMA_XSD);
            final Schema schema = schemaFactory.newSchema(url);
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
            taskContext = (ContextObject) binder.unmarshal(xmlNode);

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
    private static boolean hasTaskContextTheSameProperties(final ContextObject base,
            final ContextObject compare) {
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


      
    

    /**
     * Checks if task contexts are equal in field values.
     *
     * @param compare
     *            task context to compare with.
     * @return true if contexts are equal in field values, false else.
     */
    @JsonIgnore
    public boolean equals(final ContextObject compare) {
        return (TaskContext.hasTaskContextTheSameProperties(compare, this) && TaskContext
                .hasTaskContextTheSameProperties(this, compare));

    }

    /**
     * Generate the json representation from the object used for the front end.
     * Deletes owner and public field.
     *
     * @return json string.
     */

    

    

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
            xmlString = this.getImplementationParameters().removeImplemParams(xmlString);
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

}
