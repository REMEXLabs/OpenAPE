package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class GettingStarted extends SuperRestInterface {
	public GettingStarted() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void setupGettingStartedVELOCITYInterface() throws IllegalArgumentException, IOException {

		Spark.get("/gettingStarted", (request, response) -> {

			final MainController mainController = new MainController();
			final Map<String, Object> model = mainController.getTemplateComponents();

			return new ModelAndView(model, "velocityTemplates/gettingStarted.vm"); // located
																					// in
																					// the
																					// resources
																					// directory
		} , new VelocityTemplateEngine());
	}
}
