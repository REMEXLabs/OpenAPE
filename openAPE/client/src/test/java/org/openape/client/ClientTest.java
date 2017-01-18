package org.openape.client;
import static org.junit.Assert.assertFalse;
import static spark.Spark.get;
import static spark.Spark.staticFiles;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;


import java.io.File;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.Spark;

public class ClientTest {
	 @BeforeClass
	  public static void beforeClass() {

//		 staticFiles.location("/webcontent"); // Static files
		
		 get("/hello", (req, res) -> "Hello World");
        awaitInitialization();

	  }
	 
	  @AfterClass
	  public static void afterClass() {
	    Spark.stop();
	  }

	  
	  @Test
	 public void testClient() throws URISyntaxException, InterruptedException {

		  OpenAPEClient client = new OpenAPEClient("http://localhost:4567/");
File downloadedFile = client.getResource("http://www.openurc.org", "d:/testCopy.html");
assertFalse(downloadedFile.equals(null)  );	  
}

}
