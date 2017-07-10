package org.openape.server.rest;

import static spark.Spark.get;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openape.api.Messages;
import org.openape.server.Main;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.ListingRequestHandler;
import org.openape.server.requestHandler.ResourceDescriptionRequestHandler;
import org.openape.server.requestHandler.ResourceRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.openape.ui.velocity.Adminsection;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.Request;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SuperRestInterface {
	static Logger logger = LoggerFactory.getLogger(SuperRestInterface.class	);
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_CREATED = 201;
    public static final int HTTP_STATUS_NO_CONTENT = 204;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    private static final boolean TEST_ENVIRONMENT = true;

    /**
     * Get a sent json object from a request.
     *
     * @param req spark request containing the object.
     * @param objectType expected class of the object.
     * @return received java object of the type objectType.
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     */
    protected static <T> Object extractObjectFromRequest(Request req, Class<T> objectType)
            throws IOException, JsonParseException, JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        final Object recievedObject = mapper.readValue(req.body(), objectType);
        return recievedObject;
    }

    /**
     * Constructor for the rest interface super class. Creates all rest end
     * points of the application.
     */
    public SuperRestInterface() {

    	logger.info("Setting up REST API");	

        

    	Spark.staticFiles.location("webcontent");
        
    	File extContent = new File(System.getProperty("java.io.tmpdir")+"/extContent");
    	if (!extContent.exists())
    	{ 
    		extContent.mkdir();
    	logger.info("created new folder:" + extContent.getAbsolutePath() );	
    	} else {
    		logger.info("Found folder:" + extContent.getAbsolutePath() );
    	
    	}
    	Spark.staticFiles.externalLocation(System.getProperty("java.io.tmpdir")+"/extContent");
    	
//   before filter enables CORS
       /* Spark.before("/*", (q, response) -> {
        	logger.debug("Received api call: " + q.protocol() + "" + q.uri());
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Request-Method", "GET,PUT,POST,DELETE,OPTIONS");
//                response.header("Access-Control-Allow-Headers", headers);

        });*/
        
   	 Spark.options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return 200;
	    });

	   Spark.before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", "*");
	        response.header("Access-Control-Request-Method", "*");
	        response.header("Access-Control-Allow-Headers", "*");
	        response.header("Access-Control-Max-Age", "1728000");
	        response.header("Cache-Control", "no-cache");
	
	    });

    	Spark.get("api", (request, response) -> new API()                                                                    );
    	
        // AuthService singleton to enable security features on REST endpoints
        final AuthService authService = new AuthService();

        // Catch and print exceptions
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        // Test endpoint to see if server runs. Invoke locally: http://localhost:4567/hello
    	Spark.get(
                Messages.getString("UserContextRESTInterface.HelloWorldURL"), (request, response) -> Messages.getString("UserContextRESTInterface.HelloWorld")); //$NON-NLS-1$ //$NON-NLS-2$

        

    	Spark.get(Messages.getString("SuperRestInterface.HelloWorldURL"), (request, response) -> Messages.getString("SuperRestInterface.HelloWorld")); //$NON-NLS-1$ //$NON-NLS-2$
        // Endpoint to receive tokens
        TokenRESTInterface.setupTokenRESTInterface(authService);
        ProfileRESTInterface.setupProfileRESTInterface();

        // Resource endpoints
        EnvironmentContextRESTInterface.setupEnvironmentContextRESTInterface(new EnvironmentContextRequestHandler(), authService);
        EquipmentContextRESTInterface.setupEquipmentContextRESTInterface(new EquipmentContextRequestHandler(), authService);
        ListingRESTInterface.setupListingRESTInterface(new ListingRequestHandler());
                
ResourceDescriptionRESTInterface.setupResourceDescriptionRESTInterface(new ResourceDescriptionRequestHandler(), authService);
        
		
        try {
			Adminsection.setupAdminVELOCITYInterface(new AdminSectionRequestHandler());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //REST-Interfaces
		ResourceRESTInterface.setupResourceRESTInterface(new ResourceRequestHandler());
        TaskContextRESTInterface.setupTaskContextRESTInterface(new TaskContextRequestHandler(), authService);
        UserContextRESTInterface.setupUserContextRESTInterface(new UserContextRequestHandler(), authService);
        logger.info("REST API successfully set up");

        // Test html interface found
        if(SuperRestInterface.TEST_ENVIRONMENT) {
            TestRESTInterface.setupTestRESTInterface();
        }           
    }

}