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
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openape.api.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * User context object defined in 7.2.1
 */
@XmlRootElement(name = "user-context")
public class UserContext extends Resource {
    private static final long serialVersionUID = 5891055316807633786L;

    /**
     * Generate the user context from the json string used in the front or back
     * end. Sets public: false and owner: null.
     *
     * @return user context object.
     */
    @JsonIgnore
    public static UserContext getObjectFromJson(final String json) throws IllegalArgumentException {
        // User context to build from tree
        final UserContext userContext = new UserContext();
        try {
            // Get tree from json.
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode rootNode = mapper.readTree(json);
            final ObjectNode rootObject = (ObjectNode) rootNode;

            // get owner and public if available.
            final JsonNode owner = rootObject.get("owner");
            if ((owner != null) && !(owner instanceof NullNode)) {
                userContext.setOwner(owner.textValue());
            }
            final JsonNode publicField = rootObject.get("public");
            if ((publicField != null) && !(publicField instanceof NullNode)) {
                userContext.setPublic(publicField.booleanValue());
            }

            // Iterate over contexts and create corresponding context objects.
            final Iterator<String> contextIterator = rootObject.fieldNames();
            while (contextIterator.hasNext()) {
                final String contextID = contextIterator.next();
                if (!contextID.equals("owner") && !contextID.equals("public")) {
                    final Context context = new Context();
                    userContext.addContext(context);
                    context.setId(contextID);
                    final ObjectNode contextNode = (ObjectNode) rootObject.get(contextID);
                    context.setName(contextNode.get("name").textValue());

                    // add preference objects
                    final ObjectNode preferences = (ObjectNode) contextNode.get("preferences");
                    final Iterator<String> preferenceIterator = preferences.fieldNames();
                    while (preferenceIterator.hasNext()) {
                        final String preferenceKey = preferenceIterator.next();
                        final Preference preference = new Preference();
                        context.addPreference(preference);
                        preference.setKey(preferenceKey);
                        preference.setValue(preferences.get(preferenceKey).textValue());
                    }

                    // add condition objects
                    final JsonNode conditions = contextNode.get("conditions");
                    if ((conditions != null) && !(conditions instanceof NullNode)) {
                        final ArrayNode conditionsArray = (ArrayNode) conditions;
                        final Iterator<JsonNode> conditionsIterator = conditionsArray.iterator();
                        while (conditionsIterator.hasNext()) {
                            final ObjectNode conditionNode = (ObjectNode) conditionsIterator.next();
                            final Condition condition = new Condition();
                            context.addCondition(condition);
                            condition.setType(conditionNode.get("type").textValue());
                            final List<Operand> operandList = new ArrayList<>();

                            // add operands.
                            final ArrayNode operands = (ArrayNode) conditionNode.get("operands");
                            UserContext.recursiveOperandCreation(operandList, operands);
                            condition.setOperands(operandList);
                        }
                    }

                }
            }
        } catch (final Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        userContext.validate();
        return userContext;
    }

    /**
     * Generate the user context from the xml string used in the the front end.
     *
     * @return user context object.
     */
    @JsonIgnore
    public static UserContext getObjectFromXml(String xml) throws IllegalArgumentException {
        UserContext userContext = null;
        try {
            /*
             * add type statements (xsi:type="condition" and
             * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance") to
             * operands that are conditions, if missing.
             */
            // Create document to work on.
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
            // find all operands that are conditions.
            final NodeList nodeList = document.getElementsByTagName("operand");
            for (int i = 0; i < nodeList.getLength(); i++) {
                final Node operandNode = nodeList.item(i);
                final Element operandElement = (Element) operandNode;

                // operand that is a condition.
                if (operandElement.hasAttribute("type")) {
                    // If type information is missing add it.
                    if (!operandElement.hasAttribute("xsi:type")) {
                        operandElement.setAttribute("xsi:type", "condition");
                        operandElement.setAttribute("xmlns:xsi",
                                "http://www.w3.org/2001/XMLSchema-instance");
                        // System.out.println("bla");
                    }
                }
            }
            // Create String from document
            final DOMSource domSource = new DOMSource(document);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            // Update xml string.
            xml = writer.toString();

            xml = Resource.addPublicAttributeIfMissing(xml);

            /*
             * Create user context from xml.
             */
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false); // we will use schema instead of DTD
            factory.setNamespaceAware(true);
            final SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // get schema file from resource folder
            final URL url = UserContext.class.getClassLoader().getResource("ContextsSchema.xsd");
            final File file = new File(url.toURI());
            final Schema schema = schemaFactory.newSchema(file);
            factory.setSchema(schema);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            // Convert xml to xml doc.
            final Document document2 = builder.parse(new InputSource(new StringReader(xml)));

            // create JAXBContext which will be used to create a Binder
            final JAXBContext jc = JAXBContext.newInstance(UserContext.class);

            // create binder object
            final Binder<Node> binder = jc.createBinder();

            // set the property
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // get xml node
            final Node xmlNode = document2.getDocumentElement();

            // set schema for binder
            binder.setSchema(schema);

            // unmarshaling xml to JAXB object
            userContext = (UserContext) binder.unmarshal(xmlNode);

        } catch (final Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        userContext.validate();
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

    /**
     * Used in getObjectFromJson to generate operands.
     *
     * @param operandList
     * @param operands
     */
    private static void recursiveOperandCreation(final List<Operand> operandList,
            final ArrayNode operands) throws IOException, JsonParseException, ClassCastException {
        final Iterator<JsonNode> operandsIterator = operands.iterator();
        while (operandsIterator.hasNext()) {
            final JsonNode operand = operandsIterator.next();

            // Check if condition or operand level.
            if (operand instanceof TextNode) {
                // operand level.
                operandList.add(new Operand(operand.textValue()));
            } else {

                // condition level
                final Condition subCondition = new Condition();
                subCondition.setType(operand.get("type").textValue());
                operandList.add(subCondition);

                // recursion to add operands to sub condition.
                final List<Operand> subConditionOperandList = new ArrayList<Operand>();
                final ArrayNode subOperands = (ArrayNode) operand.get("operands");
                UserContext.recursiveOperandCreation(subConditionOperandList, subOperands);
                subCondition.setOperands(subConditionOperandList);

            }
        }
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
     * Generate the json representation from the object used for the database.
     *
     * @return json string.
     */
    @JsonIgnore
    public String getBackEndJson() throws IOException {
        String jsonString = null;
        try {
            jsonString = this.getJson(false);
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        }
        return jsonString;
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
     * Deletes owner and public field.
     *
     * @return json string.
     */
    @JsonIgnore
    public String getForntEndJson() throws IOException {
        String jsonString = null;
        try {
            jsonString = this.getJson(true);
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        }
        return jsonString;
    }

    /**
     * Generates json string from Object.
     *
     * @param frontEnd
     *            if true public and owner field are excluded, else they are
     *            included.
     * @throws ClassCastException
     * @throws IOException
     *             , JsonProcessingException
     */
    private String getJson(final boolean frontEnd) throws ClassCastException, IOException {
        final JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);
        // get root node.
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode = mapper.valueToTree(this);
        final ObjectNode rootObject = (ObjectNode) rootNode;

        if (frontEnd) {
            // remove owner attributes
            //rootObject.remove("public");
            rootObject.remove("owner");
        }

        // get context list.
        final JsonNode contextNode = rootNode.get("contexts");
        final ArrayNode contextArray = (ArrayNode) contextNode;
        final Iterator<JsonNode> contestIterator = contextArray.iterator();

        // Replace context list by context fields with id as key.
        rootObject.remove("contexts");
        while (contestIterator.hasNext()) {
            final JsonNode context = contestIterator.next();
            final ObjectNode contextObject = (ObjectNode) context;
            final String id = contextObject.get("id").textValue();
            contextObject.remove("id");
            rootObject.set(id, contextObject);

            // get preferences
            final JsonNode preferences = contextObject.get("preferences");
            final ArrayNode preferencesArray = (ArrayNode) preferences;
            final Iterator<JsonNode> pereferenceIterator = preferencesArray.iterator();

            // Format preferences
            final ObjectNode newPreferences = new ObjectNode(jsonNodeFactory);
            while (pereferenceIterator.hasNext()) {
                final JsonNode preference = pereferenceIterator.next();
                final String key = preference.get("key").textValue();
                final String value = preference.get("value").textValue();
                newPreferences.put(key, value);
            }
            contextObject.remove("preferences");
            contextObject.set("preferences", newPreferences);

            // get conditions
            final JsonNode conditions = contextObject.get("conditions");
            if ((conditions != null) && !(conditions instanceof NullNode)) {
                final ArrayNode conditionsArray = (ArrayNode) conditions;
                final Iterator<JsonNode> conditionsIterator = conditionsArray.iterator();

                // Remove value field from root condition
                while (conditionsIterator.hasNext()) {
                    final JsonNode condition = conditionsIterator.next();
                    final ObjectNode conditionObject = (ObjectNode) condition;
                    conditionObject.remove("value");

                    // Format operands
                    final ArrayNode operands = (ArrayNode) conditionObject.get("operands");
                    this.recursiveOperandFormatting(operands);
                }
            }
        }
        final StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter, rootObject);
        final String jsonString = stringWriter.toString();
        return jsonString;
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

    /**
     * Used by getJson to recursively format the conditions.
     *
     * @param operands
     * @throws ClassCastException
     */
    private void recursiveOperandFormatting(final ArrayNode operands) throws ClassCastException {
        // If the operands on this level are no conditions, store values to add
        // to operands.
        final List<String> operandValues = new ArrayList<>();
        Boolean valueLevel = false;
        final Iterator<JsonNode> operandsIterator = operands.iterator();
        while (operandsIterator.hasNext()) {
            final JsonNode opernad = operandsIterator.next();
            final ObjectNode operandObject = (ObjectNode) opernad;
            if (operandObject.get("value") instanceof NullNode) {
                // operands on this level are conditions.
                operandObject.remove("value");
                final ArrayNode operandsOfOperand = (ArrayNode) opernad.get("operands");
                // recursion
                this.recursiveOperandFormatting(operandsOfOperand);
            } else {
                // operands on this level are values.
                valueLevel = true;
                final String value = operandObject.get("value").textValue();
                operandValues.add(value);
            }

        }
        // if level with values and not conditions, revome all operands and add
        // values as strings.
        if (valueLevel) {
            operands.removeAll();
            for (final String value : operandValues) {
                operands.add(value);
            }
        }
    }

    public void setContexts(final List<Context> contexts) {
        this.contexts = contexts;
    }

    /**
     * Validate Conditions
     *
     * @throws IllegalArgumentException
     */
    @JsonIgnore
    private void validate() throws IllegalArgumentException {
        for (final Context context : this.contexts) {
            context.vaidate();
        }
    }
}
