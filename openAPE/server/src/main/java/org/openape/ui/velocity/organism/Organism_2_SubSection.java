package org.openape.ui.velocity.organism;

import org.openape.ui.velocity.molecules.Molecule_4_subNavigation;

public class Organism_2_SubSection {
	
	public String generateTopNavigation(){
		String subNavigation = new Molecule_4_subNavigation().generateSubNavigation();
		String subSection = 
				"<div class='subSection'>"
				+ subNavigation
				+ "</div>";
		return subSection;
	}

}
