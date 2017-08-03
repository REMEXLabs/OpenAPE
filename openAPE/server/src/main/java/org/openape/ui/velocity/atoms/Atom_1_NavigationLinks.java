package org.openape.ui.velocity.atoms;

import java.util.LinkedHashMap;

public class Atom_1_NavigationLinks {
	public LinkedHashMap<String, String> generateTopNavigationLinks(){
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		
		map.put("Home", "Home");	
		map.put("GettingStarted", "Getting Started");
		map.put("Tutorials", "Tutorials");
		map.put("Downloads", "Downloads");
		map.put("Context", "Context");
		map.put("Contact", "Contact");
	
		return map;
	}
	
	public LinkedHashMap<String, String> generateSubNavigationLinks(){
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		
		map.put("MyProfile", "My Profile");
		map.put("MyContexts", "My Contexts");
		map.put("MyResources", "My Resources");
		map.put("MyGroups", "My Groups");
		map.put("Administration", "Administration");
	
		return map;
	}
	public LinkedHashMap<String, String> generateUserSectionLinksLoggedOut(){
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		
		map.put("signin", "<div class='glyphicon glyphicon-log-in'></div>&nbsp; Sign in");
		map.put("signup", "<img src='img/signup.png' width='20px' height='20px'>&nbsp; Sign up");
	
		return map;
	}
}
