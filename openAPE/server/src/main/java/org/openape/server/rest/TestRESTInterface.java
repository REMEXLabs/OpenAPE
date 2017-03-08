package org.openape.server.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import spark.Spark;

public class TestRESTInterface extends SuperRestInterface {
    public static void setupResourceManagerRESTInterface() {
        /**
         * Returns HTML interface that tests the user context rest interface.
         */
        Spark.get(
                "/api/tests",
                (req, res) -> {
                    String htmlInterface = "";
                    try {
                        // Get file contents of the html file to send.
                        InputStream inputStream = TestRESTInterface.class.getClass()
                                .getClassLoader()
                                .getResourceAsStream("/resources/webcontent/restTests.html");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                inputStream));
                        StringBuilder fullContents = new StringBuilder();
                        String line;
                        while((line = bufferedReader.readLine()) != null) {
                           fullContents.append(line);
                        }
                        htmlInterface = fullContents.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    res.type("text/html");
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    return htmlInterface;
                });
    }

}
