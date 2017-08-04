package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class LegalNotice  extends SuperRestInterface{
	private static Map<String, Object> model = new HashMap<>();
	public LegalNotice() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public static void setupLegalNoticeVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/legalNotice", (request, response) -> {           
    		 
             MainController mainController = new MainController();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/legalNotice.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
