package org.openape.ui.velocity.molecules;

import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.atoms.Atom_1_navigationLinks;

public class Molecule_5_dataTableContent {
	
	public String generateAdministrationUserContent(ArrayList<User> listUsers){
		
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
}

