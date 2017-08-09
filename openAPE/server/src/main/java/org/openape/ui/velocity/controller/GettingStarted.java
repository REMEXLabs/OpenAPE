package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class GettingStarted extends SuperRestInterface {
    public static void setupGettingStartedVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/gettingStarted", (request, response) -> {
            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/gettingStarted.vm"); // located
                                                                                   // in
                                                                                   // the
                                                                                   // resources
                                                                                   // directory
            }, new VelocityTemplateEngine());
    }

    public GettingStarted() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
