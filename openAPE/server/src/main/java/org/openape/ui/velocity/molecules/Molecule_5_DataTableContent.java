package org.openape.ui.velocity.molecules;

import java.util.ArrayList;
import org.openape.api.user.User;

public class Molecule_5_DataTableContent {
	
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
					+ "<button id='edit_"+users.getId()+"' class='btn btn-md btn-default' onClick='editUser(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='delete_"+users.getId()+"' class='btn btn-md btn-default' onClick='deleteUser(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button></tr>";
		}
		
				
		return tableContent;
	}
	
	
public String generateUserContextContent(ArrayList<String[]> listUserContexts){
		
		String tableContent = "";
				
		for(String[] userContext : listUserContexts){

			
			tableContent +=  "<tr>"
					+ "<td id='tdUserContextName_"+userContext[1]+"'>"+userContext[1]+"</td>"
					+ "<td>"+userContext[0]+"</td>"
					+ "<td>"+userContext[3]+"</td>"
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
					+ "<td>"+taskContext[3]+"</td>"
					+ "<td>"+taskContext[2]+"</td>"
					+ "<td>"
					+ "<button id='"+taskContext[0]+"' name='editTaskContext' class='btn btn-md btn-default' onClick='editTaskContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+taskContext[0]+"' class='btn btn-md btn-default' onClick='deleteTaskContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+taskContext[0]+"' class='btn btn-md btn-default' onClick='copyTaskContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button> </td></tr>";
		}
				
		return tableContent;
	}
	
	public String generateEquipmentContextContent(ArrayList<String[]> listEquipmentContexts){
		
		String tableContent = "";
				
		for(String[] equipmentkContext : listEquipmentContexts){
	
			
			tableContent +=  "<tr>"
					+ "<td id='tdEquipmentContextName_"+equipmentkContext[0]+"'>"+equipmentkContext[0]+"</td>"
					+ "<td>"+equipmentkContext[1]+"</td>"
					+ "<td>"+equipmentkContext[3]+"</td>"
					+ "<td>"+equipmentkContext[2]+"</td>"
					+ "<td>"
					+ "<button id='"+equipmentkContext[0]+"' name='editEquipmentContext' class='btn btn-md btn-default' onClick='editEquipmentContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+equipmentkContext[0]+"' class='btn btn-md btn-default' onClick='deleteEquipmentContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+equipmentkContext[0]+"' class='btn btn-md btn-default' onClick='copyEquipmentContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button> </td></tr>";
		}
				
		return tableContent;
	}
	
	public String generateEnvironmentContextContent(ArrayList<String[]> listEnvironmentContexts){
		
		String tableContent = "";
				
		for(String[] environmentContext : listEnvironmentContexts){
	
			
			tableContent +=  "<tr>"
					+ "<td id='tdEnvironmentContextName_"+environmentContext[0]+"'>"+environmentContext[0]+"</td>"
					+ "<td>"+environmentContext[1]+"</td>"
					+ "<td>"+environmentContext[3]+"</td>"
					+ "<td>"+environmentContext[2]+"</td>"
					+ "<td>"
					+ "<button id='"+environmentContext[0]+"' name='editEnvironmentContext' class='btn btn-md btn-default' onClick='editEnvironmentContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button id='"+environmentContext[0]+"' class='btn btn-md btn-default' onClick='deleteEnvironmentContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
					+ "<button id='"+environmentContext[0]+"' class='btn btn-md btn-default' onClick='copyEnvironmentContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button> </td></tr>";
		}
				
		return tableContent;
	}


	public String generateGroupContent() {
		// TODO Auto-generated method stub
		String tableContent = "";
					
		tableContent +=  "<tr>"
					+ "<td>4</td>"
					+ "<td>G1</td>"
					+ "<td>"
					+ "<button class='btn btn-md btn-default' onClick='editGroup(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
					+ "<button class='btn btn-md btn-default' onClick='deleteGroup(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button></tr>";

		return tableContent;
	}
}



