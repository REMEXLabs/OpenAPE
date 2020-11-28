package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.OpenApeVelocityEngine;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Tutorials_Workflow extends SuperRestInterface {
    public static void setupTutorialsWorkflowVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/workflow", (request, response) -> { return getTutorial("workflow.vm");},new OpenApeVelocityEngine());
        Spark.get("/openape_js", (request, response) -> { return getTutorial("openape_js.vm");},new OpenApeVelocityEngine());
        Spark.get("/userContextTutorial", (request, response) -> { return getTutorial("userContextTutorial.vm");},new OpenApeVelocityEngine());
    }

    static ModelAndView getTutorial(String fileName) {
        final Map<String, Object> model = new MainComponents().getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/tutorials/" + fileName); // located      in the  resources directory
                 }

    public Tutorials_Workflow() throws IllegalArgumentException, IOException {
        super();
    }
}
