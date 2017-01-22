package org.openape.client;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.rest.RESTPaths;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

import spark.Spark;
import static spark.Spark.staticFileLocation;
public class ClientTest {
	 @BeforeClass
	  public static void beforeClass() {

	 staticFileLocation("/webcontent"); // Static files
		
		 get("/hello", (req, res) -> "Hello World");
		 post (RESTPaths.USER_CONTEXTS,"application/json",(req, res) -> TestServer.createUserContext(req.body(),res));
        awaitInitialization();

	  }
	 
	  @AfterClass
	  public static void afterClass() {
	    Spark.stop();
	  }

	  
	  @Test
	 public void testFileDownload() throws URISyntaxException, InterruptedException {
//Thread.sleep(60000);;
		  OpenAPEClient client = new OpenAPEClient("http://localhost:4567/");
File downloadedFile = client.getResource("http://localhost:4567/test.html", "d:/testCopy.html");
assertFalse(downloadedFile.equals(null)  );	  
}

	  @Test
	  public void testCreateContent() throws URISyntaxException{
		  
		  OpenAPEClient client = new OpenAPEClient("http://localhost:4567/");
		  UserContext userContext = new UserContext();
		  userContext.addContext(new Context("user"
, "test user") );
		  URI newLocation = client.createUserContext(userContext);
		  assertEquals("http://localhost:4567/testId", newLocation.toString() );
		  
		  
	  }
}
