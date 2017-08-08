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

public class MyProfile extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupMyProfileVELOCITYInterface() throws IllegalArgumentException, IOException {

        Spark.get("/myProfile", (request, response) -> {

            MyProfile.model.put("footer", new Footer().generateFooter());
            MyProfile.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
            MyProfile.model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
            MyProfile.model.put("subSection", new Organism_2_SubSection().generateTopNavigation());

            return new ModelAndView(MyProfile.model, "velocityTemplates/myProfile.vm"); // located
            // in
            // the
            // resources
            // directory
        } , new VelocityTemplateEngine());
    }

    public MyProfile() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}
