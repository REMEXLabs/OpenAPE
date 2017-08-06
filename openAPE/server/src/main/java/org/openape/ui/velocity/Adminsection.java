package org.openape.ui.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.api.user.User;
import org.openape.server.rest.SuperRestInterface;
import org.openape.ui.velocity.models.deleteUser;
import org.openape.ui.velocity.molecules.Molecule_5_dataTable;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Adminsection extends SuperRestInterface {
    private static Map<String, Object> model = new HashMap<>();

    public Adminsection() throws IllegalArgumentException, IOException {
        super();
        // TODO Auto-generated constructor stub
    }

    public static Boolean ich() {
        System.out.println("ich bin gerufen");
        return true;
    }

    public static void setupAdminVELOCITYInterface(
            final AdminSectionRequestHandler adminsectionRequestHandler)
                    throws IllegalArgumentException, IOException {

        for (final User entry : adminsectionRequestHandler.getAllUsers()) {
            System.out.println(entry.getId());
        }

        Spark.get("/adminsection", (request, response) -> {
            final User user = new User();
            user.setEmail("wjaufmann");

            final deleteUser del = new deleteUser();

            for (final User entry : del.getAdminSectionRequestHandler().getAllUsers()) {
                System.out.println(entry.getId());
            }

            Adminsection.model.put("footer", new Footer().generateFooter());
            Adminsection.model.put("logo", new OpenapeLogo().generateOpenAPELogo());
            Adminsection.model.put("topNavigation",
                    new Organism_1_Topsection().generateTopNavigation());
            Adminsection.model.put("subSection",
                    new Organism_2_SubSection().generateTopNavigation());
            Adminsection.model.put("tableContent", new Molecule_5_dataTable()
            .generateDataTableContent(adminsectionRequestHandler.getAllUsers()));
            Adminsection.model.put("deleteUser", del);

            return new ModelAndView(Adminsection.model, "velocityTemplates/adminsection.vm"); // located
            // in
            // the
            // resources
            // directory
        }, new VelocityTemplateEngine());
    }
}
