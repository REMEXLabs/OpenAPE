package org.openape.server.rest;

import spark.Spark;

public class ResourceManagerRESTInterface extends SuperRestInterface {
    public ResourceManagerRESTInterface() {
        super();

        /**
         * Get HTML interface.
         */
        Spark.get("/api/resource-manager", (req, res) -> {
            // try {
            // URL url = getClass().getResource("resource-manager.html");
            // // Return a String which has all
            // // the contents of the file.
            // Path path = Paths.get(url.toURI());
            // return new String(Files.readAllBytes(path),
            // Charset.defaultCharset());
            // } catch (IOException | URISyntaxException e) {
            // Add exception handlers here.
                // }
                return null;
            });
    }

}
