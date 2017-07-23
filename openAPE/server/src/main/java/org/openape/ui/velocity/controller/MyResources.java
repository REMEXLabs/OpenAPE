package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.atoms.Atom_2_OpenAPEHeader;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class MyResources  extends SuperRestInterface{
	private static Map<String, Object> model = new HashMap<>();
	public MyResources() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public static void setupMyResourcesVELOCITYInterface() throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/myResources", (request, response) -> {           
    		 
             MainController mainController = new MainController();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             return new ModelAndView(model, "velocityTemplates/myResources.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
