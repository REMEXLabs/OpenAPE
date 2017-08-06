package org.openape.ui.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.api.user.User;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.models.deleteUser;
import org.openape.ui.velocity.molecules.Molecule_5_DataTable;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Administration extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public static void setupAdministrationVELOCITYInterface(
            final AdminSectionRequestHandler adminsectionRequestHandler)
            throws IllegalArgumentException, IOException {

        Spark.get("/administration", (request, response) -> {
            final User user = new User();
            user.setEmail("wjaufmann");

            final deleteUser del = new deleteUser();

            for (final User entry : del.getAdminSectionRequestHandler().getAllUsers()) {
                System.out.println(entry.getId());
            }

            Administration.model.put("footer", new Footer().generateFooter());
            Administration.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
            Administration.model.put("topNavigation",
                    new Organism_1_Topsection().generateTopNavigation());
            Administration.model.put("subSection",
                    new Organism_2_SubSection().generateTopNavigation());
            Administration.model.put("tableContent", new Molecule_5_DataTable()
                    .generateDataTableContent(adminsectionRequestHandler.getAllUsers()));
            Administration.model.put("deleteUser", del);

            return new ModelAndView(Administration.model, "velocityTemplates/administration.vm"); // located
                // in
                // the
                // resources
                // directory
            }, new VelocityTemplateEngine());
    }

    public Administration() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }
}

