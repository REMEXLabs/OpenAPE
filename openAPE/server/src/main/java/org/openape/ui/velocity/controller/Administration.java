package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
             model.put("deleteUserModal", new Molecule_6_Modals().generateDeleteContextModal("User"));
             
             model.put("dataTableTaskContext", new Organism_3_DataTable().generateAdministrationTaskContextTable(adminsectionRequestHandler));
             
           
             //modals
             model.put("deleteUserContextModal", new Molecule_6_Modals().generateDeleteContextModal("User-Context"));
             model.put("addUserContextModal", new Molecule_6_Modals().generateAddContextModal("User-Context"));
             model.put("addTaskContextModal", new Molecule_6_Modals().generateAddContextModal("Task-Context"));
             model.put("editUserContextModal", new Molecule_6_Modals().generateEditContextModal("User-Context"));
             model.put("editTaskContextModal", new Molecule_6_Modals().generateEditContextModal("Task-Context"));
             model.put("deleteTaskContextModal", new Molecule_6_Modals().generateDeleteContextModal("Task-Context"));
             
             
             
             
         
             return new ModelAndView(model, "velocityTemplates/administration.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
