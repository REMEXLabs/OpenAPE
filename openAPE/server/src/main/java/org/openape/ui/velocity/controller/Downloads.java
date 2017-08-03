package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.mainControllerComponents.MainComponents;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Downloads  extends SuperRestInterface{
    
	public static void setupDownloadsVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/downloads", (request, response) -> {           
    		 
             MainComponents mainController = new MainComponents();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/downloads.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
