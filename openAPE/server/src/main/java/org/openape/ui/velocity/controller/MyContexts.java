package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.mainControllerComponents.MainComponents;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class MyContexts  extends SuperRestInterface{
	public MyContexts() throws IllegalArgumentException, IOException {
		super();
	}
    
	public static void setupTutorialsVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/myContexts", (request, response) -> {           
    		 
             MainComponents mainController = new MainComponents();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/myContexts.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
