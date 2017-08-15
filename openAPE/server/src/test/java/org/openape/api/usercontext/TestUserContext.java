package org.openape.api.usercontext;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.database.mongoDB.TestDatabaseConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
            //System.out.println(xml);
            final UserContext userContext = UserContext.getObjectFromXml(xml);
            Assert.assertTrue(userContext.equals(sample));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetJson() {
        JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);
        // get root node.
        final ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.valueToTree(TestDatabaseConnection.sampleUserContext());
        ObjectNode rootObject = (ObjectNode) rootNode;

        //remove owner attributes
        rootObject.remove("public");
        rootObject.remove("owner");
        
        // get context list.
        JsonNode contextNode = rootNode.get("contexts");
        ArrayNode contextArray = (ArrayNode) contextNode;
        Iterator<JsonNode> contestIterator = contextArray.iterator();
        
        // Replace context list by context fields with id as key.
        rootObject.remove("contexts");
        while (contestIterator.hasNext()) {
            JsonNode context = contestIterator.next();
            ObjectNode contextObject = (ObjectNode) context;
            String id = contextObject.get("id").textValue();
            contextObject.remove("id");
            rootObject.set(id, contextObject);
            

            //get preferences
            JsonNode preferences = contextObject.get("preferences");
            ArrayNode preferencesArray = (ArrayNode) preferences;
            Iterator<JsonNode> pereferenceIterator = preferencesArray.iterator();
          
            //Format preferences
            ObjectNode newPreferences = new ObjectNode(jsonNodeFactory);
            while (pereferenceIterator.hasNext()) {
                JsonNode preference = pereferenceIterator.next();
                String key = preference.get("key").textValue();
                String value = preference.get("value").textValue();
                newPreferences.put(key, value);
            }
            contextObject.remove("preferences");
            contextObject.set("preferences", newPreferences);
        }
        System.out.println("bla");
        System.out.println(rootObject.toString());
    }

}
