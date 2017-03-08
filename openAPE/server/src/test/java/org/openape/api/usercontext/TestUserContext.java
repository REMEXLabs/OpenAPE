package org.openape.api.usercontext;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.database.mongoDB.TestDatabaseConnection;

public class TestUserContext {

    @Test
    public void testEquals() {
        final UserContext sample = TestDatabaseConnection.sampleUserContextRestricedVision();
        for (final Context context : sample.getContexts()) {
            for (final Preference preference : context.getPreferences()) {
                Assert.assertTrue(preference.equals(preference));
            }
            Assert.assertTrue(context.equals(context));
        }
        Assert.assertTrue(sample.equals(sample));
    }

}
