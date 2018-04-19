package org.openape.server.rest;

import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
            final String url = req.uri().toString();

            try {
                auth.allowAdmin(req, res);
                return createReturnStringListRequest(req, res,
                        contextListType, ContextRequestHandler.getCompleteContextList(url));
            } catch (final UnauthorizedException e) {

                final CommonProfile profile = auth.getAuthenticatedProfile(req, res);
                String owner = profile.getUsername();

                if (owner != null) {

                    return createReturnStringListRequest(req, res,
                            contextListType, ContextRequestHandler.getContextListOfUser(auth
                                    .getAuthenticatedUser(req, res).getId(), url));
                } else {

                    return createReturnStringListRequest(req, res,
                            contextListType, requestHandler.getPublicContextList(url));

                }
            }
        });

    }

    public static String createReturnStringListRequest(final Request req, final Response res,
            final Class<?> type, final Object data) {
        String contentType = req.contentType();
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON;
        } // TODO add openApe Setting
        if (contentType.equals(MediaType.APPLICATION_JSON)) {
            try {
                final ObjectMapper mapper = new ObjectMapper();

                final String jsonData = mapper.writeValueAsString(data);
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
