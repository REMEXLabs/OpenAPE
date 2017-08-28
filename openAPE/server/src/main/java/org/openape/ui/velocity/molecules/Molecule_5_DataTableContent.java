package org.openape.ui.velocity.molecules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.databaseObjectBase.Property;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.api.group.Group;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.requestHandler.ProfileHandler;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

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
            final List<EnvironmentContext> listEnvironmentContexts, final String destination) throws IOException {

        String tableContent = "";
        String buttons = "";

        for (final EnvironmentContext environmentContext : listEnvironmentContexts) {
        	User user = ProfileHandler.getUserById(environmentContext.getImplementationParameters().getOwner());
            // if the user is on the public context site than the view and copy
            // to clipboard buttons will be enabled
            if (destination == "context") {
                buttons = "<button id='"
                        + environmentContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='viewContext(this)' ><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + environmentContext.getId()
                        + "' class='btn btn-md btn-default' data-clipboard-text='http://gpi.eu/"
                        + environmentContext.getId()
                        + "' onClick='copyEnvironmentContextLink(this)'><div class='glyphicon glyphicon-trash'></div> Copy link to Clipboard</button> ";
            }
            // if the user is on the MyContext site oder in the Administration
            // section than the edit, delete and copy buttons will be enabled
            else {
                buttons = "<button id='"
                        + environmentContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + environmentContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + environmentContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EnvironmentContext' onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdEnvironmentContextName_" + environmentContext.getId()
                    + "'>" + environmentContext.getId() + "</td>" + "<td id='" + user.getId()
                    + "'>" + user.getUsername() + "</td>" + "<td id='public_"+environmentContext.getId()+"'>" + environmentContext.getImplementationParameters().isPublic()
                    + "</td>"  + "<td>" + buttons
                    + "</td></tr>";
        }

        return tableContent;
    }

    public String generateEquipmentContextContent(final List<EquipmentContext> listEquipmentContexts,
            final String destination) throws IOException {

        String tableContent = "";
        String buttons = "";

        for (EquipmentContext equipmentkContext : listEquipmentContexts) {
        	User user = ProfileHandler.getUserById(equipmentkContext.getImplementationParameters().getOwner());
            if (destination == "context") {
                buttons = "<button id='"
                        + equipmentkContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='viewContext(this)'><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + equipmentkContext.getId()
                        + "' class='btn btn-md btn-default' data-clipboard-text='http://gpi.eu/"
                        + equipmentkContext.getId()
                        + "' onClick='copyEquipmentContextLink(this)'><div class='glyphicon glyphicon-trash'></div> Copy link to Clipboard</button> ";
            } else {
                buttons = "<button id='"
                        + equipmentkContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + equipmentkContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + equipmentkContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='EquipmentContext' onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdEquipmentContextName_" + equipmentkContext
                    + "'>" + equipmentkContext.getId() + "</td>" + "<td id='" + user.getId()
                    + "'>" + user.getUsername() + "</td>" + "<td id='public_"+equipmentkContext.getId()+"'>" + equipmentkContext.getImplementationParameters().isPublic() + "</td>" + "<td>" + buttons
                    + "</td></tr>";
        }

        return tableContent;
    }

    public String generateGroupContent(final List<Group> listGroup) throws IOException {
        // TODO Auto-generated method stub
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        String tableContent = "";
        for (final Group group : listGroup) {
            final String buttons = "<button id='"
                    + group.getId()
                    + "' class='btn btn-md btn-default' onClick='editGroup(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                    + "<button id='"
                    + group.getId()
                    + "' class='btn btn-md btn-default' onClick='deleteGroup(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> ";

            final String memberButtons = "<button id='"
                    + group.getId()
                    + "' onClick='addGroupMember(this)' type='button' class='btn btn-default' data-toggle='modal' ><div class='glyphicon glyphicon-plus'></div> Add </button>"
                    + "<button id='"
                    + group.getId()
                    + "' class='btn btn-md btn-default' onClick='deleteGroupMember(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> ";

            tableContent += "" + "<tr>" + "<td>" + group.getId() + "</td>" + "<td>"
                    + group.getName() + "</td>" + "<td>" + group.getDescription() + "</td>"
                    + "<td>" + group.getMembers().size() + "</td>" + "<td>" + group.isOpenAccess()
                    + "</td>" + "<td>" + buttons + "</td>" + "<td>" + memberButtons + "</td>"
                    + "</tr>";
        }

        return tableContent;
    }

    public String generateGroupUserContent(final ArrayList<User> listUsers) {

        String tableContent = "";

        for (final User users : listUsers) {

            String username = "";

            if (users.getUsername().contains("#046")) {
                username = users.getUsername().replace("#046", ".");
            } else {
                username = users.getUsername();
            }

            tableContent += "<tr>"
                    + "<td id='tdUserName_"
                    + users.getId()
                    + "'>"
                    + username
                    + "</td>"
                    + "<td>"
                    + "<button id='addUserToGroup_"
                    + users.getId()
                    + "' class='btn btn-md btn-default' onClick='addUserToGroup(this)' ><div class='glyphicon glyphicon-edit' ></div> Add </button></tr>";
        }

        return tableContent;
    }

    public String generateRemoveGroupUserContent(final ArrayList<User> listUsers) {

        String tableContent = "";

        for (final User users : listUsers) {

            String username = "";

            if (users.getUsername().contains("#046")) {
                username = users.getUsername().replace("#046", ".");
            } else {
                username = users.getUsername();
            }

            tableContent += "<tr>"
                    + "<td id='tdUserName_"
                    + users.getId()
                    + "'>"
                    + username
                    + "</td>"
                    + "<td>"
                    + "<button id='addUserToGroup_"
                    + users.getId()
                    + "' class='btn btn-md btn-default' onClick='addUserToGroup(this)' ><div class='glyphicon glyphicon-trash' ></div> Remove </button></tr>";
        }

        return tableContent;
    }

    public String generateResourceContent(final List<ResourceDescription> listResourceDescriptions)
            throws IOException {
        // TODO Auto-generated method stub
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        String tableContent = "";
        for (final ResourceDescription resourseDescription : listResourceDescriptions) {
            String format = "";
            String title = "";
            String modified = "";
            final String userId = resourseDescription.getImplementationParameters().getOwner();
            String resourceId = "";

            new AdminSectionRequestHandler();
            final User user = (User) databaseConnection.getDatabaseObjectById(
                    AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, userId);

            final String userName = user.getUsername();

            for (final Property property : resourseDescription.getPropertys()) {
                if (property.getName().contains("format")) {
                    format = property.getValue();
                } else if (property.getName().contains("title")) {
                    title = property.getValue();
                } else if (property.getName().contains("modified")) {
                    modified = property.getValue();
                } else if (property.getName().contains("resource-uri")) {
                    resourceId = property.getValue().substring(
                            property.getValue().indexOf("resources/") + 10);
                }
            }

            final String buttons = "<button id='"
                    + resourseDescription.getId()
                    + "' class='btn btn-md btn-default' onClick='editResource(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                    + "<button id='delete_"
                    + resourseDescription.getId()
                    + "' class='btn btn-md btn-default' data-resourceDescriptionId='"
                    + resourseDescription.getId()
                    + "' data-resourceId='"
                    + resourceId
                    + "' onClick='deleteResource(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> ";

            tableContent += "" + "<tr>" + "<td>" + title + "</td>" + "<td>"
                    + resourseDescription.getId() + "</td>" + "<td></td>" + "<td>" + modified
                    + "</td>" + "<td>" + format + "</td>" + "<td>" + userName + "</td>" + "<td>"
                    + buttons + "</td>" + "</tr>";
        }

        return tableContent;
    }

    public String generateTaskContextContent(final List<TaskContext> listTaskContexts,
            final String destination) throws IOException {

        String tableContent = "";
        String buttons = "";

        for (final TaskContext taskContext : listTaskContexts) {
        	User user = ProfileHandler.getUserById(taskContext.getImplementationParameters().getOwner());
            if (destination == "context") {
                buttons = "<button id='"
                        + taskContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='viewContext(this)' ><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + taskContext.getId()
                        + "' class='btn btn-md btn-default' data-clipboard-text='http://gpi.eu/"
                        + taskContext.getId()
                        + "' onClick='copyTaskContextLink(this)'><div class='glyphicon glyphicon-trash'></div> Copy link to Clipboard</button> ";
            } else {
                buttons = "<button id='"
                        + taskContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + taskContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + taskContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='TaskContext' onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdTaskContextName_" + taskContext.getId() + "'>"
                    + taskContext.getId() + "</td>" + "<td id='"+user.getId()+"'>" + user.getUsername() + "</td>" + "<td id='public_"+taskContext.getId()+"'>"
                    + taskContext.getImplementationParameters().isPublic() + "</td>" + "<td>" + buttons + "</td></tr>";
        }

        return tableContent;
    }

    public String generateUserContextContent(final List<UserContext> listUserContexts,
            final String destination) throws IOException {

        String tableContent = "";
        String buttons = "";

        for (final UserContext userContext : listUserContexts) {
        	User user = ProfileHandler.getUserById(userContext.getImplementationParameters().getOwner());
            if (destination == "context") {
                buttons = "<button id='"
                        + userContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext' onClick='viewContext(this)' ><div class='glyphicon glyphicon-edit' ></div> View </button>"
                        + "<button id='"
                        + userContext.getId()
                        + "' class='btn btn-md btn-default' data-clipboard-text='"
                        + userContext.getId()
                        + "' onClick='copyUserContextLink(this)'><div class='glyphicon glyphicon-trash'></div>Copy link to Clipboard</button> ";
            } else {
                buttons = "<button id='"
                        + userContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext' onClick='editContext(this)' ><div class='glyphicon glyphicon-edit' ></div> Edit </button>"
                        + "<button id='"
                        + userContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext' onClick='deleteContext(this)'><div class='glyphicon glyphicon-trash'></div> Delete </button> "
                        + "<button id='"
                        + userContext.getId()
                        + "' class='btn btn-md btn-default' data-contexttype='UserContext'  onClick='copyContext(this)'><div class='glyphicon glyphicon-copy'></div> Copy </button>";
            }

            tableContent += "<tr>" + "<td id='tdUserContextName_" + userContext.getId() + "'>"
                    + userContext.getId() + "</td>"  + "<td id='"+user.getId()+"'>" + user.getUsername() + "</td>" + "<td id='public_"+userContext.getId()+"'>"
                    + userContext.getImplementationParameters().isPublic() + "</td>" + "<td>" + buttons + " </td></tr>";
        }

        return tableContent;
    }
}
