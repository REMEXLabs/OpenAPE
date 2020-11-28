package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.OpenApeVelocityEngine;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Tutorials extends SuperRestInterface {
    public static void setupTutorialsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/tutorials", (request, response) -> {
            final Map<String, Object> model = new MainComponents().getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/tutorials.vm"); // located
                                                                              // in
                                                                              // the
                                                                              // resources
                                                                              // directory
            }, new OpenApeVelocityEngine());
    }

    public Tutorials() throws IllegalArgumentException, IOException {
        super();
    }
}
