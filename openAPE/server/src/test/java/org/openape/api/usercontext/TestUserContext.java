package org.openape.api.usercontext;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.database.mongoDB.TestDatabaseConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        final ObjectMapper mapper = new ObjectMapper();
        mapper.createArrayNode();
        JsonNode rootNode = mapper.valueToTree(TestDatabaseConnection.sampleUserContext());
        System.out.println(rootNode.getNodeType());
        ObjectNode object = (ObjectNode) rootNode;
        object.remove("public");
        object.remove("owner");
        JsonNode contextNode = rootNode.get("contexts");
        System.out.println(contextNode.getNodeType());
        ArrayNode contextArray = (ArrayNode) contextNode;
        Iterator<JsonNode> iterator = contextArray.iterator();
        while (iterator.hasNext()) {
            JsonNode field = iterator.next();
            System.out.println(field + ": " + field.toString());            
        }
        System.out.println("bla");
        System.out.println(rootNode.toString());
    }

}
