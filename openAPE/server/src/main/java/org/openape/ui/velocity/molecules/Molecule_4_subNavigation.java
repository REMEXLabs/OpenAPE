package org.openape.ui.velocity.molecules;

import java.util.Map.Entry;

import org.openape.ui.velocity.atoms.Atom_1_navigationLinks;

public class Molecule_4_subNavigation {
    public String generateSubNavigation() {

        String subNavigation = "";
        for (final Entry<String, String> entry : new Atom_1_navigationLinks()
                .generateSubNavigationLinks().entrySet()) {
            subNavigation += "<div class='subnav' style='float:left;padding:1em' id='div"
                    + entry.getKey() + "' hidden='true'><a href='#' id='link" + entry.getKey()
                    + "'>" + entry.getValue() + "</a></div>";
        }

        return subNavigation;
    }
}
