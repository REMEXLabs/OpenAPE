package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class MyProfile extends SuperRestInterface {
    public static void setupMyProfileVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/myProfile", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/myProfile.vm"); // located
                                                                              // in
                                                                              // the
                                                                              // resources
                                                                              // directory
            }, new VelocityTemplateEngine());
    }
}
