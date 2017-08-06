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

public class MyContexts extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupTutorialsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get(
                "/myContexts",
                (request, response) -> {

                    MyContexts.model.put("footer", new Footer().generateFooter());
                    MyContexts.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    MyContexts.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    MyContexts.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(MyContexts.model, "velocityTemplates/myContexts.vm"); // located
                    // in
                    // the
                    // resources
                    // directory
                }, new VelocityTemplateEngine());
    }

    public MyContexts() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
