package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class MyGroups extends SuperRestInterface {
    public static void setupMyGroupsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/myGroups", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/myGroups.vm"); // located
                                                                             // in
                                                                             // the
                                                                             // resources
                                                                             // directory
            }, new VelocityTemplateEngine());
    }
}
