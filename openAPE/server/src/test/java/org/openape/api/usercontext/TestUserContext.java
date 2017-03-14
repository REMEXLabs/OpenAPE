package org.openape.api.usercontext;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.database.mongoDB.TestDatabaseConnection;

public class TestUserContext {

    @Test
    public void testEquals() {
        final UserContext sample = TestDatabaseConnection.sampleUserContextRestricedVision();
        for (final Context context : sample.getContexts()) {
            Assert.assertTrue(context.equals(context));
        }
        Assert.assertTrue(sample.equals(sample));
    }

}
