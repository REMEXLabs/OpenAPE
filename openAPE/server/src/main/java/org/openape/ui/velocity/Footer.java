package org.openape.ui.velocity;
import spark.ModelAndView;
import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.rest.SuperRestInterface;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Spark;

public class Footer {
	public String generateFooter(){
		String footerContent = "Hochschuel der Medien 2017 <br> <a href='http://www.google.de'>Legal Notice</a>";
		return footerContent;	
	}

}
