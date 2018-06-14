package org.openape.api.contexts;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.Descriptor;
import org.openape.api.databaseObjectBase.ImplementationParameters;
import org.openape.api.databaseObjectBase.Property;
import org.openape.api.taskcontext.TaskContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utility.ContextParsingHelpers;

public abstract class ContextObject extends DatabaseObject {

	
	
	protected static final String PUBLIC = "public";
	protected static final String IMPLEMENTATION_PARAMETERS = "implementation-parameters";
	private ImplementationParameters implementationParameters = new ImplementationParameters();
	protected List<Property> propertys = new ArrayList<Property>();
	private String contextType;

	public ContextObject() {
        this.propertys = new ArrayList<Property>();
	}
	
	public ContextObject(String contextType) {
		this();
		this.contextType = contextType;

		
	}
	
	
	/**
     * Generate the json representation from the object used for the front end.
     * Deletes owner and public field.
     *
     * @return json string.
     */
    
@JsonIgnore	
	public String getFrontendJson() throws IOException {
        String jsonString = null;
        try {
            // Setup document root
            final JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);
            final ObjectNode root = new ObjectNode(jsonNodeFactory);
            final ArrayNode contextArray = new ArrayNode(jsonNodeFactory);
            root.set(contextType, contextArray);

            // Add all properties to context array.
            final List<Property> properties = this.getPropertys();
            for (final Property property : properties) {
                final ArrayNode propertyArray = new ArrayNode(jsonNodeFactory);
                contextArray.add(propertyArray);
                // Add name and value to property array.
                propertyArray.add(property.getName());
                // todo
                ContextParsingHelpers.propertyValueToJson(propertyArray,property);
                
                // Add descriptors to property array, if available.
                final List<Descriptor> descriptors = property.getDescriptors();
                for (final Descriptor descriptor : descriptors) {
                    final ArrayNode descriptorArray = new ArrayNode(jsonNodeFactory);
                    propertyArray.add(descriptorArray);
                    // Add name and value to descriptor array
                    descriptorArray.add(descriptor.getName());
                    descriptorArray.add(descriptor.getValue());
                }
            }
            // write out string.
            final StringWriter stringWriter = new StringWriter();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stringWriter, root);
            jsonString = stringWriter.toString();
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        }
        return jsonString;

		
	}

	public void addProperty(final Property property) {
	    this.propertys.add(property);
	
	}

	@JsonProperty(value = TaskContext.IMPLEMENTATION_PARAMETERS)
	@XmlElement(name = TaskContext.IMPLEMENTATION_PARAMETERS)
	public ImplementationParameters getImplementationParameters() {
	    return this.implementationParameters;
	}

	@XmlElement(name = "property")
	public List<Property> getPropertys() {
	    return this.propertys;
	}

	public void setImplementationParameters(final ImplementationParameters implementationParameters) {
	    this.implementationParameters = implementationParameters;
	}

	public void setPropertys(final List<Property> propertys) {
	    this.propertys = propertys;
	}

}
