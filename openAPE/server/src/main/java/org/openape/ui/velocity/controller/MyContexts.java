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

public class MyContexts extends SuperRestInterface {
    public static void setupMyContextsVELOCITYInterface(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        Spark.get("/myContexts", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            final String[] destinations = { "User", "User-Context", "Task-Context",
                    "Equipment-Context", "Environment-Context" };

            for (final String destination : destinations) {
                final String idName = destination.replace("-", "");

                // modals
                model.put("delete" + idName + "Modal",
                        new Organism_4_Modals().generateDeleteModal(destination));
                model.put("edit" + idName + "Modal",
                        new Organism_4_Modals().generateContextModal(destination, "Edit"));
                model.put("add" + idName + "Modal",
                        new Organism_4_Modals().generateContextModal(destination, "Add"));
                model.put("dataTable" + idName, new Organism_3_DataTable()
                        .generateAdministrationContextTable(adminsectionRequestHandler,
                                destination, "myContext"));
            }

            return new ModelAndView(model, "velocityTemplates/myContexts.vm");

        }, new VelocityTemplateEngine());
    }

    public MyContexts() throws IllegalArgumentException, IOException {
        super();
    }
}
