package org.openape.ui.velocity.organism;

import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.molecules.Molecule_5_dataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class Organism_3_DataTable {
	
	public String generateAdministrationUserTable(AdminSectionRequestHandler adminsectionRequestHandler) throws IllegalArgumentException, IOException{
	
		String administrationDatableContent = new Molecule_5_dataTableContent().generateAdministrationUserContent(adminsectionRequestHandler.getAllUsers());
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
}
