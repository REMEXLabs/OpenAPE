package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Index extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public Index() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void setupIndexVELOCITYInterface() throws IllegalArgumentException, IOException {

        Spark.get("/index", (request, response) -> {

            final MainController mainController = new MainController();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/index.vm"); // located
                // in
                // the
                // resources
                // directory
            }, new VelocityTemplateEngine());
    }
}
