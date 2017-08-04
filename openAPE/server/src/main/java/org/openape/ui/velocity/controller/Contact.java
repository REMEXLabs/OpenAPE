package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Contact  extends SuperRestInterface{
	private static Map<String, Object> model = new HashMap<>();
	public Contact() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public static void setupContactVELOCITYInterface() throws IllegalArgumentException, IOException {	 
    	 Spark.get("/contact", (request, response) -> {           
    		 
             MainController mainController = new MainController();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/contact.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
