package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class GettingStarted  extends SuperRestInterface{
	public static void setupGettingStartedVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/gettingStarted", (request, response) -> {           
    		 
            MainComponents mainController = new MainComponents();
            Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/gettingStarted.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
