package org.openape.server.rest;

import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.openape.api.ContextList;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.requestHandler.ContextRequestHandler;
import org.pac4j.core.profile.CommonProfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Spark;

public abstract class ContextRestInterface extends SuperRestInterface {

    protected static <contextListType> void createContextListRestEndpoint(String path,
            ContextRequestHandler requestHandler, AuthService auth,
            Class<?> contextListType) {

        /*
         * The Rest endpoint for requesting context lists Relates to ISO/IEC
         * 24752-8 7.*.6
         */
        Spark.get(path, (req, res) -> {
            final String url =  req.url().toString();


String[][] filters = new String[0][0];
String paramOwner = req.params("userId");
if (paramOwner != null) {

	filters = new String[1][2];
	filters[0][0] = "owner";
	filters[0][1] = paramOwner;
}
try {
                auth.allowAdmin(req, res);
                return createReturnStringListRequest(req, res,
                        contextListType, requestHandler.getCompleteContextList(url));
            } catch (final UnauthorizedException e) {

                final CommonProfile profile = auth.getAuthenticatedProfile(req, res);
                String owner = profile.getUsername();
                ContextList contextList = null;
                if (owner != null) {
                	System.out.println("Nutzer erkannt");
 contextList = requestHandler.getOverAllContextListOfUser(auth
        .getAuthenticatedUser(req, res).getId(), filters, url);
                                    } else {
                                    	System.out.println("anonymer nutzer");
                                    	contextList = requestHandler.getPublicContextList(url);
                }
                return createReturnStringListRequest(req, res,
                        contextListType, contextList);

            }
        });

    }

    public static String createReturnStringListRequest(final Request req, final Response res,
            final Class<?> type, final Object data) {
        String contentType = req.contentType();
        logger.info("Content type: " + contentType + " instead of " + MediaType.APPLICATION_JSON);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON;
        } // TODO add openApe Setting
        if (contentType.equals(MediaType.APPLICATION_JSON)) {
            try {
                final ObjectMapper mapper = new ObjectMapper();

                String jsonData = mapper.writeValueAsString(data);

                // TODO remove hotfix here
                jsonData = jsonData.replace("null/", "");

                return jsonData;
            } catch (final JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (contentType.equals(MediaType.APPLICATION_XML)) {
            try {
                final JAXBContext context = JAXBContext.newInstance(type);
                Marshaller m;

                m = context.createMarshaller();
                final StringWriter sw = new StringWriter();

                m.marshal(data, sw);
                return sw.toString();

            } catch (final JAXBException e) {
                SuperRestInterface.logger.warn(e.toString());
                res.status(500);
                return "Internal server error";
            }

        } else {
            res.status(400);
            return "wrong content-type";
        }
        return null;
    }
}
