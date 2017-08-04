package org.openape.ui.velocity.controllerComponents;

import java.util.HashMap;
import java.util.Map;

import org.openape.ui.velocity.atoms.Atom_2_OpenAPEHeader;
import org.openape.ui.velocity.organism.Organism_5_Footer;
import org.openape.ui.velocity.organism.Organism_1_Topsection;
import org.openape.ui.velocity.organism.Organism_2_SubSection;
import org.openape.ui.velocity.organism.Organism_4_Modals;

public class MainComponents {
	
	public Map<String, Object> getTemplateComponents () {
		Map<String, Object> model = new HashMap<>();
		
		 model.put("footer", new Organism_5_Footer().generateFooter());
         model.put("logo", new Atom_2_OpenAPEHeader().generateLogo());
         model.put("topNavigation", new Organism_1_Topsection().generateTopNavigation());
         model.put("subSection", new Organism_2_SubSection().generateTopNavigation());
         model.put("signinModal", new Organism_4_Modals().generateSigninModal());
         model.put("signupModal", new Organism_4_Modals().generateSignupModal());
		
		return model;
	}
}
