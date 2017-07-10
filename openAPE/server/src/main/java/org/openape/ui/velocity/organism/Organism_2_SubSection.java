package org.openape.ui.velocity.organism;

import org.openape.ui.velocity.molecules.Molecule_1_logo;
import org.openape.ui.velocity.molecules.Molecule_2_topnavigation;
import org.openape.ui.velocity.molecules.Molecule_3_userSection;
import org.openape.ui.velocity.molecules.Molecule_4_subNavigation;

public class Organism_2_SubSection {
	
	public String generateTopNavigation(){
		String subNavigation = new Molecule_4_subNavigation().generateSubNavigation();
		String userSectionLoggedOut = new Molecule_3_userSection().generateUserSectionLogginFormulars();
		String userSectionSignin = new Molecule_3_userSection().generateUserSectionSigninFormulars();
		String subSection = 
				"<div class='subSection'>"
				+ subNavigation
				+ "<div align='center' style='float:right;background-color:#C60000;width:45em;height:3.3em;padding-top:0.5em' id='userSectionLoggedOut' hidden='true'>"+userSectionLoggedOut+"</div>"
				+ "<div align='center' style='float:right;background-color:#C60000;width:45em;height:3.3em;padding-top:0.5em' id='userSectionSignIn' hidden='true'>"+userSectionSignin+"</div>"
				+ "</div>";
		return subSection;
	}

}
