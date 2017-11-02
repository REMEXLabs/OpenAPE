package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.organism.Organism_4_Modals;
import org.openape.ui.velocity.requestHandler.MyResourcesRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class MyResources extends SuperRestInterface {
    public static void setupMyResourcesVELOCITYInterface(
            final MyResourcesRequestHandler myResourcesRequestHandler)
            throws IllegalArgumentException, IOException {

        Spark.get(
                "/myResources",
                (request, response) -> {
                    final MainComponents mainController = new MainComponents();
                    final Map<String, Object> model = mainController.getTemplateComponents();

                    model.put("dataTableResource", new Organism_3_DataTable()
                            .generateMyResourceTable(myResourcesRequestHandler));

                    // modals
                    model.put("deleteResourceModal",
                            new Organism_4_Modals().generateDeleteModal("Resource"));
                    model.put("addResourceModal",
                            new Organism_4_Modals().generateResourceModal("Add"));

                    model.put("editResourceModal",
                            new Organism_4_Modals().generateResourceModal("Edit"));

                    return new ModelAndView(model, "velocityTemplates/myResources.vm"); // located
                                                                                        // in
                                                                                        // the
                                                                                        // resources
                                                                                        // directory
                }, new VelocityTemplateEngine());
    }

    public MyResources() throws IllegalArgumentException, IOException {
        super();
    }
}
