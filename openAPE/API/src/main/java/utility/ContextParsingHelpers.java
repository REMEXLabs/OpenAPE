package utility;

import org.openape.api.contexts.KeyValuePair;
import org.openape.api.databaseObjectBase.Property;
import org.openape.api.usercontext.Preference;
import org.openape.api.usercontext.UserContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContextParsingHelpers {

	public static void termValueToJson(JsonNode preference, ObjectNode newPreferences) {
	    final String key = preference.get(UserContext.KEY).textValue();
	    System.out.println("serialize: " + key);
	    
	                    JsonNode node = preference.get(UserContext.VALUE);
	                    
	
	        if ( node.isBoolean()   ) {
	newPreferences.put(key, node.asBoolean()   );
	} else if (node.isDouble()   ) {
	newPreferences.put(key, node.asDouble()   );
	} else if(node.isInt()    ) {
	newPreferences.put(key, node.asInt()   );
	} else {   
	newPreferences.put(key, node.textValue());
	}
	
			
		}

	public static void parseNode(KeyValuePair preference, JsonNode nodeToParse) {
		switch(nodeToParse.getNodeType()) {
	    case STRING :
	    	preference.setValue(nodeToParse.textValue());
	    	break;
	    case BOOLEAN:
	    	
	    	preference.setValue(nodeToParse.asBoolean()   );
	    
	    	break;
	    case NUMBER:
	    	if(nodeToParse.isFloatingPointNumber() ) {
	    	preference.setValue(nodeToParse.asDouble()   );
	    } else {
	    	
	    	preference.setValue(nodeToParse.asInt()   );
	    	
	    }
	    	
	    	break;
	    	default:
	    }
	    
		
	}

	public static void propertyValueToJson(ArrayNode propertyArray, Property property) {
		Object value = property.getValue();
		if (value instanceof	Boolean) {
			propertyArray.add((Boolean) value);
					} else if (value instanceof Number) {
						if (value instanceof  Double) {
							propertyArray.add((Double)value);
						} else {
							propertyArray.add((Integer)value);
						}
					} else if (value instanceof String) {
						propertyArray.add((String) value);
					}
		
	}

}
