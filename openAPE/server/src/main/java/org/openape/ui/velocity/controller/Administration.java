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
		 
    	 Spark.get("/administration", (request, response) -> {
    		
             model.put("footer", new Footer().generateFooter());
             model.put("logo", new Atom_2_OpenAPEHeader().generateLogo());
             model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
             model.put("subSection", new Organism_2_SubSection().generateTopNavigation());
             model.put("dataTableUser", new Organism_3_DataTable().generateAdministrationUserTable(adminsectionRequestHandler));
             model.put("dataTableUserContext", new Organism_3_DataTable().generateAdministrationUserContextTable(adminsectionRequestHandler));
             model.put("deleteUserModal", new Molecule_6_Modals().generateDeleteUserModal());
             model.put("deleteUserContextModal", new Molecule_6_Modals().generateDeleteUserContextModal());
             model.put("addUserContextModal", new Molecule_6_Modals().generateAddUserContextModal());
             model.put("editUserContextModal", new Molecule_6_Modals().generateEditUserContextModal());
             
         
             return new ModelAndView(model, "velocityTemplates/administration.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
