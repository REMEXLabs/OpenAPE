package org.openape.server.rest;

import java.io.IOException;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.openape.api.Messages;
import org.openape.api.UserContextList;
import org.openape.api.usercontext.UserContext;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.requestHandler.UserContextRequestHandler;

import spark.Request;
import spark.Response;
import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserContextRESTInterface extends SuperRestInterface {

    private static UserContext createRequestObejct(final Request req)
            throws IllegalArgumentException {
        final String contentType = req.contentType();
        if (contentType.equals(MediaType.APPLICATION_JSON)) {
            return UserContext.getObjectFromJson(req.body());
        } else if (contentType.equals(MediaType.APPLICATION_XML)) {
            return UserContext.getObjectFromXml(req.body());
        } else {
            SuperRestInterface.logger.debug("Received Message with wrong content-type");
            throw new IllegalArgumentException("wrong content-type");
        }
    }

    private static String createReturnString(final Request req, final UserContext userContext)
            throws IOException, IllegalArgumentException {
        final String contentType = req.contentType();

        if (contentType != null) {
            if (contentType.equals(MediaType.APPLICATION_JSON)) {
                return userContext.getForntEndJson();
            } else if (contentType.equals(MediaType.APPLICATION_XML)) {
                return userContext.getXML();
            } else {
                throw new IllegalArgumentException("wrong content-type");
            }
        } else {
            return userContext.getXML();
        }

    }

    private static String createReturnStringListRequest(final Request req, final Response res,
            final Class type, final Object data) {
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

    public static void setupUserContextRESTInterface(
            final UserContextRequestHandler requestHandler, final AuthService auth) {

        // Authentication: Make sure only registered principals (users and
        // admins) can create a new context
        Spark.before(Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"),
                auth.authorize("user"));
        // Authentication: Everyone can access the route for a specific ID
        Spark.before(Messages.getString("UserContextRESTInterface.UserContextURLWithID"),
                auth.authorize("anonymous"));

       

        /**
         * Request 7.2.2 create user-context. Can only be accessed by roles
         * "user" and "admin.
         */
        Spark.post(
                Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    SuperRestInterface.logger.info("bla");
                    try {
                        // Try to map the received json object to a userContext
                        // object.
                        final UserContext receivedUserContext = UserContextRESTInterface
                                .createRequestObejct(req);
                        // Make sure to set the id of the authenticated user as
                        // the ownerId
                        SuperRestInterface.logger.debug("lusm: requesting user");
                        final String id = auth.getAuthenticatedUser(req, res).getId();
                        SuperRestInterface.logger.info("id: " + id);
                        receivedUserContext.getImplementationParameters().setOwner(
                                auth.getAuthenticatedUser(req, res).getId());
                        SuperRestInterface.logger.debug("Lusm: success");
                        // Test the object for validity.
                        if (!receivedUserContext.isValid()) {
                            SuperRestInterface.logger.info("lusm: none valide");
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("UserContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String userContextId = requestHandler
                                .createUserContext(receivedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        return userContextId;
                    } catch (final IllegalArgumentException e) {
                        // If the parse is not successful return bad request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         * Request 7.2.3 get user-context. Used to get a specific user context
         * identified by ID.
         */
        Spark.get(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return user context.
                        final UserContext userContext = requestHandler
                                .getUserContextById(userContextId);
                        // Make sure only admins or the owner can view the
                        // context, except if it is public
                        auth.allowAdminOwnerAndPublic(req, res, userContext
                                .getImplementationParameters().getOwner(), userContext
                                .getImplementationParameters().isPublic());
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("UserContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        final String jsonData = UserContextRESTInterface.createReturnString(req,
                                userContext);
                        return jsonData;
                        // if not return corresponding error status.
                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         * Request 7.2.4 update user-context.
         */
        Spark.put(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        final UserContext receivedUserContext = UserContextRESTInterface
                                .createRequestObejct(req);
                        // Test the object for validity.
                        if (!receivedUserContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("UserContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // Check if the user context does exist
                        final UserContext userContext = requestHandler
                                .getUserContextById(userContextId);
                        // Make sure only admins and the owner can update a
                        // context
                        auth.allowAdminAndOwner(req, res, userContext.getImplementationParameters()
                                .getOwner());
                        receivedUserContext.getImplementationParameters().setOwner(
                                userContext.getImplementationParameters().getOwner()); // Make
                        // sure
                        // the
                        // owner
                        // can't
                        // be
                        // changed
                        // Perform the update
                        requestHandler.updateUserContextById(userContextId, receivedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$ //TODO
                                                                                           // $NON-NLS-1$
                                                                                           //$NON-NLS-1$ return
                                                                                           // $NON-NLS-1$
                                                                                           //$NON-NLS-1$ right
                                                                                           // $NON-NLS-1$
                                                                                           //$NON-NLS-1$ statuscode
                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
                        // If the parse or update is not successful return bad
                        // request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         * Request 7.2.5 delete user-context.
         */
        Spark.delete(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // Check if the user context does exist
                        final UserContext userContext = requestHandler
                                .getUserContextById(userContextId);
                        // Make sure only admins and the owner can delete a
                        // context
                        auth.allowAdminAndOwner(req, res, userContext.getImplementationParameters()
                                .getOwner());
                        // Perform delete and return empty string
                        requestHandler.deleteUserContextById(userContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        // if not return corresponding error status.
                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });
        /*
         * Request 7.2.6 for user-context-lists
         */
        
        Spark.get(
                Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    final String url = req.uri().toString();
                    try {
                        auth.allowAdmin(req, res);
                        return UserContextRESTInterface.createReturnStringListRequest(req, res,
                                UserContextList.class, requestHandler.getAllUserContexts(url));
                    } catch (final UnauthorizedException e) {
                        return UserContextRESTInterface.createReturnStringListRequest(req, res,
                                UserContextList.class, requestHandler.getMyContexts(auth
                                        .getAuthenticatedUser(req, res).getId(), url));
                    }
                });
    }
}
