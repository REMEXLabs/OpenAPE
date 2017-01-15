package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.server.requestHandler.ResourceRequestHandler;

import spark.Spark;

public class ResourceRESTInterface extends SuperRestInterface {
    public ResourceRESTInterface(final ResourceRequestHandler requestHandler) {
        super();

        /**
         * Request 7.6.2 create resource.
         */
        Spark.post("/api/resource", (req, res) -> {
            try {
                // try to extract the received object.
                ObjectMapper mapper = new ObjectMapper();
                Object recievedObject = mapper.readValue(req.body(), Object.class);
                // If the object is okay, save it and return the id.
                String resourceId = requestHandler.createResource(recievedObject);
                res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                res.type("application/json");
                return resourceId;
            } catch (JsonParseException | JsonMappingException e) {
                // If the parse is not successful return bad request
                // error code.
                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                return "";
            } catch (IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

        /**
         * Request 7.6.3 get resource by ID. Used to get a specific resource
         * identified by ID.
         */
        Spark.get("/api/resource/resource-id", (req, res) -> {
            String resourceId = req.params(":resource-id");
            try {
                // if it is successful return user context.
                Object resource = requestHandler.getResourceById(resourceId);
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                res.type("application/json");
                return resource;
                // if not return corresponding error status.
            } catch (IllegalArgumentException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return "";
            } catch (IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }

        });

        /**
         * Request 7.6.4 get resource from listing.
         */
        Spark.get("/api/resource?listing-id=listing-id&index=index", (req, res) -> {
            return "";
            // String userContextId = req.params(Messages
            //                            .getString("UserContextRESTInterface.IDParam")); //$NON-NLS-1$
            // try {
            // UserContext recievedUserContext = (UserContext) this
            // .extractContentFromRequest(req, UserContext.class);
            // // Test the object for validity.
            // if (!recievedUserContext.isValid()) {
            // res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
            //                            return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
            // }
            // // If the object is okay, update it.
            // requestHandler.updateUserContextById(userContextId,
            // recievedUserContext);
            // res.status(SuperRestInterface.HTTP_STATUS_OK);
            //                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
            // } catch (JsonParseException | JsonMappingException |
            // IllegalArgumentException e) {
            // // If the parse or update is not successful return bad
            // // request
            // // error code.
            // res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
            //                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
            // } catch (IOException e) {
            // res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
            //                        return Messages.getString("UserContextRESTInterface.EmptyString"); //$NON-NLS-1$
            // }
            });

        /**
         * Request 7.6.5 delete resource.
         */
        Spark.delete("/api/resource/resource-id", (req, res) -> {
            String resourceId = req.params(":resource-id");
            try {
                // if it is successful return user context.
                requestHandler.deleteResourceById(resourceId);
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                return "";
                // if not return corresponding error status.
            } catch (IllegalArgumentException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return "";
            } catch (IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return "";
            }
        });

    }

}
