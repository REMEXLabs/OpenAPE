package org.openape.ui.velocity.molecules;

import java.util.Map.Entry;

import org.openape.ui.velocity.atoms.Atom_1_navigationLinks;

public class Molecule_2_topnavigation {
    public String generateTopNavigationLinks() {

        String topNavigation = "";
        for (final Entry<String, String> entry : new Atom_1_navigationLinks()
                .generateTopNavigationLinks().entrySet()) {
            topNavigation += "<div class='navigationLinks' id='div" + entry.getKey()
                    + "'><a href='#' id='link" + entry.getKey() + "'>" + entry.getValue()
                    + "</a></div>";
        }

        return topNavigation;
    }
}
