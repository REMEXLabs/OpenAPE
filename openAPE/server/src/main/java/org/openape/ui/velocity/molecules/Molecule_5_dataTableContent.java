package org.openape.ui.velocity.molecules;

import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
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
	
	
public String generateUserContextContent(ArrayList<String[]> listUserContexts){
		
		String tableContent = "";
				
		for(String[] userContext : listUserContexts){

			
			tableContent +=  "<tr>"
					+ "<td id='tdUserContextName_"+userContext[1]+"'>"+userContext[0]+"</td>"
					+ "<td>"+userContext[1]+"</td>"
					+ "<td>"+userContext[2]+"</td>"
					+ "<td>"
					+ "<button id='"+userContext[1]+"' class='btn btn-md btn-default' onClick='editUserContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+userContext[1]+"' class='btn btn-md btn-default' onClick='deleteUserContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+userContext[1]+"' class='btn btn-md btn-default' onClick='copyUserContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button> </td></tr>";
		}
		
				
		return tableContent;
	}
	
	public String generateTaskContextContent(ArrayList<String[]> listTaskContexts){
		
		String tableContent = "";
				
		for(String[] taskContext : listTaskContexts){
	
			
			tableContent +=  "<tr>"
					+ "<td id='tdTaskContextName_"+taskContext[0]+"'>"+taskContext[0]+"</td>"
					+ "<td>"+taskContext[1]+"</td>"
					+ "<td>"+taskContext[2]+"</td>"
					+ "<td>"
					+ "<button id='"+taskContext[0]+"' name='editTaskContext' class='btn btn-md btn-default' onClick='editTaskContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+taskContext[0]+"' class='btn btn-md btn-default' onClick='deleteTaskContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+taskContext[0]+"' class='btn btn-md btn-default' onClick='copyTaskContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button> </td></tr>";
		}
				
		return tableContent;
	}
}



