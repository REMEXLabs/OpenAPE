package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Context extends SuperRestInterface {
    public static void setupContextVELOCITYInterface() throws IllegalArgumentException, IOException {

        Spark.get("/context", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/context.vm"); // located
                                                                            // in
                                                                            // the
                                                                            // resources
                                                                            // directory
            }, new VelocityTemplateEngine());
    }

    public Context() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
