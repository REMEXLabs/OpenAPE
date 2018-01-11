package org.openape.server.rest;

import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.jetty.http.HttpParser.RequestHandler;
import org.openape.api.Messages;
import org.openape.api.UserContextList;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.pac4j.core.profile.CommonProfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Spark;

public abstract class ContextRestInterface extends SuperRestInterface {

	protected static <contextListType> void createContextListRestEndpoint(String path,
			UserContextRequestHandler requestHandler, AuthService auth, Class<UserContextList> contextListType) {
		
		/*The Rest endpoint for requesting context lists
		 *Relates to ISO/IEC 24752-8 7.*.6 
		 */
		Spark.get(path, (req, res) -> {
			final String url = req.uri().toString();
			logger.info("requesting lists");
			try {
				auth.allowAdmin(req, res);
				return UserContextRESTInterface.createReturnStringListRequest(req, res, contextListType,
						requestHandler.getAllUserContexts(url));
			} catch (final UnauthorizedException e) {
				try {
					
					final CommonProfile profile = auth.getAuthenticatedProfile(req, res);
					String owner = profile.getUsername();
					auth.allowAdminAndOwner(profile, owner);
				return UserContextRESTInterface.createReturnStringListRequest(req, res, contextListType,
						requestHandler.getMyContexts(auth.getAuthenticatedUser(req, res).getId(), url));
				} catch (final UnauthorizedException ex  ) {
					return UserContextRESTInterface.createReturnStringListRequest(req, res, contextListType,
							requestHandler.getPublicContexts(url));

				}
			}
		});

	}

	public static String createReturnStringListRequest(final Request req, final Response res, final Class type,
			final Object data) {
		final String contentType = req.contentType();
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
