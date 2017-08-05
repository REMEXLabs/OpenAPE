package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;
import org.openape.ui.velocity.controllerComponents.myContexts;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.organism.Organism_4_Modals;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class MyContexts  extends SuperRestInterface{
	public MyContexts() throws IllegalArgumentException, IOException {
		super();
	}
	
	public static String Test(){
		return "hi";
		
		
	}
    
	public static void setupMyContextsVELOCITYInterface(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException {
		 
    	 Spark.get("/myContexts", (request, response) -> {           
    		 
             MainComponents mainController = new MainComponents();
             Map<String, Object> model = mainController.getTemplateComponents();
             
             String[] destinations = {"User", "User-Context", "Task-Context", "Equipment-Context", "Environment-Context"};
             
             for(String destination : destinations){
            	 String idName = destination.replace("-", "");
            	 
            	//modals
            	 model.put("delete"+idName+"Modal", new Organism_4_Modals().generateDeleteModal(destination));
            	 model.put("edit"+idName+"Modal", new Organism_4_Modals().generateEditContextModal(destination));
            	 model.put("add"+idName+"Modal", new Organism_4_Modals().generateAddContextModal(destination));
            	 model.put("dataTable"+idName, new Organism_3_DataTable().generateAdministrationContextTable(adminsectionRequestHandler, destination)); 
             }
             
             model.put("test", new myContexts());
             
             return new ModelAndView(model, "velocityTemplates/myContexts.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
