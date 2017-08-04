package org.openape.client;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.rest.RESTPaths;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;

import spark.Spark;

public class ClientTest {
	private static String testUser = "TestUser";
	private static String testPw = "TestPw";

	@BeforeClass
	public static void beforeClass() {

		Spark.staticFileLocation("/webcontent"); // Static files

		Spark.get("/hello", (req, res) -> "Hello World");
		Spark.post(RESTPaths.USER_CONTEXTS, "application/json",
				(req, res) -> TestServer.createUserContext(req.body(), res));
		Spark.awaitInitialization();

	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}

	@Test
	public void testFileDownload() throws URISyntaxException, InterruptedException {
		// Thread.sleep(60000);;
		final OpenAPEClient client = ClientTest.getOpenApeClient();
		final File downloadedFile = client.getResource("http://localhost:4567/test.html", "d:/testCopy.html");
		Assert.assertFalse(downloadedFile.equals(null));
	}

	@Test
	public void testCreateContent() throws URISyntaxException {

		final OpenAPEClient client = ClientTest.getOpenApeClient();
		final UserContext userContext = new UserContext();
		userContext.addContext("testContext", new Context("test"));
		final URI newLocation = client.createUserContext(userContext);
		Assert.assertEquals("http://localhost:4567/testId", newLocation.toString());

	}

	private static OpenAPEClient getOpenApeClient() {
		return new OpenAPEClient(ClientTest.testUser, ClientTest.testPw, "http://localhost:4567/");
	}
}
