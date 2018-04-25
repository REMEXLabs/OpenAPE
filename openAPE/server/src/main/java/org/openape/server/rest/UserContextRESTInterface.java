package org.openape.server.rest;

import java.io.IOException;
import javax.ws.rs.core.MediaType;
import org.openape.api.Messages;
import org.openape.api.UserContextList;
import org.openape.api.usercontext.UserContext;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.requestHandler.ContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;

import spark.Request;
import spark.Spark;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UserContextRESTInterface extends ContextRestInterface {

    private static UserContext createRequestObject(final Request req)
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

    public static void setupUserContextRESTInterface(
            final UserContextRequestHandler requestHandler, final AuthService auth) {

        // Authentication: Make sure only registered principals (users and //
        // admins) can create a new context, GET requests are possible for
        // everyone
        // Spark.before(Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"),
        // (req,res) -> { logger.info("method" + req.requestMethod()); if
        // (!req.requestMethod().equals("GET")){ return auth.authorize("user");}
        // else {return auth.authorize("anonymous");}});
        Spark.before(Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"),
                auth.authorize("anonymous"));

        // Authentication: Everyone can access the route for a specific ID
        Spark.before(Messages.getString("UserContextRESTInterface.UserContextURLWithID"),
                auth.authorize("anonymous"));
        /**
         * * Request 7.2.2 create user-context. Can only be accessed by roles *
         * "user" and "admin.
         */
        Spark.post(
                Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$

                    try {
                        // only admins and users are allowed to create new
                        // contexts
                        auth.allowAdminAndUser(req, res);
                        // Try to map the received json object to a userContext object
                        
                        final UserContext receivedUserContext = UserContextRESTInterface
                                .createRequestObject(req);
                        // Make sure to set the id of the authenticated user as
                        // the ownerID
                        final String id = auth.getAuthenticatedUser(req, res).getId();

                        receivedUserContext.getImplementationParameters().setOwner(id);
                        SuperRestInterface.logger.debug("Lusm: success");

                        // Test the object for validity.
                        if (!receivedUserContext.isValid()) {
                            SuperRestInterface.logger.debug("lusm: none valide"); //$NON-NLS-1$
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("UserContextRESTInterface.NoValidObjectErrorMassage");
                        }

                        // If the object is okay, save it and return the id.
                        final String userContextId = requestHandler
                                .createContext(receivedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        return userContextId;
                    } catch (UnauthorizedException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                        return "You are not an admin or user";
                    }

                    catch (final IllegalArgumentException e) {
                        // If the parse is not successful return bad request //
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    }

                    catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         * * Request 7.2.3 get user-context. Used to get a specific user context
         * * identified by ID.
         */
        Spark.get(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> //$NON-NLS-1$

                {
                    final String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try { // if it is successful return user context.
                        final UserContext userContext = requestHandler
                                .getContextById(userContextId); // Make sure
                                                                    // only
                                                                    // admins or
                                                                    // the owner
                                                                    // can
                                                                    // access
                                                                    // the
                                                                    // requested
                                                                    // context,
                                                                    // except if
                                                                    // it
                                                                    // ispublic

                        auth.allowAdminOwnerAndPublic(req, res, userContext
                                .getImplementationParameters().getOwner(), userContext
                                .getImplementationParameters().isPublic());
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("UserContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        final String jsonData = UserContextRESTInterface.createReturnString(req,
                                userContext);
                        return jsonData; // if not return corresponding error
                                         // status.

                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /** * Request 7.2.4 update user-context. */
        Spark.put(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"),
                (req, res) -> {
                    final String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$

                    try {
                        final UserContext receivedUserContext = UserContextRESTInterface
                                .createRequestObject(req);
                        // Test the object for validity.
                        if (!receivedUserContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("UserContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }

                        // Check if the user context does exist
                        final UserContext userContext = requestHandler
                                .getContextById(userContextId); // Make sure
                                                                    // only
                                                                    // admins
                                                                    // and the
                                                                    // owner can
                                                                    // update a
                                                                    // //
                                                                    // context
                        auth.allowAdminAndOwner(req, res, userContext.getImplementationParameters()
                                .getOwner());
                        receivedUserContext.getImplementationParameters().setOwner(
                                userContext.getImplementationParameters().getOwner()); // Make
                                                                                       // //
                                                                                       // sure
                                                                                       // //
                                                                                       // the
                                                                                       // //
                                                                                       // owner
                                                                                       // //
                                                                                       // can't
                                                                                       // //
                                                                                       // be
                                                                                       // //
                                                                                       // changed
                                                                                       // //
                                                                                       // Perform
                                                                                       // the
                                                                                       // update
                        requestHandler.updateContextById(userContextId, receivedUserContext);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
                    } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) { // If
                                                                                                       // the
                                                                                                       // parse
                                                                                                       // or
                                                                                                       // update
                                                                                                       // is
                                                                                                       // not
                                                                                                       // successful
                                                                                                       // return
                                                                                                       // bad
                                                                                                       // //
                                                                                                       // request
                                                                                                       // //
                                                                                                       // error
                                                                                                       // code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /** * Request 7.2.5 delete user-context. */
        Spark.delete(
                Messages.getString("UserContextRESTInterface.UserContextURLWithID"), (req, res) -> //$NON-NLS-1$
                { // $NON-NLS-1$

                    String userContextId = req.params(Messages
                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try { // Check if the user context does exist

                        UserContext userContext = requestHandler.getContextById(userContextId); // Make
                                                                                                    // sure
                                                                                                    // only
                                                                                                    // admins
                                                                                                    // and
                                                                                                    // the
                                                                                                    // owner
                                                                                                    // can
                                                                                                    // delete
                                                                                                    // a
                                                                                                    // //
                                                                                                    // context
                        auth.allowAdminAndOwner(req, res, userContext.getImplementationParameters()
                                .getOwner()); // Perform
                                              // delete
                                              // and
                                              // return
                                              // empty
                                              // string
                        requestHandler.deleteContextById(userContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$ // if not return
                                                                                           // corresponding
                                                                                           // error
                                                                                           // status.
                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                }); /*
                     * 
                     * 
                     * /* Request 7.2.6 for user - context - lists
                     */

        createContextListRestEndpoint(
                Messages.getString("UserContextRESTInterface.UserContextURLWithoutID"),
                requestHandler, auth, UserContextList.class);
    }
}