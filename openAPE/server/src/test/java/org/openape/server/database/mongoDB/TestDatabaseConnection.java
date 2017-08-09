package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openape.api.Property;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.usercontext.Condition;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDatabaseConnection {
    public static ResourceDescription sampleResourceDescription() {
        final ResourceDescription resourceDescription = new ResourceDescription();
        final Property property = new Property();
        resourceDescription.addProperty(property);
        property.setName("resource-uri");
        property.setValue("https://res.openurc.org/api/resources/R12345");
        return resourceDescription;
    }

    /**
     * @return a sample user context representing someone with restricted
     *         vision.
     */
    public static UserContext sampleUserContext() {
        final UserContext userContext = new UserContext();
        final Context defaultPreference = new Context("Default preferences");
        final Context darkPreference = new Context("little environmental light");
        userContext.addContext("default", defaultPreference);
        userContext.addContext("dark", darkPreference);
        defaultPreference
                .addPreference("http://registry.gpii.net/common/magnifierEnabled", "false");
        defaultPreference.addPreference(
                "http://registry.gpii.net/applications/org.chrome.cloud4chrome/invertColours",
                "false");
        darkPreference.addPreference("http://registry.gpii.net/common/magnifierEnabled", "true");
        darkPreference.addPreference("http://registry.gpii.net/common/magnification", "2");

        final List<Object> andConditionOperands = new ArrayList<Object>();
        final List<Object> geOperandList = new ArrayList<Object>();
        geOperandList.add("http://registry.gpii.net/common/env/visual.luminance");
        geOperandList.add("0");
        final List<Object> leOperandList = new ArrayList<Object>();
        leOperandList.add("http://registry.gpii.net/common/env/visual.luminance");
        leOperandList.add("200");
        andConditionOperands.add(new Condition("ge", geOperandList));
        andConditionOperands.add(new Condition("le", leOperandList));
        final Condition andCondition = new Condition("and", andConditionOperands);
        darkPreference.addCondition(andCondition);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final String jsonData = mapper.writeValueAsString(userContext);
            System.out.println(jsonData);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return userContext;
    }

    private DatabaseConnection dataBaseConnection;

    @Before
    public void init() {
        this.dataBaseConnection = DatabaseConnection.getInstance();
    }

    @Test
    public void testDataStoreage() {
        final UserContext sampleContext = TestDatabaseConnection.sampleUserContext();
        try {
            // test insert.
            String id;
            Assert.assertNotEquals(
                    "",
                    id = this.dataBaseConnection.storeData(MongoCollectionTypes.USERCONTEXT,
                            sampleContext));
            System.out.println(id);
            // test get.
            final UserContext recievedContext = (UserContext) this.dataBaseConnection.getData(
                    MongoCollectionTypes.USERCONTEXT, id);
            Assert.assertTrue(sampleContext.equals(recievedContext));
            // remove second context.
            final Map<String, Context> newContexts = new HashMap<String, Context>();
            newContexts.put("default", sampleContext.getContexts().get("default"));
            sampleContext.setContexts(newContexts);
            // test update
            this.dataBaseConnection.updateData(MongoCollectionTypes.USERCONTEXT, sampleContext, id);
            final UserContext updatetContext = (UserContext) this.dataBaseConnection.getData(
                    MongoCollectionTypes.USERCONTEXT, id);
            Assert.assertTrue(sampleContext.equals(updatetContext));
            Assert.assertFalse(TestDatabaseConnection.sampleUserContext().equals(updatetContext));
            // test delete
            Assert.assertTrue(this.dataBaseConnection.deleteData(MongoCollectionTypes.USERCONTEXT,
                    id));
        } catch (ClassCastException | IOException e) {
            e.printStackTrace();
        }

    }

}
