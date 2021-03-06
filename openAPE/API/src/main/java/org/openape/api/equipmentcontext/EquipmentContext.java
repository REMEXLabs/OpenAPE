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

package org.openape.api.equipmentcontext;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.XMLConstants;
import jakarta.xml.bind.Binder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openape.api.contexts.ContextObject;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.Descriptor;
import org.openape.api.databaseObjectBase.ImplementationParameters;
import org.openape.api.databaseObjectBase.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utility.ContextParsingHelpers;

/**
 * Equipment context object defined in 7.4.1
 */
@XmlRootElement(name = "equipment-context")
public class EquipmentContext extends ContextObject{
    private static final String CONTEXTS_SCHEMA_XSD = "ContextsSchema.xsd";

    private static final String EQUIPMENT_CONTEXT = "equipment-context";

    private static final long serialVersionUID = 4810176872836108065L;

    /**
     * Generate the user context from the json string used in the front or back
     * end. Sets public: false and owner: null.
     *
     * @return context object.
     */
    @JsonIgnore
    public static EquipmentContext getObjectFromJson(final String json)
            throws IllegalArgumentException {
        // Context to build from tree
        final EquipmentContext context = new EquipmentContext();
        try {
            // Get tree from json.
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode rootNode = mapper.readTree(json);
            final ObjectNode rootObject = (ObjectNode) rootNode;

            // get owner and public if available.
            final JsonNode implemParams = rootObject
                    .get(ContextObject.IMPLEMENTATION_PARAMETERS);
            if ((implemParams != null) && !(implemParams instanceof NullNode)) {
                final ObjectNode implemParamsNode = (ObjectNode) implemParams;
                context.getImplementationParameters().setPublic(
                        implemParamsNode.get(ContextObject.PUBLIC).booleanValue());
            }

            // get root node
            final JsonNode contextNode = rootObject.get(EquipmentContext.EQUIPMENT_CONTEXT);
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
                property.setValue(propertyArray.get(1).textValue());

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
     * Generate the equipment context from the xml string used in the the front
     * end.
     *
     * @return equipment context object.
     */
    @JsonIgnore
    public static EquipmentContext getObjectFromXml(String xml) throws IllegalArgumentException {
        EquipmentContext equipmentContext = null;
        try {
            xml = ImplementationParameters.addPublicAttributeIfMissing(xml);
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false); // we will use schema instead of DTD
            factory.setNamespaceAware(true);
            final SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // get schema file from resource folder
            final URL url = EquipmentContext.class.getClassLoader().getResource(
                    EquipmentContext.CONTEXTS_SCHEMA_XSD);
            final Schema schema = schemaFactory.newSchema(url);
            factory.setSchema(schema);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            // Convert xml to xml doc.
            final Document document = builder.parse(new InputSource(new StringReader(xml)));

            // create JAXBContext which will be used to create a Binder
            final JAXBContext jc = JAXBContext.newInstance(EquipmentContext.class);

            // create binder object
            final Binder<Node> binder = jc.createBinder();

            // set the property
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // get xml node
            final Node xmlNode = document.getDocumentElement();

            // set schema for binder
            binder.setSchema(schema);

            // unmarshaling xml to JAXB object
            equipmentContext = (EquipmentContext) binder.unmarshal(xmlNode);

        } catch (final Exception e) {
            System.out.println("verdammter Fehler");
        	e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return equipmentContext;
    }

    public EquipmentContext() {
       super(EQUIPMENT_CONTEXT);
    }

    @Override
    @JsonIgnore
    public boolean isValid() {
        return true;
    }
}
