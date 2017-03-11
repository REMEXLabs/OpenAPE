package org.openape.server.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import spark.Spark;

public class ResourceManagerRESTInterface extends SuperRestInterface {
    public static void setupResourceManagerRESTInterface() {
        /**
         * Get HTML interface.
         */
        Spark.get(
                "/resource-manager",
                (req, res) -> {
                    String htmlInterface = "";
                    try {
                        // Get file contents of the html file to send.
                        final InputStream inputStream = ResourceManagerRESTInterface.class
                                .getClassLoader().getResourceAsStream(
                                        File.separator + "webcontent" + File.separator
                                                + "resource-manager.html");
                        final BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(inputStream, "UTF-8"));
                        final StringBuilder fullContents = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            fullContents.append(line + "\n");
                        }
                        htmlInterface = fullContents.toString();
                    } catch (final Exception e) {
                        e.printStackTrace();
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                    res.type("text/html");
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    return htmlInterface;
                });
    }

}
