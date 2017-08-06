package org.openape.server.rest;

import java.io.IOException;

import org.openape.api.Messages;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;

import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EquipmentContextRESTInterface extends SuperRestInterface {

    public static void setupEquipmentContextRESTInterface(
            final EquipmentContextRequestHandler requestHandler, final AuthService auth) {

        // Authentication: Make sure only registered principals (users and admins) can create a new context
        Spark.before(Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithoutID"), auth.authorize("user"));
        // Authentication: Everyone can access the route for a specific context
        Spark.before(Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithID"), auth.authorize("anonymous"));

        /**
         * Request 7.4.2 create equipment-context.
         */
        Spark.post(
                Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                    }
                    try {
                        // Try to map the received json object to an EquipmentContext object.
                        final EquipmentContext receivedEquipmentContext = (EquipmentContext) SuperRestInterface
                                .extractObjectFromRequest(req, EquipmentContext.class);
                        // Make sure to set the id of the authenticated user as the ownerId
                        receivedEquipmentContext.setOwner(auth.getAuthenticatedUser(req, res).getId());
                        // Test the object for validity.
                        if (!receivedEquipmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("EquipmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String equipmentContextId = requestHandler
                                .createEquipmentContext(receivedEquipmentContext);
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        return equipmentContextId;
                    } catch (JsonParseException | JsonMappingException e) {
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
         * Request 7.4.3 get equipment-context. Used to get a specific equipment
         * context identified by ID.
         */
        Spark.get(
                Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String equipmentContextId = req.params(Messages
                            .getString("EquipmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return equipment context.
                        final EquipmentContext equipmentContext = requestHandler.getEquipmentContextById(equipmentContextId);
                        // Make sure only admins or the owner can view the context, except if it is public
                        auth.allowAdminOwnerAndPublic(req, res, equipmentContext.getOwner(), equipmentContext.isPublic());
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("EquipmentContextRESTInterface.JsonMimeType")); //$NON-NLS-1$
                        final ObjectMapper mapper = new ObjectMapper();
                        final String jsonData = mapper.writeValueAsString(equipmentContext);
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
         * Request 7.4.4 update equipment-context.
         */
        Spark.put(Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                }
                final String equipmentContextId = req.params(Messages
                        .getString("EquipmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                try {
                    final EquipmentContext receivedEquipmentContext = (EquipmentContext) SuperRestInterface
                            .extractObjectFromRequest(req, EquipmentContext.class);
                    // Test the object for validity.
                    if (!receivedEquipmentContext.isValid()) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages
                                .getString("EquipmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                    }
                    // Check if the user context does exist
                    final EquipmentContext equipmentContext = requestHandler.getEquipmentContextById(equipmentContextId);
                    // Make sure only admins and the owner can update a context
                    auth.allowAdminAndOwner(req, res, equipmentContext.getOwner());
                    receivedEquipmentContext.setOwner(equipmentContext.getOwner()); // Make sure the owner can't be changed
                    // Perform update
                    requestHandler.updateEquipmentContextById(equipmentContextId,
                            receivedEquipmentContext);
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    return Messages.getString("EquipmentContextRESTInterface.EmptyString"); //$NON-NLS-1$ //TODO return right statuscode
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
         * Request 7.4.5 delete equipment-context.
         */
        Spark.delete(
                Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String equipmentContextId = req.params(Messages
                            .getString("EquipmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // Check if the user context does exist
                        final EquipmentContext equipmentContext = requestHandler.getEquipmentContextById(equipmentContextId);
                        // Make sure only admins and the owner can delete a context
                        auth.allowAdminAndOwner(req, res, equipmentContext.getOwner());
                        // if it is successful return empty string.
                        requestHandler.deleteEquipmentContextById(equipmentContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("EquipmentContextRESTInterface.EmptyString"); //$NON-NLS-1$
                        // if not return corresponding error status.
                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

    }

}
