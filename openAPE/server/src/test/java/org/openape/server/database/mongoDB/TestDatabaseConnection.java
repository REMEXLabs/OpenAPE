package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

public class TestDatabaseConnection {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @return a sample user context representing someone with restricted
     *         vision.
     */
    public static UserContext sampleUserContextRestricedVision() {
        final UserContext restrictedVision = new UserContext();
        final Context restrictedViewPc = new Context("computer operation system", "0");
        final Context restrictedViewTicketMachine = new Context("ticket machine", "1");
        restrictedVision.addContext(restrictedViewPc);
        restrictedVision.addContext(restrictedViewTicketMachine);
        restrictedViewPc.addPreference("/smalltext", "screen magnifier");
        restrictedViewPc.addPreference("/longtext", "screen reader");
        restrictedViewTicketMachine.addPreference("/all", "high contrast");
        restrictedViewTicketMachine.addPreference("/text", "large font");
        return restrictedVision;
    }

}
