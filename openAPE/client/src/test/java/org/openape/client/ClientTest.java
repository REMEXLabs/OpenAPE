package org.openape.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.auth.TokenResponse;
import org.openape.api.rest.RESTPaths;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

import com.google.gson.Gson;

import spark.Spark;

public class ClientTest {
    private static String testUser = "TestUser";
    private static String testPw = "TestPw";

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }

    @BeforeClass
    public static void beforeClass() {


        Spark.staticFileLocation("/webcontent"); // Static files

        Spark.get("/hello", (req, res) -> "Hello World");
        Spark.post(RESTPaths.TOKEN, (req, res) -> {return  new Gson().toJson( new TokenResponse("test", "0")); } );
        Spark.post(RESTPaths.USER_CONTEXTS, (req,res) -> {
//        	UserContext.getObjectFromJson(req.body() );
        	res.header("Location", "http://localhost:4567/testId");
        	res.status(201);
        	return "http://localhost:4567/testId";
        });
        Spark.awaitInitialization();

    }

    private static OpenAPEClient getOpenApeClient() throws MalformedURLException {
        return new OpenAPEClient(ClientTest.testUser, ClientTest.testPw, "http://localhost:4567/");
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
    public void testFileDownload() throws URISyntaxException, InterruptedException, MalformedURLException {
        
        final OpenAPEClient client = ClientTest.getOpenApeClient();
        final File downloadedFile = client.getResource("http://localhost:4567/test.html",
                "d:/testCopy.html");
        Assert.assertFalse(downloadedFile.equals(null));
        downloadedFile.delete();
    }
}
