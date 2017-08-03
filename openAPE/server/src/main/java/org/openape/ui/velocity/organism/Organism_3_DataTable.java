package org.openape.ui.velocity.organism;

import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.molecules.Molecule_5_DataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class Organism_3_DataTable {
public String generateDataTableContent(ArrayList<User> listUsers){
		
		String tableContent = "";
				
		for(User users : listUsers){
			String roles = "";
			String username = "";
			String email = "";
			
			for (String role : users.getRoles())
			{
				roles += role;
			}
			
			if(users.getUsername().contains("#046")){
				username = users.getUsername().replace("#046", ".");
			} else {
				username = users.getUsername();
			}
			
			if(users.getEmail().contains("#046")){
				email = users.getEmail().replace("#046", ".");
			} else {
				email = users.getEmail();
			}
			
			
			tableContent +=  "<tr>"
					+ "<td id='tdUserName_"+users.getId()+"'>"+username+"</td>"
					+ "<td>"+users.getId()+"</td>"
					+ "<td id='tdEmail_"+users.getId()+"'>"+email+"</td>"
					+ "<td id='tdRoles_"+users.getId()+"'>"+roles+"</td>"
					+ "<td>"
					+ "<button id='"+users.getId()+"' class='btn btn-md btn-default' onClick='editUser(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+users.getId()+"' class='btn btn-md btn-default' onClick='deleteUser(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+users.getId()+"' class='btn btn-md btn-default'><div class='glyphicon glyphicon-copy'></div> Copy to clipboard  </button> </td></tr>";
		}
		
				
		return tableContent;
	}
	
	public String generateAdministrationUserTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
	
		String administrationDatableContent = new Molecule_5_DataTableContent().generateAdministrationUserContent(adminsectionRequestHandler.getAllUsers());
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
		
		String administrationDatableUserContextContent = new Molecule_5_DataTableContent().generateUserContextContent(adminsectionRequestHandler.getAllUsercontexts());
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
			administrationDatableContextContent = new Molecule_5_DataTableContent().generateUserContextContent(adminsectionRequestHandler.getAllUsercontexts());
		} else if(contextName == "Task-Context") {
			administrationDatableContextContent = new Molecule_5_DataTableContent().generateTaskContextContent(adminsectionRequestHandler.getAllTaskContexts());
		} else if(contextName == "Equipment-Context") {
			administrationDatableContextContent = new Molecule_5_DataTableContent().generateEquipmentContextContent(adminsectionRequestHandler.getAllEquipmentContexts());
		} else if(contextName == "Environment-Context") {
			administrationDatableContextContent = new Molecule_5_DataTableContent().generateEnvironmentContextContent(adminsectionRequestHandler.getAllEnvironmentContexts());
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
	
	
public String generateAdministrationGroupTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
		
		String administrationDatableGroupContent =  new Molecule_5_DataTableContent().generateGroupContent(/*adminsectionRequestHandler.getAllEnvironmentContexts()*/);
		
		String administrationContextTable =""
			+ "<table id='groupDataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
	        + "<thead>"
	        + "<tr>"
	        + "<th >ID</th>"
	        + "<th >Group name</th>"
	        + "<th >Options</th>"
	        + "</tr>"
	        + "</thead>"
	        + "<tbody id='tableContent'>"
	        + administrationDatableGroupContent
	        + "</tbody>"
	        + "</table>";
		return administrationContextTable;
		
	}
}
