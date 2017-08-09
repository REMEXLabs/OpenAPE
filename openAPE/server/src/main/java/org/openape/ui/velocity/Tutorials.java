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

public class Tutorials extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupTutorialsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get(
                "/tutorials",
                (request, response) -> {

                    Tutorials.model.put("footer", new Footer().generateFooter());
                    Tutorials.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    Tutorials.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    Tutorials.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(Tutorials.model, "velocityTemplates/tutorials.vm"); // located
                    // in
                    // the
                    // resources
                    // directory
                }, new VelocityTemplateEngine());
    }

    public Tutorials() throws IllegalArgumentException, IOException {
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

public class Tutorials extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupTutorialsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get(
                "/tutorials",
                (request, response) -> {

                    Tutorials.model.put("footer", new Footer().generateFooter());
                    Tutorials.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
                    Tutorials.model.put("topNavigation",
                            new Organism_1_Topsection().generateTopNavigation());
                    Tutorials.model.put("subSection",
                            new Organism_2_SubSection().generateTopNavigation());

                    return new ModelAndView(Tutorials.model, "velocityTemplates/tutorials.vm"); // located
                                                                                                // in
                                                                                                // the
                                                                                                // resources
                                                                                                // directory
                }, new VelocityTemplateEngine());
    }

    public Tutorials() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
>>>>>>> refs/heads/newMaster
