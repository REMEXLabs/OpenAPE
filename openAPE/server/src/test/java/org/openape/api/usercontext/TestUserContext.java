package org.openape.api.usercontext;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.database.mongoDB.TestDatabaseConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestUserContext {

    @Test
    public void testEquals() {
        final UserContext sample = TestDatabaseConnection.sampleUserContext();
        Assert.assertTrue(sample.equals(sample));
    }

    @Test
    public void testGetXML() {
        final UserContext sample = TestDatabaseConnection.sampleUserContext();
        try {
            final String xml = sample.getXML();
            // System.out.println(xml);
            final UserContext userContext = UserContext.getObjectFromXml(xml);
            Assert.assertTrue(userContext.equals(sample));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetJson() throws IOException {
        final UserContext sample = TestDatabaseConnection.sampleUserContext();
        //System.out.println(sample.getForntEndJson());
        //System.out.println(sample.getBackEndJson());
        
    }
    
    @Test
    public void testGetObjectFromJson() throws JsonProcessingException, IOException {
        Scanner scanner = new Scanner(getClass().getResourceAsStream("sample.json"));
        String s = new String();
        while(scanner.hasNextLine()){
                s = s + scanner.nextLine();
         }
        scanner.close();

        // Get tree from json.
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode = mapper.readTree(s);
        final ObjectNode rootObject = (ObjectNode) rootNode;
        
        // User context to build from tree
        UserContext userContext = new UserContext();
        
        // get owner and public if available.
        JsonNode owner = rootObject.get("owner");
        if(owner != null && !(owner instanceof NullNode)) {
            userContext.setOwner(owner.textValue());
        }   
        JsonNode publicField = rootObject.get("public");
        if(publicField != null && !(publicField instanceof NullNode)) {
            userContext.setPublic(publicField.booleanValue());
        }
        
        
        

//        // get context list.
//        final JsonNode contextNode = rootNode.get("contexts");
//        final ArrayNode contextArray = (ArrayNode) contextNode;
//        final Iterator<JsonNode> contestIterator = contextArray.iterator();
//
//        // Replace context list by context fields with id as key.
//        rootObject.remove("contexts");
//        while (contestIterator.hasNext()) {
//            final JsonNode context = contestIterator.next();
//            final ObjectNode contextObject = (ObjectNode) context;
//            final String id = contextObject.get("id").textValue();
//            contextObject.remove("id");
//            rootObject.set(id, contextObject);
//
//            // get preferences
//            final JsonNode preferences = contextObject.get("preferences");
//            final ArrayNode preferencesArray = (ArrayNode) preferences;
//            final Iterator<JsonNode> pereferenceIterator = preferencesArray.iterator();
//
//            // Format preferences
//            final ObjectNode newPreferences = new ObjectNode(jsonNodeFactory);
//            while (pereferenceIterator.hasNext()) {
//                final JsonNode preference = pereferenceIterator.next();
//                final String key = preference.get("key").textValue();
//                final String value = preference.get("value").textValue();
//                newPreferences.put(key, value);
//            }
//            contextObject.remove("preferences");
//            contextObject.set("preferences", newPreferences);
//
//            // get conditions
//            final JsonNode conditions = contextObject.get("conditions");
//            if ((conditions != null) && !(conditions instanceof NullNode)) {
//                final ArrayNode conditionsArray = (ArrayNode) conditions;
//                final Iterator<JsonNode> conditionsIterator = conditionsArray.iterator();
//
//                // Remove value field from root condition
//                while (conditionsIterator.hasNext()) {
//                    final JsonNode condition = conditionsIterator.next();
//                    final ObjectNode conditionObject = (ObjectNode) condition;
//                    conditionObject.remove("value");
//
//                    // Format operands
//                    final ArrayNode operands = (ArrayNode) conditionObject.get("operands");
//                    this.recursiveOperandFormatting(operands);
//                }
//            }
//        }
    
        // TODO manipulate tree
        //UserContext userContext = mapper.treeToValue(rootNode, UserContext.class);
    }

}
