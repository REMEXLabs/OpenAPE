package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.OpenApeVelocityEngine;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Downloads extends SuperRestInterface {

    public static void setupDownloadsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/downloads", (request, response) -> {
            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/downloads.vm"); // located
                                                                              // in
                                                                              // the
                                                                              // resources
                                                                              // directory

            }, new OpenApeVelocityEngine());
    }

    public Downloads() throws IllegalArgumentException, IOException {
        super();
    }
}
