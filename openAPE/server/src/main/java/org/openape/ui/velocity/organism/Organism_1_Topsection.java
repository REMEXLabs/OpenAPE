package org.openape.ui.velocity.organism;

import org.openape.ui.velocity.atoms.Atom_2_OpenAPEHeader;
import org.openape.ui.velocity.molecules.Molecule_2_Topnavigation;
import org.openape.ui.velocity.molecules.Molecule_3_UserSection;

public class Organism_1_Topsection {
	
	public String generateTopNavigation(){
		String logo = new Atom_2_OpenAPEHeader().generateLogo();
		String topNavigationLinks = new Molecule_2_Topnavigation().generateTopNavigationLinks();
		String userSection = new Molecule_3_UserSection().generateUsersection();
		String topNavigation = 
				"<div class='topNavigation'>"
				+ "<div class='logo'>"+logo+"</div>"
				+ "<div class='topNavigationLinks' id='topNavigationLinks'>"+topNavigationLinks+"</div>"
				+ "<div align='right' class='userSection' id='userSection' hidden='true'>"+userSection+"</div>"
				+ "</div>";
		return topNavigation;
	}

}
