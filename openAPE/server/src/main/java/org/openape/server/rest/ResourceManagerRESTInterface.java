package org.openape.server.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openape.server.requestHandler.ResourceRequestHandler;

import spark.Spark;

public class ResourceManagerRESTInterface extends SuperRestInterface {
    public ResourceManagerRESTInterface(final ResourceRequestHandler requestHandler) {
        super();

        /**
         * Get HTML interface.
         */
        Spark.get("/api/resource-manager", (req, res) -> {
        	try {
                URL url = getClass().getResource("resource-manager.html");
                // Return a String which has all
                // the contents of the file.
                Path path = Paths.get(url.toURI());
                return new String(Files.readAllBytes(path), Charset.defaultCharset());
            } catch (IOException | URISyntaxException e) {
                // Add exception handlers here.
            }
            return null;
        });
    }

}
