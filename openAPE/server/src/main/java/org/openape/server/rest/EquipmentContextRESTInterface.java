package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.Messages;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;

import spark.Spark;

public class EquipmentContextRESTInterface extends SuperRestInterface {

    public static void setupEquipmentContextRESTInterface(
            final EquipmentContextRequestHandler requestHandler) {
        /**
         * Request 7.4.2 create equipment-context.
         */
        Spark.post(
                Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        // Try to map the received json object to a
                        // equipmentContext
                        // object.
                        final EquipmentContext recievedEquipmentContext = (EquipmentContext) SuperRestInterface
                                .extractObjectFromRequest(req, EquipmentContext.class);
                        // Test the object for validity.
                        if (!recievedEquipmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("EquipmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, save it and return the id.
                        final String equipmentContextId = requestHandler
                                .createEquipmentContext(recievedEquipmentContext);
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
                        final EquipmentContext equipmentContext = requestHandler
                                .getEquipmentContextById(equipmentContextId);
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
        Spark.put(
                Messages.getString("EquipmentContextRESTInterface.EquipmentContextURLWithID"), //$NON-NLS-1$
                (req, res) -> {
                    final String equipmentContextId = req.params(Messages
                            .getString("EquipmentContextRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        final EquipmentContext recievedEquipmentContext = (EquipmentContext) SuperRestInterface
                                .extractObjectFromRequest(req, EquipmentContext.class);
                        // Test the object for validity.
                        if (!recievedEquipmentContext.isValid()) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return Messages
                                    .getString("EquipmentContextRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
                        }
                        // If the object is okay, update it.
                        requestHandler.updateEquipmentContextById(equipmentContextId,
                                recievedEquipmentContext);
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
