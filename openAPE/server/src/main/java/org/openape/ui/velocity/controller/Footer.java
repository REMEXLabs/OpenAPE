package org.openape.ui.velocity.controller;
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
		String footerContent = "&#9400; Hochschuel der Medien 2017 <br> <a href='http://localhost:4567/legalNotice'>Legal Notice</a>";
		return footerContent;	
	}
}
