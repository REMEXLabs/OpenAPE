package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Index extends SuperRestInterface {
    public static void setupIndexVELOCITYInterface() throws IllegalArgumentException, IOException {

        Spark.get("/index", (request, response) -> {
            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/index.vm"); // located
                                                                          // in
                                                                          // the
                                                                          // resources
                                                                          // directory

            }, new VelocityTemplateEngine());
    }
}
