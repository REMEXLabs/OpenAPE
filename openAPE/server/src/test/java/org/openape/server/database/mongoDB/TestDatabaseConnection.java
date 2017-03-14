package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDatabaseConnection {
    /**
     * @return a sample user context representing someone with restricted
     *         vision.
     */
    public static UserContext sampleUserContextRestricedVision() {
        final UserContext restrictedVision = new UserContext();
        final Context restrictedViewPc = new Context("Default preferences", "default");
        final Context restrictedViewTicketMachine = new Context("little environmental light",
                "dark");
        restrictedVision.addContext(restrictedViewPc);
        restrictedVision.addContext(restrictedViewTicketMachine);
        restrictedViewPc.addPreference("http://registry.gpii.net/common/magnifierEnabled", "false");
        restrictedViewPc.addPreference(
                "http://registry.gpii.net/applications/org.chrome.cloud4chrome/invertColours",
                "false");
        restrictedViewTicketMachine.addPreference(
                "http://registry.gpii.net/common/magnifierEnabled", "true");
        restrictedViewTicketMachine.addPreference("http://registry.gpii.net/common/magnification",
                "2");
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final String jsonData = mapper.writeValueAsString(restrictedVision);
            System.out.println(jsonData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return restrictedVision;
    }

    private DatabaseConnection dataBaseConnection;

    @Before
    public void init() {
        this.dataBaseConnection = DatabaseConnection.getInstance();
    }

    @Test
    public void testDataStoreage() {
        final UserContext sampleContext = TestDatabaseConnection.sampleUserContextRestricedVision();
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
            newContexts.add(sampleContext.getContexts().get(0));
            sampleContext.setContexts(newContexts);
            // test update
            this.dataBaseConnection.updateData(MongoCollectionTypes.USERCONTEXT, sampleContext, id);
            final UserContext updatetContext = (UserContext) this.dataBaseConnection.getData(
                    MongoCollectionTypes.USERCONTEXT, id);
            Assert.assertTrue(sampleContext.equals(updatetContext));
            Assert.assertFalse(TestDatabaseConnection.sampleUserContextRestricedVision().equals(
                    updatetContext));
            // test delete
            Assert.assertTrue(this.dataBaseConnection.deleteData(MongoCollectionTypes.USERCONTEXT,
                    id));
        } catch (ClassCastException | IOException e) {
            e.printStackTrace();
        }

    }

}
