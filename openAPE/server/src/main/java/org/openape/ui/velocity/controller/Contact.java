package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.OpenApeVelocityEngine;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Contact extends SuperRestInterface {
    public static void setupContactVELOCITYInterface() throws IllegalArgumentException, IOException {
        Spark.get("/contact", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/contact.vm"); // located
                                                                            // in
                                                                            // the
                                                                            // resources
                                                                            // directory

            }, new OpenApeVelocityEngine());
    }

    public Contact() throws IllegalArgumentException, IOException {
        super();
    }
}
