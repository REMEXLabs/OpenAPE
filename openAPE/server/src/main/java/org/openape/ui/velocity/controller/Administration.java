package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.mainControllerComponents.MainComponents;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.organism.Organism_4_Modals;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Administration  extends SuperRestInterface{
	public Administration() throws IllegalArgumentException, IOException {
		super();
	}
    
	public static void setupAdministrationVELOCITYInterface(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException {
		 adminsectionRequestHandler.getAllTaskContexts();
    	 Spark.get("/administration", (request, response) -> { 		 
             //site components
    		 Map<String, Object> model = new MainComponents().getTemplateComponents();
             
             //unique datatable
             model.put("dataTableUserContext", new Organism_3_DataTable().generateAdministrationUserContextTable(adminsectionRequestHandler));
             
             String[] destinations = {"User", "User-Context", "Task-Context", "Equipment-Context", "Environment-Context"};
             
             //modals
             model.put("addGroupModal", new Organism_4_Modals().generateAddGroupModal());
             model.put("dataTableGroup", new Organism_3_DataTable().generateAdministrationGroupTable(adminsectionRequestHandler));             
             model.put("editGroupModal", new Organism_4_Modals().generateEditGroupModal());
             model.put("deleteGroupModal", new Organism_4_Modals().generateDeleteModal("Group"));
             
             for(String destination : destinations){
            	 String idName = destination.replace("-", "");
            	 
            	//modals
            	 model.put("delete"+idName+"Modal", new Organism_4_Modals().generateDeleteModal(destination));
            	 model.put("edit"+idName+"Modal", new Organism_4_Modals().generateEditContextModal(destination));
            	 model.put("add"+idName+"Modal", new Organism_4_Modals().generateAddContextModal(destination));
            	 
            	//Datatable creation
            	 if(destination == "User"){
                     model.put("dataTableUser", new Organism_3_DataTable().generateAdministrationUserTable(adminsectionRequestHandler));
            	 } else {
            		 model.put("dataTable"+idName, new Organism_3_DataTable().generateAdministrationContextTable(adminsectionRequestHandler, destination));    
            	 }     
             }
             
             return new ModelAndView(model, "velocityTemplates/administration.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
