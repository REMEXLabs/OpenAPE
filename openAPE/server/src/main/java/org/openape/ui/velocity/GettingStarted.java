package org.openape.ui.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class GettingStarted  extends SuperRestInterface{
	private static Map<String, Object> model = new HashMap<>();
	public GettingStarted() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public static void setupGettingStartedVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/gettingStarted", (request, response) -> {           
    		 
             model.put("footer", new Footer().generateFooter());
             model.put("logo", new OpenapeLogo().generateOpenAPELogo());
             model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
             model.put("subSection", new Organism_2_SubSection().generateTopNavigation());
             
             return new ModelAndView(model, "velocityTemplates/gettingStarted.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
