package org.openape.server.rest;

import java.io.File;
import java.io.IOException;

import org.openape.api.Messages;
import org.openape.api.groups.GroupMembershipRequest;
import org.openape.server.admin.AdminInterface;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.GroupManagementHandler;
import org.openape.server.requestHandler.ListingRequestHandler;
import org.openape.server.requestHandler.ResourceDescriptionRequestHandler;
import org.openape.server.requestHandler.ResourceRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.openape.ui.velocity.controller.Administration;
import org.openape.ui.velocity.controller.Contact;
import org.openape.ui.velocity.controller.Context;
import org.openape.ui.velocity.controller.Downloads;
import org.openape.ui.velocity.controller.GettingStarted;
import org.openape.ui.velocity.controller.Index;
import org.openape.ui.velocity.controller.LegalNotice;
import org.openape.ui.velocity.controller.MyContexts;
import org.openape.ui.velocity.controller.MyGroups;
import org.openape.ui.velocity.controller.MyProfile;
import org.openape.ui.velocity.controller.MyResources;
import org.openape.ui.velocity.controller.Tutorials;
import org.openape.ui.velocity.controller.Tutorials_Workflow;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.openape.ui.velocity.requestHandler.MyResourcesRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Spark;
import spark.route.Routes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SuperRestInterface {
    protected static Logger logger = LoggerFactory.getLogger(SuperRestInterface.class);
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_CREATED = 201;
    public static final int HTTP_STATUS_NO_CONTENT = 204;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    private static final boolean TEST_ENVIRONMENT = true;

    public static GroupMembershipRequest extractFromRequest(
            final Class<GroupMembershipRequest> class1, final Request req) {
        return null;
    }

    /**
     * Get a sent json object from a request.
     *
     * @param req
     *            spark request containing the object.
     * @param objectType
     *            expected class of the object.
     * @return received java object of the type objectType.
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     */
    protected static <T> Object extractObjectFromRequest(final Request req,
            final Class<T> objectType) throws IOException, JsonParseException, JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        final Object recievedObject = mapper.readValue(req.body(), objectType);
        return recievedObject;
    }

    /**
     * Constructor for the rest interface super class. Creates all rest end
     * points of the application.
     */
    public SuperRestInterface() {

        SuperRestInterface.logger.info("Setting up REST API");

        Spark.staticFiles.location("webcontent");

        final File extContent = new File(System.getProperty("java.io.tmpdir") + "/extContent");
        if (!extContent.exists()) {
            extContent.mkdir();
            SuperRestInterface.logger.info("created new folder:" + extContent.getAbsolutePath());
        } else {
            SuperRestInterface.logger.info("Found folder:" + extContent.getAbsolutePath());

        }
        Spark.staticFiles.externalLocation(System.getProperty("java.io.tmpdir") + "/extContent");

        // before filter enables CORS
        Spark.before("/*", (q, response) -> {
            
            SuperRestInterface.logger.debug("Received api call: " + q.protocol() + "" + q.uri());
            logger.debug("authorisation: " + q.headers("authorization") );
            response.header("Access-Control-Allow-Origin", "*");
            // response.header("Access-Control-Request-Method",
            // "GET,PUT,POST,DELETE,OPTIONS");
                response.header("Access-Control-Request-Method", "*");
                // response.header("Access-Control-Allow-Headers", headers);
                response.header("Access-Control-Allow-Headers", "Authorization");
                response.header("Access-Control-Max-Age", "1728000");
                response.header("Cache-Control", "no-cache");

            });
        Spark.options(
                "/*",
                (request, response) -> {

                    final String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    final String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }

                    return 200;
                });

        /*
         * Spark.before((request, response) -> {
         * response.header("Access-Control-Allow-Origin", "*");
         * response.header("Access-Control-Request-Method", "*");
         * response.header("Access-Control-Allow-Headers", "*");
         * response.header("Access-Control-Max-Age", "1728000");
         * response.header("Cache-Control", "no-cache");
         * 
         * });
         */
        Spark.get("api", (request, response) -> new API());

        // AuthService singleton to enable security features on REST endpoints
        final AuthService authService = new AuthService();

        // Catch and print exceptions
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        // Test endpoint to see if server runs. Invoke locally:
        // http://localhost:4567/hello
        Spark.get(Messages.getString("UserContextRESTInterface.HelloWorldURL"), //$NON-NLS-1$
                (request, response) -> Messages.getString("UserContextRESTInterface.HelloWorld")); //$NON-NLS-1$

        Spark.get(Messages.getString("SuperRestInterface.HelloWorldURL"), //$NON-NLS-1$
                (request, response) -> Messages.getString("SuperRestInterface.HelloWorld")); //$NON-NLS-1$
        // Endpoint to receive tokens
        AdminInterface.setupAdminRestInterface(authService);
        TokenRESTInterface.setupTokenRESTInterface(authService);
        ProfileRESTInterface.setupProfileRESTInterface();
                

        try {
            Administration.setupAdministrationVELOCITYInterface(new AdminSectionRequestHandler(), authService);
            GettingStarted.setupGettingStartedVELOCITYInterface();
            Tutorials.setupTutorialsVELOCITYInterface();
            Downloads.setupDownloadsVELOCITYInterface();
            Context.setupContextVELOCITYInterface(new AdminSectionRequestHandler());
            Contact.setupContactVELOCITYInterface();
            MyProfile.setupMyProfileVELOCITYInterface();
            MyContexts.setupMyContextsVELOCITYInterface(new AdminSectionRequestHandler(), authService );
            MyResources.setupMyResourcesVELOCITYInterface(new MyResourcesRequestHandler());
            MyGroups.setupMyGroupsVELOCITYInterface();
            
            Index.setupIndexVELOCITYInterface();
            Tutorials_Workflow.setupTutorialsWorkflowVELOCITYInterface();
            // TODO exception handling
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Resource endpoints
        try {
            GroupManagementRestInterface.setupGroupManagementRestInterface(
                    new GroupManagementHandler(), authService);
          //TODO exception handling
        } catch (final IOException e) {
            e.printStackTrace();
        }
        // REST-Interfaces defined in ISO/IEC 24752-8
        EnvironmentContextRESTInterface.setupEnvironmentContextRESTInterface(
                 EnvironmentContextRequestHandler.getInstance(), authService);
        EquipmentContextRESTInterface.setupEquipmentContextRESTInterface(
                EquipmentContextRequestHandler.getInstance(), authService);
        ListingRESTInterface.setupListingRESTInterface(new ListingRequestHandler());
        ResourceRESTInterface.setupResourceRESTInterface(new ResourceRequestHandler(), authService);
        ResourceDescriptionRESTInterface.setupResourceDescriptionRESTInterface(
                new ResourceDescriptionRequestHandler(), authService);
        TaskContextRESTInterface.setupTaskContextRESTInterface(TaskContextRequestHandler.getInstance(),
                authService);
        UserContextRESTInterface.setupUserContextRESTInterface(UserContextRequestHandler.getInstance(),
                authService);
        SuperRestInterface.logger.info("REST API successfully set up");

        // Test html interface found
        if (SuperRestInterface.TEST_ENVIRONMENT) {
            TestRESTInterface.setupTestRESTInterface();
        }

        // redirect to index if no path was found in the url
//        Spark.redirect.get("/", "/index");
        
    }

}