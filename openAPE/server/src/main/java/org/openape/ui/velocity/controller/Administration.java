package org.openape.ui.velocity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.atoms.Atom_2_OpenAPEHeader;
import org.openape.ui.velocity.molecules.Molecule_6_Modals;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Administration extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public Administration() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void setupAdministrationVELOCITYInterface(
            final AdminSectionRequestHandler adminsectionRequestHandler)
                    throws IllegalArgumentException, IOException {
        adminsectionRequestHandler.getAllTaskContexts();
        Spark.get("/administration", (request, response) -> {
            Administration.model.put("footer", new Footer().generateFooter());
            Administration.model.put("logo", new Atom_2_OpenAPEHeader().generateLogo());
            Administration.model.put("topNavigation",
                    new Organism_1_Topsection().generateTopNavigation());
            Administration.model.put("subSection",
                    new Organism_2_SubSection().generateTopNavigation());
            Administration.model.put("dataTableUserContext", new Organism_3_DataTable()
            .generateAdministrationUserContextTable(adminsectionRequestHandler));

            final String[] contexts = { "User", "User-Context", "Task-Context",
                    "Equipment-Context", "Environment-Context" };

            for (final String context : contexts) {
                final String contextIdName = context.replace("-", "");
                Administration.model.put("delete" + contextIdName + "Modal",
                        new Molecule_6_Modals().generateDeleteContextModal(context));
                Administration.model.put("edit" + contextIdName + "Modal",
                        new Molecule_6_Modals().generateEditContextModal(context));
                Administration.model.put("add" + contextIdName + "Modal",
                        new Molecule_6_Modals().generateAddContextModal(context));
                if (context == "User") {
                    Administration.model.put("dataTableUser", new Organism_3_DataTable()
                    .generateAdministrationUserTable(adminsectionRequestHandler));
                } else {
                    Administration.model.put("dataTable" + contextIdName,
                            new Organism_3_DataTable().generateAdministrationContextTable(
                                    adminsectionRequestHandler, context));
                }
            }

            return new ModelAndView(Administration.model, "velocityTemplates/administration.vm"); // located
            // in
            // the
            // resources
            // directory
        }, new VelocityTemplateEngine());
    }
}
