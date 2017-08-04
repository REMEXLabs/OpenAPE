package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class MyProfile extends SuperRestInterface {
	private static Map<String, Object> model = new HashMap<>();

	public MyProfile() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void setupMyProfileVELOCITYInterface() throws IllegalArgumentException, IOException {

		Spark.get("/myProfile", (request, response) -> {

			final MainController mainController = new MainController();
			final Map<String, Object> model = mainController.getTemplateComponents();

			return new ModelAndView(model, "velocityTemplates/myProfile.vm"); // located
																				// in
																				// the
																				// resources
																				// directory
		} , new VelocityTemplateEngine());
	}
}
