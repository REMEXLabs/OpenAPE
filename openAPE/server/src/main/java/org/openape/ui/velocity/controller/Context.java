package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Context  extends SuperRestInterface{
	public static void setupContextVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/context", (request, response) -> {           
    		 
             MainComponents mainController = new MainComponents();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/context.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
