package org.openape.ui.velocity.organism;

import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.molecules.Molecule_5_dataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class Organism_3_DataTable {
	
	public String generateAdministrationUserTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
	
		String administrationDatableContent = new Molecule_5_dataTableContent().generateAdministrationUserContent(adminsectionRequestHandler.getAllUsers());
		adminsectionRequestHandler.getAllUsercontexts();
		String administrationUserTable =""
			+ "<table id='example' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
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
	
	public String generateAdministrationTaskContextTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
		
		String administrationDatableTaskContextContent = new Molecule_5_dataTableContent().generateTaskContextContent(adminsectionRequestHandler.getAllTaskContexts());
		String administrationTaskContextTable =""
			+ "<table id='taskContextDataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
	        + "<thead>"
	        + "<tr>"
	        + "<th>ID</th>"
	        + "<th>UserID</th>"
	        + "<th>isPublic</th>"
	        + "<th>Options</th>"
	        + "</tr>"
	        + "</thead>"
	        + "<tbody id='tableContent'>"
	        + administrationDatableTaskContextContent
	        + "</tbody>"
	        + "</table>";
		return administrationTaskContextTable;
		
	}
}
