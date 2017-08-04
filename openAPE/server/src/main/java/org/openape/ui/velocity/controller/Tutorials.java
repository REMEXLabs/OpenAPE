package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Tutorials  extends SuperRestInterface{
	public static void setupTutorialsVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/tutorials", (request, response) -> {            
             Map<String, Object> model = new MainComponents().getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/tutorials.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
