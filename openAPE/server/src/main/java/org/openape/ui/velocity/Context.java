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

public class Context extends SuperRestInterface {
	private static Map<String, Object> model = new HashMap<>();

	public Context() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void setupContextVELOCITYInterface() throws IllegalArgumentException, IOException {

		Spark.get("/context", (request, response) -> {

			Context.model.put("footer", new Footer().generateFooter());
			Context.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
			Context.model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
			Context.model.put("subSection", new Organism_2_SubSection().generateTopNavigation());

			return new ModelAndView(Context.model, "velocityTemplates/context.vm"); // located
																					// in
																					// the
																					// resources
																					// directory
		} , new VelocityTemplateEngine());
	}
}
