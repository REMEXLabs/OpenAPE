package org.openape.client;

import static spark.Spark.post;

import java.net.MalformedURLException;

import java.net.URISyntaxException;

import spark.Response;

public class TestServer {

    public static Object createUserContext(final String body, final Response res) {
        // TODO Auto-generated method stub

        res.status(201);
        res.header("Location", "http://localhost:4567/testId");

        return "successful";
    }

    public static void main(final String[] args) throws URISyntaxException, InterruptedException {
        // post (RESTPaths.USER_CONTEXTS,"application/json",(req, res) ->
        // TestServer.createUserContext(req.body(),res));
        ClientTest.beforeClass();
        new ClientTest().testFileDownload();
        ClientTest.afterClass();
    }

}
