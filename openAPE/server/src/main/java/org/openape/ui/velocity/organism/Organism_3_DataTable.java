package org.openape.ui.velocity.organism;

import java.io.IOException;
import org.openape.ui.velocity.molecules.Molecule_5_dataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class Organism_3_DataTable {
	
	public String generateAdministrationUserTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
	
		String administrationDatableContent = new Molecule_5_dataTableContent().generateAdministrationUserContent(adminsectionRequestHandler.getAllUsers());
		adminsectionRequestHandler.getAllUsercontexts();
		String administrationUserTable =""
			+ "<table id='user' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
	        + "<thead>"
	        + "<tr>"
	        + "<th>Username</th>"
	        + "<th>User ID</th>"
	        + "<th>Email</th>"
	        + "<th>Role</th>"
	        + "<th>Options</th>"
	        + "</tr>"
	        + "</thead>"
	        + "<tbody id='tableContent'>"
	        + administrationDatableContent
	        + "</tbody>"
	        + "</table>";
		return administrationUserTable;
		
	}
	
	public String generateAdministrationUserContextTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
		
		String administrationDatableUserContextContent = new Molecule_5_dataTableContent().generateUserContextContent(adminsectionRequestHandler.getAllUsercontexts());
		adminsectionRequestHandler.getAllUsercontexts();
		String administrationUserTable =""
			+ "<table id='userContextDataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
	        + "<thead>"
	        + "<tr>"
	        + "<th>Name</th>"
	        + "<th>ID</th>"
	        + "<th>isPublic</th>"
	        + "<th>Options</th>"
	        + "</tr>"
	        + "</thead>"
	        + "<tbody id='tableContent'>"
	        + administrationDatableUserContextContent
	        + "</tbody>"
	        + "</table>";
		return administrationUserTable;
		
	}
	
	public String generateAdministrationContextTable(AdminSectionRequestHandler adminsectionRequestHandler, String contextName) throws IllegalArgumentException, IOException{
		
		String administrationDatableContextContent = "";
		String idName = contextName.substring(0,1).toLowerCase() + contextName.substring(1).replace("-", "");
		
		if(contextName == "User-Context"){
			administrationDatableContextContent = new Molecule_5_dataTableContent().generateUserContextContent(adminsectionRequestHandler.getAllUsercontexts());
		} else if(contextName == "Task-Context") {
			administrationDatableContextContent = new Molecule_5_dataTableContent().generateTaskContextContent(adminsectionRequestHandler.getAllTaskContexts());
		} else if(contextName == "Equipment-Context") {
			administrationDatableContextContent = new Molecule_5_dataTableContent().generateEquipmentContextContent(adminsectionRequestHandler.getAllEquipmentContexts());
		} else if(contextName == "Environment-Context") {
			administrationDatableContextContent = new Molecule_5_dataTableContent().generateEnvironmentContextContent(adminsectionRequestHandler.getAllEnvironmentContexts());
		}
		
		String administrationContextTable =""
			+ "<table id='"+idName+"DataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
	        + "<thead>"
	        + "<tr>"
	        + "<th>ID</th>"
	        + "<th>UserID</th>"
	        + "<th>isPublic</th>"
	        + "<th>Options</th>"
	        + "</tr>"
	        + "</thead>"
	        + "<tbody id='tableContent'>"
	        + administrationDatableContextContent
	        + "</tbody>"
	        + "</table>";
		return administrationContextTable;
		
	}
}
