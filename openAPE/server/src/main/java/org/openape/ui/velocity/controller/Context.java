package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.organism.Organism_4_Modals;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Context extends SuperRestInterface {
    public static void setupContextVELOCITYInterface(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        Spark.get("/context", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            final String[] destinations = { "User-Context", "Task-Context", "Equipment-Context",
                    "Environment-Context" };

            for (final String destination : destinations) {
                final String idName = destination.replace("-", "");

                // modals
                model.put("view" + idName + "Modal",
                        new Organism_4_Modals().generateViewContextModal(destination));

                model.put("dataTable" + idName, new Organism_3_DataTable()
                        .generateAdministrationContextTable(adminsectionRequestHandler,
                                destination, "context"));
            }

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
