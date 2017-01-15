package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import spark.Request;
import spark.Spark;

public class SuperRestInterface {

    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_CREATED = 201;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    /**
     * Constructor for the rest interface super class. Sets the server port to
     * 8080.
     */
    public SuperRestInterface() {
        // Change port to default port. Not 4040 the standard spark port.
        Spark.port(80);
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
    protected <T> Object extractContentFromRequest(Request req, Class<T> objectType)
            throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Object recievedObject = mapper.readValue(req.body(), objectType);
        return recievedObject;
    }

}