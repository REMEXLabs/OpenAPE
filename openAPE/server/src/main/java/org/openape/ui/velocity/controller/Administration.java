package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.atoms.Atom_2_OpenAPEHeader;
import org.openape.ui.velocity.molecules.Molecule_5_dataTableContent;
import org.openape.ui.velocity.molecules.Molecule_6_Modals;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Administration  extends SuperRestInterface{
	private static Map<String, Object> model = new HashMap<>();
	public Administration() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public static void setupAdministrationVELOCITYInterface(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException {
		 adminsectionRequestHandler.getAllTaskContexts();
    	 Spark.get("/administration", (request, response) -> {
    		
    		 
             model.put("footer", new Footer().generateFooter());
             model.put("logo", new Atom_2_OpenAPEHeader().generateLogo());
             model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
             model.put("subSection", new Organism_2_SubSection().generateTopNavigation());
             model.put("dataTableUser", new Organism_3_DataTable().generateAdministrationUserTable(adminsectionRequestHandler));
             model.put("dataTableUserContext", new Organism_3_DataTable().generateAdministrationUserContextTable(adminsectionRequestHandler));
             model.put("dataTableEquipmentContext", new Organism_3_DataTable().generateAdministrationContextTable(adminsectionRequestHandler, "Equipment-Context"));

             
             model.put("dataTableTaskContext", new Organism_3_DataTable().generateAdministrationContextTable(adminsectionRequestHandler, "Task-Context"));
             
           
             String[] contexts = {"User", "User-Context", "Task-Context", "Equipment-Context", "Environment-Context"};
             
             for(String context : contexts){
            	 String contextIdName = context.replace("-", "");
            	 model.put("delete"+contextIdName+"Modal", new Molecule_6_Modals().generateDeleteContextModal(context));
            	 model.put("edit"+contextIdName+"Modal", new Molecule_6_Modals().generateEditContextModal(context));
            	 model.put("add"+contextIdName+"Modal", new Molecule_6_Modals().generateAddContextModal(context));
             }
             
         
             return new ModelAndView(model, "velocityTemplates/administration.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
