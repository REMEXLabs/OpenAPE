package org.openape.ui.velocity.organism;

import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.openape.ui.velocity.molecules.Molecule_5_DataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.openape.ui.velocity.requestHandler.GroupsRequestHandler;
import org.openape.ui.velocity.requestHandler.MyResourcesRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Organism_3_DataTable {
	
	private static Logger logger = LoggerFactory.getLogger(Organism_3_DataTable.class );
	
    /**TODO comment all parameers
     *creates the HTML table for all  and public contexts 
     * @param adminsectionRequestHandler
     * @param contextType
     * @param b 
     * indicates if function is called by "myContexts" or "contexts"
     * @param userId
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public String generateAdministrationContextTable(
            final AdminSectionRequestHandler adminsectionRequestHandler, final String contextType,
            final boolean  privateContexts, final String userId) throws IllegalArgumentException, IOException {

    	
    	    	
        String administrationDatableContextContent = "";
        final String idName = contextType.substring(0, 1).toLowerCase()
                + contextType.substring(1).replace("-", "");

        
        
if (privateContexts) {
	administrationDatableContextContent 	 = createPrivateTableContent(contextType,  userId);
} else {
	administrationDatableContextContent 	 = createPublicTableContent(contextType);
}
        final String administrationContextTable = ""
                + "<table id='"
                + idName
                + "DataTable' style='border: 4px solid #ddd !important;' class='table table-striped table-bordered dt-responsive nowrap' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>ID</th>" + "<th>Owner</th>"
                + "<th>Public</th>" + "<th>Options</th>" + "</tr>" + "</thead>"
                + "<tbody id='tableContent'>" + administrationDatableContextContent + "</tbody>"
                + "</table>";
        // todo debugging
        logger.debug("administrationContextTable: " + administrationContextTable);
        return administrationContextTable;

    }

    private String createPublicTableContent(String contextType) throws IOException {
    	String administrationDatableContextContent = ""; 
    	if (contextType == "User-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateUserContextContent(UserContextRequestHandler.getInstance().getPublicContexts(),
                            false);
        } else if (contextType == "Task-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateTaskContextContent(TaskContextRequestHandler.getInstance().getPublicContexts(),
                            false);
        } else if (contextType == "Equipment-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateEquipmentContextContent(
                            EquipmentContextRequestHandler.getInstance().getPublicContexts(), false);
        } else if (contextType == "Environment-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateEnvironmentContextContent(
                            EnvironmentContextRequestHandler.getInstance().getPublicContexts(), false);
        }
return administrationDatableContextContent; 
		

		
	}

	String administrationDatableContextContent = ""; 
    private String createPrivateTableContent(String contextType,  String userId) throws IOException{
        if (contextType == "User-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateUserContextContent(UserContextRequestHandler.getInstance().getContextsOfUser(userId),
                            true);
        } else if (contextType == "Task-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateTaskContextContent(TaskContextRequestHandler.getInstance().getContextsOfUser(userId),
                            true);
        } else if (contextType == "Equipment-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateEquipmentContextContent(
                            EquipmentContextRequestHandler.getInstance().getContextsOfUser(userId), true);
        } else if (contextType == "Environment-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateEnvironmentContextContent(
                            EnvironmentContextRequestHandler.getInstance().getContextsOfUser(userId), true);
        }
return administrationDatableContextContent; 
		
	}

	public String generateAdministrationUserTable(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        final String administrationDatableContent = new Molecule_5_DataTableContent()
                .generateAdministrationUserContent(adminsectionRequestHandler.getAllUsers());
        adminsectionRequestHandler.getAllUsercontexts();
        final String administrationUserTable = ""
                + "<table id='user' style='border: 4px solid #ddd !important;' class='table table-striped table-bordered dt-responsive nowrap' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>Username</th>" + "<th>User ID</th>" + "<th>Email</th>"
                + "<th>Role</th>" + "<th>Options</th>" + "</tr>" + "</thead>"
                + "<tbody id='tableContent'>" + administrationDatableContent + "</tbody>"
                + "</table>";
        return administrationUserTable;

    }

    public String generateDataTableContent(final ArrayList<User> listUsers) {

        String tableContent = "";

        for (final User users : listUsers) {
            String roles = "";
            String username = "";
            String email = "";

            for (final String role : users.getRoles()) {
                roles += role;
            }

            if (users.getUsername().contains("#046")) {
                username = users.getUsername().replace("#046", ".");
            } else {
                username = users.getUsername();
            }

            if (users.getEmail().contains("#046")) {
                email = users.getEmail().replace("#046", ".");
            } else {
                email = users.getEmail();
            }

            tableContent += "<tr>" + "<td id='tdUserName_"
                    + users.getId()
                    + "'>"
                    + username
                    + "</td>"
                    + "<td>"
                    + users.getId()
                    + "</td>"
                    + "<td id='tdEmail_"
                    + users.getId()
                    + "'>"
                    + email
                    + "</td>"
                    + "<td id='tdRoles_"
                    + users.getId()
                    + "'>"
                    + roles
                    + "</td>"
                    + "<td>"
                    + "<button id='"
                    + users.getId()
                    + "' class='btn btn-md btn-default' onClick='editUser(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                    + "<button id='"
                    + users.getId()
                    + "' class='btn btn-md btn-default' onClick='deleteUser(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                    + "<button id='"
                    + users.getId()
                    + "' class='btn btn-md btn-default'><div class='glyphicon glyphicon-copy'></div> Copy to clipboard  </button> </td></tr>";
        }

        return tableContent;
    }

    public String generateDeleteGroupUserTable(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        final String administrationDatableContent = new Molecule_5_DataTableContent()
                .generateRemoveGroupUserContent(adminsectionRequestHandler.getAllUsers());
        adminsectionRequestHandler.getAllUsercontexts();
        final String administrationUserTable = ""
                + "<table style='margin-top:1em; border: 3px solid #ddd !important;' id='deleteGroupMemberDataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>Name</th>"
                + "<th style='width:10% !important'>Options</th>" + "</tr>" + "</thead>"
                + "</table>";
        return administrationUserTable;
    }

    public String generateGroupTable(final GroupsRequestHandler groupsRequestHandler)
            throws IllegalArgumentException, IOException {

        final String tableContent = new Molecule_5_DataTableContent()
                .generateGroupContent(groupsRequestHandler.getAllGroups());

        final String resourceDataTable = ""
                + "<table id='groupDataTable' style='border: 4px solid #ddd !important;' class='table table-striped table-bordered dt-responsive nowrap' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>Group ID</th>" + "<th>Name</th>"
                + "<th>Description</th>" + "<th>Members</th>" + "<th>OpenAccess</th>"
                + "<th>Group Options</th>" + "<th>Member Options</th>" + "</tr>" + "</thead>"
                + "<tbody id='tableContent'>" + tableContent + "</tbody>" + "</table>";
        return resourceDataTable;

    }

    public String generateGroupUserTable(final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        final String administrationDatableContent = new Molecule_5_DataTableContent()
                .generateGroupUserContent(adminsectionRequestHandler.getAllUsers());
        adminsectionRequestHandler.getAllUsercontexts();
        final String administrationUserTable = ""
                + "<table style='margin-top:1em; border: 3px solid #ddd !important;' id='userGroupDataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>Name</th>" + "<th>Status</th>"
                + "<th style='width:10% !important'>Options</th>" + "</tr>" + "</thead>"
                + "</table>";
        return administrationUserTable;
    }

    public String generateMyResourceTable(final MyResourcesRequestHandler myResourceRequestHandler)
            throws IllegalArgumentException, IOException {

        final String tableContent = new Molecule_5_DataTableContent()
                .generateResourceContent(myResourceRequestHandler.getAllResourceDescriptions());

        final String resourceDataTable = ""
                + "<table id='ResourceDataTable' style='border: 4px solid #ddd !important;' class='table table-striped table-bordered dt-responsive nowrap' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>Description</th>" + "<th>Resource Id</th>"
                + "<th>Created</th>" + "<th>Last update</th>" + "<th>Type</th>" + "<th>Owner</th>"
                + "<th>Options</th>" + "</tr>" + "</thead>" + "<tbody id='tableContent'>"
                + tableContent + "</tbody>" + "</table>";
        return resourceDataTable;

    }

	public Object generateAdministrationPublicContextTable(AdminSectionRequestHandler adminsectionRequestHandler,
			String destination, String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
