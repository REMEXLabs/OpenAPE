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

public class Downloads extends SuperRestInterface {
	private static Map<String, Object> model = new HashMap<>();

	public Downloads() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void setupDownloadsVELOCITYInterface() throws IllegalArgumentException, IOException {

		Spark.get("/downloads", (request, response) -> {

			Downloads.model.put("footer", new Footer().generateFooter());
			Downloads.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
			Downloads.model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
			Downloads.model.put("subSection", new Organism_2_SubSection().generateTopNavigation());

			return new ModelAndView(Downloads.model, "velocityTemplates/downloads.vm"); // located
																						// in
																						// the
																						// resources
																						// directory
		} , new VelocityTemplateEngine());
	}
}
