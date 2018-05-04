package org.openape.ui.velocity.molecules;

import java.util.Map.Entry;

import org.openape.ui.velocity.atoms.Atom_1_NavigationLinks;

public class Molecule_4_SubNavigation {
    public String generateSubNavigation() {

        String subNavigation = "";
        for (final Entry<String, String> entry : new Atom_1_NavigationLinks()
                .generateSubNavigationLinks().entrySet()) {
            subNavigation += "<div class='subnav' style='float:left;padding:1em' id='div"
                    + entry.getKey() // + "' hidden='true'"
                    		+ "'><a href='"+ entry.getKey() +"' id='link_" + entry.getKey()
                    + "'>" + entry.getValue() + "</a></div>";
        }

        return subNavigation;
    }
}
