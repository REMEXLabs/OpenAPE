package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class getAllUsers extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupContactVELOCITYInterface() throws IllegalArgumentException, IOException {

        Spark.get(
                "/contact",
                (request, response) -> {

                    getAllUsers.model.put("footer", new Footer().generateFooter());
                    getAllUsers.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    getAllUsers.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    getAllUsers.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(getAllUsers.model, "velocityTemplates/contact.vm"); // located
                    // in
                    // the
                    // resources
                    // directory
                }, new VelocityTemplateEngine());
    }

    public getAllUsers() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
