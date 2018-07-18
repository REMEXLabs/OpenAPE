package org.openape.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.OpenAPEEndPoints;
import org.openape.api.auth.TokenResponse;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.rest.RESTPaths;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

import com.google.gson.Gson;

import spark.Spark;

public class ClientTest {
    private static String testUser = "TestUser";
    private static String testPw = "TestPw";

    static final String environmentContextIdJson = "environmentContextExample1.json";
    static final String equipmentContextIdJson = "equipmentContextExample1.json";
    static final String userContextIdJson = "userContextExample1.json";
	static final String taskContextIdJson = "taskContextExample1.json";
	
    
    @AfterClass
    public static void afterClass() {
        System.out.println("stopping test server");
    	Spark.stop();
    }

    @BeforeClass
    public static void beforeClass() {


        Spark.staticFileLocation("/webcontent"); // Static files

        Spark.get("/hello", (req, res) -> "Hello World");
        Spark.post(RESTPaths.TOKEN, (req, res) -> {return  new Gson().toJson( new TokenResponse("test", "0")); } );
        Spark.get(OpenAPEEndPoints.MY_ID, (req,res) -> "123456");
        Spark.post(RESTPaths.USER_CONTEXTS, (req,res) -> {
//        	UserContext.getObjectFromJson(req.body() );
        	res.header("Location", "http://localhost:4567/testId");
        	res.status(201);
        	return "http://localhost:4567/testId";
        });
        Spark.awaitInitialization();

    }

    private static OpenAPEClient getOpenApeClient() throws MalformedURLException {
        return new OpenAPEClient(ClientTest.testUser, ClientTest.testPw, "http://localhost:4567");
    }

    @Test
    public void testCreateContent() throws URISyntaxException, MalformedURLException {

        final OpenAPEClient client = ClientTest.getOpenApeClient();
        final UserContext userContext = new UserContext();
        userContext.addContext(new Context("testContext", "test"));
        final URI newLocation = client.createUserContext(userContext);
        Assert.assertEquals("http://localhost:4567/testId", newLocation.toString());

    }

    @Test
    public void testGetEquipmentContext() throws MalformedURLException {
    	OpenAPEClient c = getOpenApeClient();
    	EquipmentContext uc = c.getEquipmentContext(equipmentContextIdJson);
    }
    
    @Test
    public void testGetEnvironmentContext() throws MalformedURLException {
    	OpenAPEClient c = getOpenApeClient();
    	EnvironmentContext uc = c.getEnvironmentContext(environmentContextIdJson);
    }
    
    
    @Test
    public void testGetUserContext() throws MalformedURLException {
    	OpenAPEClient c = getOpenApeClient();
    	UserContext uc = c.getUserContext(userContextIdJson);
    }
    
    @Test
    public void testGetTaskContext() throws MalformedURLException {
    	OpenAPEClient c = getOpenApeClient();
    	TaskContext tc = c.getTaskContext(taskContextIdJson);
    System.out.println("tc: " + tc);
    }
    @Test
    public void testFileDownload() throws URISyntaxException, InterruptedException, MalformedURLException {
        
        final OpenAPEClient client = ClientTest.getOpenApeClient();
        final File downloadedFile = client.getResource("http://localhost:4567/test.html",
                "d:/testCopy.html");
        Assert.assertFalse(downloadedFile.equals(null));
        downloadedFile.delete();
    }
}
