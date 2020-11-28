package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.OpenApeVelocityEngine;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class LegalNotice extends SuperRestInterface {
    public static void setupLegalNoticeVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/legalNotice", (request, response) -> {
            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            return new ModelAndView(model, "velocityTemplates/legalNotice.vm"); // located
                                                                                // in
                                                                                // the
                                                                                // resources
                                                                                // directory
            }, new OpenApeVelocityEngine());
    }

    public LegalNotice() throws IllegalArgumentException, IOException {
        super();
    }
}
