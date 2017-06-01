package org.openape.server.rest;

import java.io.File;
import java.io.IOException;

import org.openape.api.Messages;
import org.openape.server.Main;
import org.openape.server.auth.AuthService;
import org.openape.server.filter.CorsFilter;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.ListingRequestHandler;
import org.openape.server.requestHandler.ResourceDescriptionRequestHandler;
import org.openape.server.requestHandler.ResourceRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Spark;

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
   CorsFilter.apply();
    	Spark.get("api", (req,res) -> new API()                                                                    );
    	
        // AuthService singleton to enable security features on REST endpoints
        final AuthService authService = new AuthService();

        // Catch and print exceptions
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        // Test endpoint to see if server runs. Invoke locally: http://localhost:4567/hello
    	Spark.get(
                Messages.getString("UserContextRESTInterface.HelloWorldURL"), (req, res) -> Messages.getString("UserContextRESTInterface.HelloWorld")); //$NON-NLS-1$ //$NON-NLS-2$

        

Spark.get(Messages.getString("SuperRestInterface.HelloWorldURL"), (req, res) -> Messages.getString("SuperRestInterface.HelloWorld")); //$NON-NLS-1$ //$NON-NLS-2$
        // Endpoint to receive tokens
        TokenRESTInterface.setupTokenRESTInterface(authService);
        ProfileRESTInterface.setupProfileRESTInterface();

        // Resource endpoints
        EnvironmentContextRESTInterface.setupEnvironmentContextRESTInterface(new EnvironmentContextRequestHandler(), authService);
        EquipmentContextRESTInterface.setupEquipmentContextRESTInterface(new EquipmentContextRequestHandler(), authService);
        ListingRESTInterface.setupListingRESTInterface(new ListingRequestHandler());
                
ResourceDescriptionRESTInterface.setupResourceDescriptionRESTInterface(new ResourceDescriptionRequestHandler(), authService);
        
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