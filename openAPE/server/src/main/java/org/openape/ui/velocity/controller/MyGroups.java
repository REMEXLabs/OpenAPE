package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.controllerComponents.MainComponents;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.organism.Organism_4_Modals;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.openape.ui.velocity.requestHandler.GroupsRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class MyGroups extends SuperRestInterface {

    public static void setupMyGroupsVELOCITYInterface() throws IllegalArgumentException,
            IOException {

        Spark.get("/myGroups", (request, response) -> {

            final MainComponents mainController = new MainComponents();
            final Map<String, Object> model = mainController.getTemplateComponents();

            model.put("dataTableGroup",
                    new Organism_3_DataTable().generateGroupTable(new GroupsRequestHandler()));
            
            model.put("dataTableDeleteGroupMember", new Organism_3_DataTable()
                    .generateDeleteGroupUserTable(new AdminSectionRequestHandler()));


            model.put("deleteGroupModal", new Organism_4_Modals().generateDeleteModal("Group"));
            
            model.put("dataTableUserGroup", new Organism_3_DataTable()
                    .generateGroupUserTable(new AdminSectionRequestHandler()));

            model.put("addGroupModal", new Organism_4_Modals().generateGroupModal("Add"));

            model.put("editGroupModal", new Organism_4_Modals().generateGroupModal("Edit"));
            
            model.put("addGroupUserModal",
                    new Organism_4_Modals().generateGroupUserModal("Add"));

            model.put("deleteGroupUserModal",
                    new Organism_4_Modals().generateGroupUserModal("Delete"));
            
            return new ModelAndView(model, "velocityTemplates/myGroups.vm"); // located
                                                                             // in
                                                                             // the
                                                                             // resources
                                                                             // directory
            }, new VelocityTemplateEngine());
    }

    public MyGroups() throws IllegalArgumentException, IOException {
        super();
    }
}
