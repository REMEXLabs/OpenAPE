package org.openape.ui.velocity.molecules;

import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.atoms.Atom_1_navigationLinks;

public class Molecule_5_dataTable {
	public String generateDataTableContent(ArrayList<User> listUsers){
		
		String tableContent = "";
				
		for(User users : listUsers){
			String roles = "";
			
			for (String role : users.getRoles())
			{
				roles += role;
			}
			tableContent +=  "<tr>"
					+ "<td>"+users.getUsername()+"</td>"
					+ "<td>"+users.getId()+"</td>"
					+ "<td>"+users.getEmail()+"</td>"
					+ "<td>"+roles+"</td>"
					+ "<td>"
					+ "<button id='"+users.getId()+"' class='btn btn-md btn-default'><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+users.getId()+"' class='btn btn-md btn-default' onClick='deleteUser(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+users.getId()+"' class='btn btn-md btn-default'><div class='glyphicon glyphicon-copy'></div> Copy to clipboard  </button> </td></tr>";
		}
		
				
		return tableContent;
	}
}

