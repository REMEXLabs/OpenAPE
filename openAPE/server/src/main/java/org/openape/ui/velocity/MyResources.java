<<<<<<< HEAD
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

public class MyResources extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupMyResourcesVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get(
                "/myResources",
                (request, response) -> {

                    MyResources.model.put("footer", new Footer().generateFooter());
                    MyResources.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    MyResources.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    MyResources.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(MyResources.model, "velocityTemplates/myResources.vm"); // located
                    // in
                    // the
                    // resources
                    // directory
                }, new VelocityTemplateEngine());
    }

    public MyResources() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
=======
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

public class MyResources extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupMyResourcesVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get(
                "/myResources",
                (request, response) -> {

                    MyResources.model.put("footer", new Footer().generateFooter());
                    MyResources.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    MyResources.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    MyResources.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(MyResources.model, "velocityTemplates/myResources.vm"); // located
                                                                                                    // in
                                                                                                    // the
                                                                                                    // resources
                                                                                                    // directory
                }, new VelocityTemplateEngine());
    }

    public MyResources() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
>>>>>>> refs/heads/newMaster
