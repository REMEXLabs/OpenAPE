package org.openape.ui.velocity.molecules;

import org.openape.ui.velocity.atoms.Atom_1_navigationLinks;

public class Molecule_3_userSection {
	public String generateUsersection(){
		String userSection = "";	
		for(String entry : new Atom_1_navigationLinks().generateUserSectionLinksLoggedOut()){
			userSection += "<div class='userSectionLinks' >"+entry+"</div>";
		}
		return userSection;
	}
	
	public String generateUserSectionLogginFormulars(){
		String userSectionFormulars = "";	
		
		return userSectionFormulars += ""
				+ "<div class='col-xs-4' style='float:left'>"
				+ "<input class='form-control' id='ex1' placeholder='username' type='text'>"
				+ "</div>"
				+ "<div class='col-xs-4' style='float:left'>"
				+ "<input class='form-control' id='ex1' placeholder='password' type='password'>"
				+ "</div>"
				+ "<button class='btn btn-sm btn-danger'>"
				+ "<div class='glyphicon glyphicon-log-in'></div> Sign in</button>"
				+ "      <button class='btn btn-sm btn-danger'>"
				+ "<div class='glyphicon glyphicon-question-sign'></div> Forgot PW?</button>";
	}
	
	public String generateUserSectionSigninFormulars(){
		String userSectionFormulars = "";	
		
		return userSectionFormulars += ""
				+ "<div class='col-xs-4' style='float:left'>"
				+ "<input class='form-control' id='ex1' placeholder='email' type='text'>"
				+ "</div>"
				+ "<div class='col-xs-3' style='float:left'>"
				+ "<input class='form-control' id='ex1' placeholder='username' type='text'>"
				+ "</div>"
				+ "<div class='col-xs-3' style='float:left'>"
				+ "<input class='form-control' id='ex1' placeholder='password' type='password'>"
				+ "</div>"
				+ "<div style='loat:left'>"
				+ "<button class='btn btn-sm btn-danger'>"
				+ "<div class='glyphicon glyphicon-log-in'></div> Sign in</button></div>";
	}
	
}
