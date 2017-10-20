package org.openape.api.usercontext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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
    public void testGetJson() throws IOException {
        final UserContext sample = TestDatabaseConnection.sampleUserContext();
        final String json = sample.getBackEndJson();
        System.out.println(json);
        Assert.assertTrue(sample.equals(UserContext.getObjectFromJson(json)));

    }

    @Test
    public void testGetXML() {
        final UserContext sample = TestDatabaseConnection.sampleUserContext();
        try {
            final String xml = sample.getXML();
            System.out.println(xml);
            final UserContext userContext = UserContext.getObjectFromXml(xml);
            Assert.assertTrue(userContext.equals(sample));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void testXmlTypeAdding() {
        Class<? extends TestUserContext> class1 = this.getClass();
        InputStream ioStream = class1.getResourceAsStream("sample.xml");
        final Scanner scanner = new Scanner(ioStream);
        String s = new String();
        while (scanner.hasNextLine()) {
            s = s + scanner.nextLine();
        }
        scanner.close();
        UserContext.getObjectFromXml(s);
    }
}
