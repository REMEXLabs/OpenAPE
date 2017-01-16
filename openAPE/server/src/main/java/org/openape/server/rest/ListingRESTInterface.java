package org.openape.server.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.listing.Listing;
import org.openape.server.requestHandler.ListingRequestHandler;

import spark.Spark;

public class ListingRESTInterface extends SuperRestInterface {

    public ListingRESTInterface(final ListingRequestHandler requestHandler) {
        super();

        /**
         * Request 7.8.2 create listing.
         */
        Spark.post("/api/listing", (req, res) -> {
            try {
                // Try to map the received json object to a
                // environmentContext
                // object.
                final Listing recievedListing = (Listing) this.extractContentFromRequest(req,
                        Listing.class);
                // Test the object for validity.
                if (!recievedListing.isValid()) {
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return "No valid context object";
                }
                // If the object is okay, save it and return the id.
                final String listingId = requestHandler.createListing(recievedListing);
                res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                return listingId;
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
         * Request 7.8.3 get listing. Used to get a specific listing identified
         * by ID.
         */
        Spark.get("/api/listing/listing-id", (req, res) -> {
            final String environmentContextId = req.params(":listing-id");
            try {
                // if it is successful return listing.
                final Listing listing = requestHandler.getListingById(environmentContextId);
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                res.type("application/json");
                final ObjectMapper mapper = new ObjectMapper();
                final String jsonData = mapper.writeValueAsString(listing);
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
         * Request 7.8.4 delete listing.
         */
        Spark.delete("/api/listing/listing-id", (req, res) -> {
            final String listingId = req.params(":listing-id");
            try {
                // if it is successful return empty string.
                requestHandler.deleteListingById(listingId);
                res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                return "";
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
