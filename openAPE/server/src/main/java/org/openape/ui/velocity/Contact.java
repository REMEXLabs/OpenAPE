package org.openape.ui.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Contact extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public Contact() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void setupContactVELOCITYInterface() throws IllegalArgumentException, IOException {

        Spark.get(
                "/contact",
                (request, response) -> {

                    Contact.model.put("footer", new Footer().generateFooter());
                    Contact.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    Contact.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    Contact.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(Contact.model, "velocityTemplates/contact.vm"); // located
                    // in
                    // the
                    // resources
                    // directory
                }, new VelocityTemplateEngine());
    }
}
