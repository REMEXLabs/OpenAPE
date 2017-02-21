package org.openape.api.usercontext;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.Main;

public class TestUserContext {

    @Test
    public void testEquals() {
        UserContext sample = Main.sampleUserContextRestricedVision();
        for (Context context : sample.getContexts()) {
            for (Preference preference : context.getPreferences()) {
                Assert.assertTrue(preference.equals(preference));
            }
            Assert.assertTrue(context.equals(context));
        }
        Assert.assertTrue(sample.equals(sample));
    }

}
