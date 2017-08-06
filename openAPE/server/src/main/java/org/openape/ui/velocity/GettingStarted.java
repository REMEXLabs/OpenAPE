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

public class GettingStarted extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupGettingStartedVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/gettingStarted", (request, response) -> {

            GettingStarted.model.put("footer", new Footer().generateFooter());
            GettingStarted.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
            GettingStarted.model.put("topNavigation",
                    new Organism_1_Topsection().generateTopNavigation());
            GettingStarted.model.put("subSection",
                    new Organism_2_SubSection().generateTopNavigation());

            return new ModelAndView(GettingStarted.model, "velocityTemplates/gettingStarted.vm"); // located
                // in
                // the
                // resources
                // directory
            }, new VelocityTemplateEngine());
    }

    public GettingStarted() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
