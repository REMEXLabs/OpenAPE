package org.openape.api.usercontext;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openape.server.database.mongoDB.TestDatabaseConnection;

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
            System.out.println(xml);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
