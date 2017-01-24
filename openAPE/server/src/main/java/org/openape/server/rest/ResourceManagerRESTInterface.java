package org.openape.server.rest;

import org.openape.api.Messages;

import spark.Spark;

public class ResourceManagerRESTInterface extends SuperRestInterface {
    public ResourceManagerRESTInterface() {
        super();

        /**
         * Get HTML interface.
         */
        Spark.get(Messages.getString("ResourceManagerRESTInterface.ResourceManagerURL"), (req, res) -> { //$NON-NLS-1$
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
