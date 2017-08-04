package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class LegalNotice  extends SuperRestInterface{
	public static void setupLegalNoticeVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/legalNotice", (request, response) -> {           
    		 
             MainComponents mainController = new MainComponents();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/legalNotice.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
