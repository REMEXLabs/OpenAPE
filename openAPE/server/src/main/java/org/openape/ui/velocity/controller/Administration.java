package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.auth.AuthService;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.OpenApeVelocityEngine;
import org.openape.ui.velocity.controllerComponents.MainComponents;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.organism.Organism_4_Modals;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.openape.ui.velocity.requestHandler.GroupsRequestHandler;
import org.openape.ui.velocity.requestHandler.MyResourcesRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Administration extends SuperRestInterface {
    private static AuthService auth;

	public static void setupAdministrationVELOCITYInterface(
            final AdminSectionRequestHandler adminsectionRequestHandler, AuthService authService)
            throws IllegalArgumentException, IOException {
        auth = authService;
		adminsectionRequestHandler.getAllTaskContexts();

		Spark.before("/administration",
		        auth.authorize("admin"));


		Spark.get("/administration", (request, response) -> {



        	final String userId = auth.getAuthenticatedUser(request, response).getId();
        	// site components
                final Map<String, Object> model = new MainComponents().getTemplateComponents();

                final String[] destinations = { "User", "User-Context", "Task-Context",
                        "Equipment-Context", "Environment-Context" };

                model.put("dataTableResource", new Organism_3_DataTable()
                        .generateMyResourceTable(new MyResourcesRequestHandler()));

                model.put("dataTableGroup",
                        new Organism_3_DataTable().generateGroupTable(new GroupsRequestHandler()));

                model.put("dataTableUserGroup", new Organism_3_DataTable()
                        .generateGroupUserTable(new AdminSectionRequestHandler()));

                model.put("dataTableDeleteGroupMember", new Organism_3_DataTable()
                        .generateDeleteGroupUserTable(new AdminSectionRequestHandler()));

                // modals
                model.put("deleteResourceModal",
                        new Organism_4_Modals().generateDeleteModal("Resource"));

                model.put("addResourceModal", new Organism_4_Modals().generateResourceModal("Add"));

                model.put("editResourceModal",
                        new Organism_4_Modals().generateResourceModal("Edit"));

                model.put("addGroupUserModal",
                        new Organism_4_Modals().generateGroupUserModal("Add"));

                model.put("deleteGroupUserModal",
                        new Organism_4_Modals().generateGroupUserModal("Delete"));

                // modals

                model.put("deleteGroupModal", new Organism_4_Modals().generateDeleteModal("Group"));

                model.put("addGroupModal", new Organism_4_Modals().generateGroupModal("Add"));

                model.put("editGroupModal", new Organism_4_Modals().generateGroupModal("Edit"));

                for (final String destination : destinations) {
                    final String idName = destination.replace("-", "");

                    // modals
                    model.put("delete" + idName + "Modal",
                            new Organism_4_Modals().generateDeleteModal(destination));
                    model.put("edit" + idName + "Modal",
                            new Organism_4_Modals().generateContextModal(destination, "Edit"));
                    model.put("add" + idName + "Modal",
                            new Organism_4_Modals().generateContextModal(destination, "Add"));

                    // Datatable creation
                    if (destination == "User") {
                        model.put("dataTableUser", new Organism_3_DataTable()
                                .generateAdministrationUserTable(adminsectionRequestHandler));
                    } else {
                        model.put("dataTable" + idName, new Organism_3_DataTable()
                                .generateAdministrationContextTable(adminsectionRequestHandler,
                                        destination, true, userId));
                    }
                }

                return new ModelAndView(model, "velocityTemplates/administration.vm"); // located
                                                                                       // in
                                                                                       // the
                                                                                       // resources
                                                                                       // directory
            }, new OpenApeVelocityEngine());
    }

    public Administration() throws IllegalArgumentException, IOException {
        super();

    }
}
