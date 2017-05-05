package org.openape.web;

import org.openape.server.rest.SuperRestInterface;

import spark.servlet.SparkApplication;

public class Init implements SparkApplication {

    @Override
    public void init() {
        // Start rest api.
        new SuperRestInterface();
    }

}
