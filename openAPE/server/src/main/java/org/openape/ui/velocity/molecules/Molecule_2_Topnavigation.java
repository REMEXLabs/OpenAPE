package org.openape.ui.velocity.molecules;

import java.util.Map.Entry;

import org.openape.ui.velocity.atoms.Atom_1_NavigationLinks;

public class Molecule_2_Topnavigation {
    public String generateTopNavigationLinks() {
// TODO LUSM: replace with template
        String topNavigation = "";
        for (final Entry<String, String> entry : new Atom_1_NavigationLinks()
                .generateTopNavigationLinks().entrySet()) {
            topNavigation += "<div class='navigationLinks' id='div" + entry.getKey()
                    + "'><a href='" + entry.getKey()  + "' id='link_" + entry.getKey() + "'>" + entry.getValue()
                    + "</a></div>";
        }

        return topNavigation;
    }
}
