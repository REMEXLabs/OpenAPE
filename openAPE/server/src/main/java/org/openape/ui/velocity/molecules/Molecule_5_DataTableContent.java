package org.openape.ui.velocity.molecules;

import java.util.ArrayList;

import org.openape.api.user.User;

public class Molecule_5_DataTableContent {

    public String generateAdministrationUserContent(final ArrayList<User> listUsers) {

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
                    + "<button id='edit_"
                    + users.getId()
                    + "' class='btn btn-md btn-default' onClick='editUser(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                    + "<button id='delete_"
                    + users.getId()
                    + "' class='btn btn-md btn-default' onClick='deleteUser(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button></tr>";
        }

        return tableContent;
    }

    public String generateEnvironmentContextContent(
            final ArrayList<String[]> listEnvironmentContexts, final String destination) {

        String tableContent = "";
        String buttons = "";

        for (final String[] environmentContext : listEnvironmentContexts) {
        	//if the user is on the public context site than the view and copy to clipboard buttons will be enabled
            if (destination == "context") {
                buttons = "<button id='"
                        + environmentContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='viewContext(this)' ><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + environmentContext[0]
                        + "' class='btn btn-md btn-default' data-clipboard-text='http://gpi.eu/"
                        + environmentContext[0]
                        + "' onClick='copyEnvironmentContextLink(this)'><div class='glyphicon glyphicon-trash'></div> Copy link to Clipboard</button> ";
            } 
          //if the user is on the MyContext site oder in the Administration section than the edit, delete and copy buttons will be enabled
            else {
                buttons = "<button id='"
                        + environmentContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + environmentContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + environmentContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdEnvironmentContextName_" + environmentContext[0]
                    + "'>" + environmentContext[0] + "</td>" + "<td id='" + environmentContext[4]
                    + "'>" + environmentContext[1] + "</td>" + "<td>" + environmentContext[3]
                    + "</td>" + "<td>" + environmentContext[2] + "</td>" + "<td>" + buttons
                    + "</td></tr>";
        }

        return tableContent;
    }

    public String generateEquipmentContextContent(final ArrayList<String[]> listEquipmentContexts,
            final String destination) {

        String tableContent = "";
        String buttons = "";

        for (final String[] equipmentkContext : listEquipmentContexts) {

            if (destination == "context") {
                buttons = "<button id='"
                        + equipmentkContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='viewContext(this)'><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + equipmentkContext[0]
                        + "' class='btn btn-md btn-default' data-clipboard-text='http://gpi.eu/"
                        + equipmentkContext[0]
                        + "' onClick='copyEquipmentContextLink(this)'><div class='glyphicon glyphicon-trash'></div> Copy link to Clipboard</button> ";
            } else {
                buttons = "<button id='"
                        + equipmentkContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + equipmentkContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + equipmentkContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdEquipmentContextName_" + equipmentkContext[0]
                    + "'>" + equipmentkContext[0] + "</td>" + "<td id='" + equipmentkContext[4]
                    + "'>" + equipmentkContext[1] + "</td>" + "<td>" + equipmentkContext[3]
                    + "</td>" + "<td>" + equipmentkContext[2] + "</td>" + "<td>" + buttons
                    + "</td></tr>";
        }

        return tableContent;
    }

    public String generateGroupContent() {
        // TODO Auto-generated method stub
        String tableContent = "";

        tableContent += "<tr>"
                + "<td>4</td>"
                + "<td>G1</td>"
                + "<td>"
                + "<button class='btn btn-md btn-default' onClick='editGroup(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                + "<button class='btn btn-md btn-default' onClick='deleteGroup(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button></tr>";

        return tableContent;
    }

    public String generateTaskContextContent(final ArrayList<String[]> listTaskContexts,
            final String destination) {

        String tableContent = "";
        String buttons = "";

        for (final String[] taskContext : listTaskContexts) {
            if (destination == "context") {
                buttons = "<button id='"
                        + taskContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='viewContext(this)' ><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + taskContext[0]
                        + "' class='btn btn-md btn-default' data-clipboard-text='http://gpi.eu/"
                        + taskContext[0]
                        + "' onClick='copyTaskContextLink(this)'><div class='glyphicon glyphicon-trash'></div> Copy link to Clipboard</button> ";
            } else {
                buttons = "<button id='"
                        + taskContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + taskContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + taskContext[0]
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdTaskContextName_" + taskContext[0] + "'>"
                    + taskContext[0] + "</td>" + "<td id='" + taskContext[4] + "'>"
                    + taskContext[1] + "</td>" + "<td>" + taskContext[3] + "</td>" + "<td>"
                    + taskContext[2] + "</td>" + "<td>" + buttons + "</td></tr>";
        }

        return tableContent;
    }

    public String generateUserContextContent(final ArrayList<String[]> listUserContexts,
            final String destination) {

        String tableContent = "";
        String buttons = "";

        for (final String[] userContext : listUserContexts) {

            if (destination == "context") {
                buttons = "<button id='"
                        + userContext[1]
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext' onClick='viewContext(this)' ><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + userContext[1]
                        + "' class='btn btn-md btn-default' data-clipboard-text='"
                        + userContext[1]
                        + "' onClick='copyUserContextLink(this)'><div class='glyphicon glyphicon-trash'></div>Copy link to Clipboard</button> ";
            } else {
                buttons = "<button id='"
                        + userContext[1]
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + userContext[1]
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + userContext[1]
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext'  onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdUserContextName_" + userContext[1] + "'>"
                    + userContext[1] + "</td>" + "<td id='" + userContext[4] + "'>"
                    + userContext[0] + "</td>" + "<td>" + userContext[3] + "</td>" + "<td>"
                    + userContext[2] + "</td>" + "<td>" + buttons + " </td></tr>";
        }

        return tableContent;
    }
}
