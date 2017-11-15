package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Tutorials_Workflow extends SuperRestInterface {
    public static void setupTutorialsWorkflowVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/tutorials_workflow", (request, response) -> {
            final Map<String, Object> model = new MainComponents().getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/tutorials_workflow.vm"); // located
                                                                              // in
                                                                              // the
                                                                              // resources
                                                                              // directory
            }, new VelocityTemplateEngine());
    }

    public Tutorials_Workflow() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
