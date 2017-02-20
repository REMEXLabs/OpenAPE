package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openape.api.usercontext.UserContext;
import org.openape.api.usercontext.Context;
import org.openape.server.Main;

public class TestDatabaseConnection {
    private DatabaseConnection dataBaseConnection;

    @Before
    public void init() {
        dataBaseConnection = DatabaseConnection.getInstance();
    }

    @Test
    public void testDataStoreage() {
        UserContext sampleContext = Main.sampleUserContextRestricedVision();
        try {
            // test insert.
            String id;
            Assert.assertNotEquals(
                    "",
                    id = dataBaseConnection.storeData(MongoCollectionTypes.USERCONTEXT,
                            sampleContext));
            System.out.println(id);
            // test get.
            Assert.assertEquals(sampleContext,
                    dataBaseConnection.getData(MongoCollectionTypes.USERCONTEXT, id));
            // remove second context.
            List<Context> newContexts = new ArrayList<Context>();
            newContexts.add(sampleContext.getContexts().get(0));
            sampleContext.setContexts(newContexts);
            // test update
            dataBaseConnection.updateData(MongoCollectionTypes.USERCONTEXT, sampleContext, id);
            Assert.assertEquals(sampleContext,
                    dataBaseConnection.getData(MongoCollectionTypes.USERCONTEXT, id));
            // test delete
            Assert.assertTrue(dataBaseConnection.deleteData(MongoCollectionTypes.USERCONTEXT, id));

        } catch (ClassCastException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
