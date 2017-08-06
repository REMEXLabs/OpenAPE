package org.openape.ui.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.api.user.User;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.models.deleteUser;
import org.openape.ui.velocity.molecules.Molecule_5_dataTable;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Adminsection  extends SuperRestInterface{
	private static Map<String, Object> model = new HashMap<>();
	public Adminsection() throws IllegalArgumentException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

    public static Boolean ich(){
    	System.out.println("ich bin gerufen");
    	return true;
    }
    
	public static void setupAdminVELOCITYInterface(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException {

        for(User entry : adminsectionRequestHandler.getAllUsers()){
        	System.out.println(entry.getId());
        }
		 
    	 Spark.get("/adminsection", (request, response) -> {
    		 User user = new User();
    		 user.setEmail("wjaufmann");
    		 
    		 deleteUser del = new deleteUser();
    		 
    		 for(User entry : del.getAdminSectionRequestHandler().getAllUsers()){
    			 System.out.println(entry.getId());
    		 }
 
            
             model.put("footer", new Footer().generateFooter());
             model.put("logo", new OpenapeLogo().generateOpenAPELogo());
             model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
             model.put("subSection", new Organism_2_SubSection().generateTopNavigation());
             model.put("tableContent", new Molecule_5_dataTable().generateDataTableContent(adminsectionRequestHandler.getAllUsers()));
             model.put("deleteUser", del);
            
             
             return new ModelAndView(model, "velocityTemplates/adminsection.vm"); // located in the resources directory
         }, new VelocityTemplateEngine());
    }
}
