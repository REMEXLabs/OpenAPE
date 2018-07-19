package org.openape.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.OpenAPEEndPoints;
import org.openape.api.auth.TokenResponse;
import org.openape.api.databaseObjectBase.Property;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.rest.RESTPaths;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.Preference;
import org.openape.api.usercontext.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import spark.Spark;

public class ClientTest {
    private static String testUser = "TestUser";
    private static String testPw = "TestPw";

    static final String environmentContextIdJson = "environmentContextExample1.json";
    static final String equipmentContextIdJson = "equipmentContextExample1.json";
    static final String userContextIdJson = "userContextExample1.json";
	static final String taskContextIdJson = "taskContextExample1.json";
	
private static Logger logger = LoggerFactory.getLogger(ClientTest.class);
	
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
        Spark.post(RESTPaths.TASK_CONTEXTS, (req,res) -> {
        	String body = req.body();
        	logger.info("Received message for parsin. Task Context: " + body);
        	try {
        	TaskContext.getObjectFromJson(body);
        	
        	} catch (Exception e) {
				logger.error(e.toString() );
			e.printStackTrace();;
        	}
        	res.header("Location", "http://localhost:4567/testId");
        	res.status(201);
        	return "success";
        });
        
        Spark.post(RESTPaths.USER_CONTEXTS, (req,res) -> {
String body = req.body();
        	logger.info("Received message for parsing: " + body );
        	try {
        	UserContext.getObjectFromJson(body );
        	
        	} catch (Exception e) {
        		logger.error("Error: Parsing error for incoming user context: " + body);
        		e.printStackTrace();
        		throw e;
				
			}
        	res.header("Location", "http://localhost:4567/testId");
        	res.status(201);
        	return "http://localhost:4567/testId";
        });
        
        Spark.put("api/user-contexts/updateUserContext", (req,res) -> {
        	String body = req.body();
        	logger.info("received message: " + body);
        	try {
        	UserContext.getObjectFromJson(body );
        	} catch (Exception e) {
        		logger.error("Parsing and updating user context failed");
        		throw e;
				
			}
        	res.status(200);
        	return "success";
        });
        
        Spark.put("api/task-contexts/updateTaskContext", (req,res) -> {
        	String body = req.body();
        	logger.info("received message: " + body);
        	try {
        	TaskContext.getObjectFromJson(body );
        	} catch (Exception e) {
        		logger.error("Parsing and updating user context failed");
        		throw e;
				
			}
        	res.status(200);
        	return "success";
        });
        
        
        Spark.awaitInitialization();

    }

    private static OpenAPEClient getOpenApeClient() throws MalformedURLException {
        return new OpenAPEClient(ClientTest.testUser, ClientTest.testPw, "http://localhost:4567");
    }

    @Test
    public void testCreateTaskContext() throws URISyntaxException, IOException {
    	OpenAPEClient c = getOpenApeClient();
    	TaskContext tc = createTestTaskContext(); 
    			    	URI url = c.createTaskContext(tc);
    	Assert.assertEquals("http://localhost:4567/testId", url.toString());
    }
    
    private TaskContext createTestTaskContext() {
		TaskContext tc = new TaskContext();
    	tc.addProperty(new Property("test", "testValue")) ;

		return tc;
	}

	@Test
    public void testCreateUserContext() throws URISyntaxException, IOException {

        final OpenAPEClient client = ClientTest.getOpenApeClient();
        final UserContext userContext = createTestUserContext(); 
        final URI newLocation = client.createUserContext(userContext);
        Assert.assertEquals("http://localhost:4567/testId", newLocation.toString());

    }

    private UserContext createTestUserContext() {
		UserContext uc = new UserContext();
Context context = new Context("default", "This is a default context");
uc.addContext(context);
context.addPreference(new Preference("testPreference", "test"));


		return uc;
	}
@Test
	public void testUpdateUserContext() throws IOException {
    	final OpenAPEClient client = ClientTest.getOpenApeClient();
    	Assert.assertTrue	(
    	client.updateUserContext("updateUserContext", createTestUserContext())
    	);
    }

@Test
public void testUpdateTaskContext() throws IOException {
	final OpenAPEClient client = ClientTest.getOpenApeClient();
	Assert.assertTrue	(
	client.updateTaskContext("updateTaskContext", createTestTaskContext())
	);
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
