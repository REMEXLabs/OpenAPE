package org.openape.ui.velocity.organism;

import org.openape.ui.velocity.atoms.Atom_2_OpenAPEHeader;
import org.openape.ui.velocity.molecules.Molecule_2_topnavigation;
import org.openape.ui.velocity.molecules.Molecule_3_userSection;

public class Organism_1_Topsection {

    public String generateTopNavigation() {
        final String logo = new Atom_2_OpenAPEHeader().generateLogo();
        final String topNavigationLinks = new Molecule_2_topnavigation()
                .generateTopNavigationLinks();
        final String userSection = new Molecule_3_userSection().generateUsersection();
        final String topNavigation = "<div class='topNavigation'>" + "<div class='logo'>" + logo
                + "</div>" + "<div class='topNavigationLinks' id='topNavigationLinks'>"
                + topNavigationLinks + "</div>"
                + "<div align='right' class='userSection' id='userSection' hidden='true'>"
                + userSection + "</div>" + "</div>";
        return topNavigation;
    }

}
