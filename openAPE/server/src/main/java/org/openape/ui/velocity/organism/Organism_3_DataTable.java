package org.openape.ui.velocity.organism;

import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.molecules.Molecule_5_DataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class Organism_3_DataTable {
    public String generateAdministrationContextTable(
            final AdminSectionRequestHandler adminsectionRequestHandler, final String contextName,
            final String destination) throws IllegalArgumentException, IOException {

        String administrationDatableContextContent = "";
        final String idName = contextName.substring(0, 1).toLowerCase()
                + contextName.substring(1).replace("-", "");

        if (contextName == "User-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateUserContextContent(adminsectionRequestHandler.getAllUsercontexts(),
                            destination);
        } else if (contextName == "Task-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateTaskContextContent(adminsectionRequestHandler.getAllTaskContexts(),
                            destination);
        } else if (contextName == "Equipment-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateEquipmentContextContent(
                            adminsectionRequestHandler.getAllEquipmentContexts(), destination);
        } else if (contextName == "Environment-Context") {
            administrationDatableContextContent = new Molecule_5_DataTableContent()
                    .generateEnvironmentContextContent(
                            adminsectionRequestHandler.getAllEnvironmentContexts(), destination);
        }

        final String administrationContextTable = ""
                + "<table id='"
                + idName
                + "DataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th>ID</th>" + "<th>Owner</th>" + "<th>Created</th>"
                + "<th>Public</th>" + "<th>Options</th>" + "</tr>" + "</thead>"
                + "<tbody id='tableContent'>" + administrationDatableContextContent + "</tbody>"
                + "</table>";
        return administrationContextTable;

    }

    public String generateAdministrationGroupTable(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        final String administrationDatableGroupContent = new Molecule_5_DataTableContent()
                .generateGroupContent(/*
                                       * adminsectionRequestHandler.
                                       * getAllEnvironmentContexts()
                                       */);

        final String administrationContextTable = ""
                + "<table id='groupDataTable' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
                + "<thead>" + "<tr>" + "<th >ID</th>" + "<th >Group name</th>"
                + "<th >Options</th>" + "</tr>" + "</thead>" + "<tbody id='tableContent'>"
                + administrationDatableGroupContent + "</tbody>" + "</table>";
        return administrationContextTable;

    }

    public String generateAdministrationUserTable(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        final String administrationDatableContent = new Molecule_5_DataTableContent()
                .generateAdministrationUserContent(adminsectionRequestHandler.getAllUsers());
        adminsectionRequestHandler.getAllUsercontexts();
        final String administrationUserTable = ""
                + "<table id='user' class='table table-striped table-bordered' cellspacing='0' width='100%'>"
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
}
