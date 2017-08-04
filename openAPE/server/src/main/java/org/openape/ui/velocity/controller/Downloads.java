package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;

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

			final MainController mainController = new MainController();
			final Map<String, Object> model = mainController.getTemplateComponents();

			return new ModelAndView(model, "velocityTemplates/downloads.vm"); // located
																				// in
																				// the
																				// resources
																				// directory
		} , new VelocityTemplateEngine());
	}
}
