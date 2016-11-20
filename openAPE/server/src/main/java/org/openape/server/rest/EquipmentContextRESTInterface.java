package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.server.EquipmentContextRequestHandler;

import static spark.Spark.*;

public class EquipmentContextRESTInterface extends SuperRestInterface {

    public EquipmentContextRESTInterface(
            final EquipmentContextRequestHandler requestHandler) {
        super();

        /**
         * Request 7.4.2 create equipment-context.
         */
        post("/api/equipment-context", (req, res) -> {
            try {
                // Try to map the received json object to a equipmentContext
                // object.
                EquipmentContext recievedEquipmentContext = (EquipmentContext) extractContentFromRequest(
                        req, EquipmentContext.class);
                // Test the object for validity.
                if (!recievedEquipmentContext.isValid()) {
                    res.status(HTTP_STATUS_BAD_REQUEST);
                    return "";
                }
                // If the object is okay, save it and return the id.
                String equipmentContextId = requestHandler
                        .createEquipmentContext(recievedEquipmentContext);
                res.status(HTTP_STATUS_OK);
                res.type("application/json");
                return equipmentContextId;
            } catch (JsonParseException | JsonMappingException e) {
                // If the parse is not successful return bad request error code.
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

        /**
         * Request 7.4.3 get equipment-context. Used to get a specific equipment context
         * identified by ID.
         */
        get("/api/equipment-context/:equipment-context-id",
                (req, res) -> {
                    String equipmentContextId = req.params(":equipment-context-id");
                    try {
                        // if it is successful return equipment context.
                        EquipmentContext equipmentContext = requestHandler
                                .getEquipmentContextById(equipmentContextId);
                        res.status(HTTP_STATUS_OK);
                        res.type("application/json");
                        return equipmentContext;
                        // if not return corresponding error status.
                    } catch (IllegalArgumentException e) {
                        res.status(HTTP_STATUS_BAD_REQUEST);
                        return "";
                    } catch (IOException e) {
                        res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return "";
                    }

                });

        /**
         * Request 7.4.4 update equipment-context.
         */
        put("/api/equipment-context/:equipment-context-id",
                (req, res) -> {
                    String equipmentContextId = req.params(":equipment-context-id");
                    try {
                        EquipmentContext recievedEquipmentContext = (EquipmentContext) extractContentFromRequest(
                                req, EquipmentContext.class);
                        // Test the object for validity.
                        if (!recievedEquipmentContext.isValid()) {
                            res.status(HTTP_STATUS_BAD_REQUEST);
                            return "";
                        }
                        // If the object is okay, update it.
                        requestHandler.updateEquipmentContextById(equipmentContextId,
                                recievedEquipmentContext);
                        res.status(HTTP_STATUS_OK);
                        return "";
                    } catch (JsonParseException | JsonMappingException
                            | IllegalArgumentException e) {
                        // If the parse or update is not successful return bad
                        // request
                        // error code.
                        res.status(HTTP_STATUS_BAD_REQUEST);
                        return "";
                    } catch (IOException e) {
                        res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return "";
                    }
                });

        /**
         * Request 7.4.5 delete equipment-context.
         */
        delete("/api/equipment-context/:equipment-context-id", (req, res) -> {
            String equipmentContextId = req.params(":equipment-context-id");
            try {
                // if it is successful return equipment context.
                requestHandler.deleteEquipmentContextById(equipmentContextId);
                res.status(HTTP_STATUS_OK);
                return "";
                // if not return corresponding error status.
            } catch (IllegalArgumentException e) {
                res.status(HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

    }

}