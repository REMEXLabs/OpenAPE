//package org.openape.server.rest;
//
//import java.io.IOException;
//
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.openape.api.usercontext.UserContext;
//import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
//import org.openape.server.requestHandler.EquipmentContextRequestHandler;
//import org.openape.server.requestHandler.TaskContextRequestHandler;
//import org.openape.server.requestHandler.UserContextRequestHandler;
//import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
//
//import spark.Spark;
//
//public class UserContextRESTInterfaceTest extends SuperRestInterface {
//
//	@Path("myresource")
//    public static class MyResource {
//
////        @POST
////        @Consumes(MediaType.TEXT_PLAIN)
////        @Produces(MediaType.APPLICATION_XML)
////        public MyModel createMyModel(int number) {
////
////            return new MyModel(number);
////        }
//
//	}
//
//	static TJWSEmbeddedJaxrsServer server = new TJWSEmbeddedJaxrsServer();
//
//
//	@BeforeClass
//	public static void setup() throws Exception {
//		server.setPort(12345);
//
//		server.getDeployment().getResources().add(new UserContextRESTInterface(new UserContextRequestHandler()));
//		server.getDeployment().getResources().add(new EnvironmentContextRESTInterface(new EnvironmentContextRequestHandler()));
//		server.getDeployment().getResources().add(new EquipmentContextRESTInterface(new EquipmentContextRequestHandler()));
//		server.getDeployment().getResources().add(new TaskContextRESTInterface(new TaskContextRequestHandler()));
//
//		server.start();
//	}
//	@AfterClass
//	public static void close() throws Exception {
//		server.stop();
//	}
//
////	@Test
//
//
////
////    }
//
// }
