package org.openape.api.usercontext;

import java.io.IOException;
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
        Assert.assertTrue(sample.equals(UserContext.getObjectFromJson(sample.getBackEndJson())));

    }

    @Test
    public void testGetXML() {
        final UserContext sample = TestDatabaseConnection.sampleUserContext();
        try {
            final String xml = sample.getXML();
            // System.out.println(xml);
            final UserContext userContext = UserContext.getObjectFromXml(xml);
            Assert.assertTrue(userContext.equals(sample));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXmlTypeAdding() {
        Scanner scanner = new Scanner(getClass().getResourceAsStream("sample.xml"));
        String s = new String();
        while(scanner.hasNextLine()){
                s = s + scanner.nextLine();
         }
        scanner.close();
        UserContext.getObjectFromXml(s);
    }
}
