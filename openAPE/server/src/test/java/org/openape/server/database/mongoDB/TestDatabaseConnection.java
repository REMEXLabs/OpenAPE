package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openape.api.Property;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.usercontext.Condition;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.Operand;
import org.openape.api.usercontext.Preference;
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
        userContext.setPublic(true);
        final Context defaultPreference = new Context("default", "Default preferences");
        final Context darkPreference = new Context("dark", "little environmental light");
        userContext.addContext(defaultPreference);
        userContext.addContext(darkPreference);
        defaultPreference.addPreference(new Preference(
                "http://registry.gpii.net/common/magnifierEnabled", "false"));
        defaultPreference.addPreference(new Preference(
                "http://registry.gpii.net/applications/org.chrome.cloud4chrome/invertColours",
                "false"));
        darkPreference.addPreference(new Preference(
                "http://registry.gpii.net/common/magnifierEnabled", "true"));
        darkPreference.addPreference(new Preference(
                "http://registry.gpii.net/common/magnification", "2"));

        final List<Operand> andConditionOperands = new ArrayList<Operand>();
        final List<Operand> geOperandList = new ArrayList<Operand>();
        geOperandList.add(new Operand("http://registry.gpii.net/common/env/visual.luminance"));
        geOperandList.add(new Operand("0"));
        final List<Operand> leOperandList = new ArrayList<Operand>();
        leOperandList.add(new Operand("http://registry.gpii.net/common/env/visual.luminance"));
        leOperandList.add(new Operand("200"));
        andConditionOperands.add(new Condition("ge", geOperandList));
        andConditionOperands.add(new Condition("le", leOperandList));
        final Condition andCondition = new Condition("and", andConditionOperands);
        darkPreference.addCondition(andCondition);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final String jsonData = mapper.writeValueAsString(userContext);
            // System.out.println(jsonData);
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
            final List<Context> newContexts = new ArrayList<Context>();
            newContexts.add(sampleContext.getContext("default"));
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
