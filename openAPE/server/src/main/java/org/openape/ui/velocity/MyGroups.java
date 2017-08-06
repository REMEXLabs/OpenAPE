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

public class MyGroups extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public MyGroups() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void setupMyGroupsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get(
                "/myGroups",
                (request, response) -> {

                    MyGroups.model.put("footer", new Footer().generateFooter());
                    MyGroups.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    MyGroups.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    MyGroups.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(MyGroups.model, "velocityTemplates/myGroups.vm"); // located
                                                                                              // in
                                                                                              // the
                                                                                              // resources
                                                                                              // directory
                }, new VelocityTemplateEngine());
    }
}
