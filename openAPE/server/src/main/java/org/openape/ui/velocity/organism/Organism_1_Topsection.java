package org.openape.ui.velocity.organism;

import org.openape.ui.velocity.molecules.Molecule_1_logo;
import org.openape.ui.velocity.molecules.Molecule_2_topnavigation;
import org.openape.ui.velocity.molecules.Molecule_3_userSection;

public class Organism_1_Topsection {
	
	public String generateTopNavigation(){
		String logo = new Molecule_1_logo().generateLogo();
		String topNavigationLinks = new Molecule_2_topnavigation().generateTopNavigationLinks();
		String userSection = new Molecule_3_userSection().generateUsersection();
		String topNavigation = 
				"<div class='topNavigation'>"
				+ "<div class='logo'>"+logo+"</div>"
				+ "<div class='topNavigationLinks' id='topNavigationLinks'>"+topNavigationLinks+"</div>"
				+ "<div align='right' class='userSection'>"+userSection+"</div>"
				+ "</div>";
		return topNavigation;
	}

}
