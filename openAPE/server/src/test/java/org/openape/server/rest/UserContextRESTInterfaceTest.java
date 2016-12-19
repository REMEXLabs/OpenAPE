package org.openape.server.rest;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.usercontext.UserContext;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

import spark.Spark;

public class UserContextRESTInterfaceTest extends SuperRestInterface {
	
	@Path("myresource")
    public static class MyResource {

//        @POST
//        @Consumes(MediaType.TEXT_PLAIN)
//        @Produces(MediaType.APPLICATION_XML)
//        public MyModel createMyModel(int number) {
//
//            return new MyModel(number);
//        }

	}
	
	static TJWSEmbeddedJaxrsServer server = new TJWSEmbeddedJaxrsServer();
	
	
	@BeforeClass
	public static void setup() throws Exception {
		server.setPort(12345);
		
		server.getDeployment().getResources().add(new UserContextRESTInterface(new UserContextRequestHandler()));
		server.getDeployment().getResources().add(new EnvironmentContextRESTInterface(new EnvironmentContextRequestHandler()));
		server.getDeployment().getResources().add(new EquipmentContextRESTInterface(new EquipmentContextRequestHandler()));
		server.getDeployment().getResources().add(new TaskContextRESTInterface(new TaskContextRequestHandler()));
		
		server.start();
	}
	@AfterClass
	public static void close() throws Exception {
		server.stop();
	}
	
//	@Test
	
	
//    public UserContextRESTInterfaceTest(final UserContextRequestHandler requestHandler) {
//        super();
//
//        /**
//         * test request to test if the server runs. Invoke locally using:
//         * http://localhost:8080/hello
//         */
//        Spark.get("/hello", (req, res) -> "Hello World");
//
//        /**
//         * Request 7.2.2 create user-context.
//         */
//        Spark.post("/api/user-contexts", (req, res) -> {
//            try {
//                // Try to map the received json object to a userContext
//                // object.
//                UserContext recievedUserContext = (UserContext) this.extractContentFromRequest(req,
//                        UserContext.class);
//                // Test the object for validity.
//                if (!recievedUserContext.isValid()) {
//                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
//                    return "";
//                }
//                // If the object is okay, save it and return the id.
//                String userContextId = requestHandler.createUserContext(recievedUserContext);
//                res.status(SuperRestInterface.HTTP_STATUS_OK);
//                res.type("application/json");
//                return userContextId;
//            } catch (JsonParseException | JsonMappingException e) {
//                // If the parse is not successful return bad request error code.
//                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
//                return "";
//            } catch (IOException e) {
//                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
//                return "";
//            }
//        });
//
//        /**
//         * Request 7.2.3 get user-context. Used to get a specific user context
//         * identified by ID.
//         */
//        Spark.get("/api/user-contexts/:user-context-id", (req, res) -> {
//            String userContextId = req.params(":user-context-id");
//            try {
//                // if it is successful return user context.
//                UserContext userContext = requestHandler.getUserContextById(userContextId);
//                res.status(SuperRestInterface.HTTP_STATUS_OK);
//                res.type("application/json");
//                return userContext;
//                // if not return corresponding error status.
//            } catch (IllegalArgumentException e) {
//                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
//                return "";
//            } catch (IOException e) {
//                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
//                return "";
//            }
//
//        });
//
//        /**
//         * Request 7.2.4 update user-context.
//         */
//        Spark.put(
//                "/api/user-contexts/:user-context-id",
//                (req, res) -> {
//                    String userContextId = req.params(":user-context-id");
//                    try {
//                        UserContext recievedUserContext = (UserContext) this
//                                .extractContentFromRequest(req, UserContext.class);
//                        // Test the object for validity.
//                        if (!recievedUserContext.isValid()) {
//                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
//                            return "";
//                        }
//                        // If the object is okay, update it.
//                        requestHandler.updateUserContextById(userContextId, recievedUserContext);
//                        res.status(SuperRestInterface.HTTP_STATUS_OK);
//                        return "";
//                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
//                        // If the parse or update is not successful return bad
//                        // request
//                        // error code.
//                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
//                        return "";
//                    } catch (IOException e) {
//                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
//                        return "";
//                    }
//                });
//
//        /**
//         * Request 7.2.5 delete user-context.
//         */
//        Spark.delete("/api/user-contexts/:user-context-id", (req, res) -> {
//            String userContextId = req.params(":user-context-id");
//            try {
//                // if it is successful return user context.
//                requestHandler.deleteUserContextById(userContextId);
//                res.status(SuperRestInterface.HTTP_STATUS_OK);
//                return "";
//                // if not return corresponding error status.
//            } catch (IllegalArgumentException e) {
//                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
//                return "";
//            } catch (IOException e) {
//                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
//                return "";
//            }
//        });
//
//    }

}
