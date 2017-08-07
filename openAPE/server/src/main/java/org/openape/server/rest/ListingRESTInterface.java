package org.openape.server.rest;

import java.io.IOException;

import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.server.requestHandler.ListingRequestHandler;

import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListingRESTInterface extends SuperRestInterface {

    public static void setupListingRESTInterface(final ListingRequestHandler requestHandler) {
        /**
         * Request 7.8.2 create listing.
         */
        Spark.post(Messages.getString("ListingRESTInterface.ListingURLWithoutID"), (req, res) -> { //$NON-NLS-1$
                    try {
                        if (!req.contentType().equals(Messages.getString("MimeTypeJson"))) {//$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("Contexts.WrongMimeTypeErrorMsg");//$NON-NLS-1$
                }
                // Try to map the received json object to a
                // environmentContext
                // object.
                final Listing recievedListing = (Listing) SuperRestInterface
                        .extractObjectFromRequest(req, Listing.class);
                // Test the object for validity.
                if (!recievedListing.isValid()) {
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return Messages.getString("ListingRESTInterface.NoValidObjectErrorMassage"); //$NON-NLS-1$
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
        Spark.get(
                Messages.getString("ListingRESTInterface.ListingURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String environmentContextId = req.params(Messages
                            .getString("ListingRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return listing.
                        final Listing listing = requestHandler.getListingById(environmentContextId);
                        res.status(SuperRestInterface.HTTP_STATUS_OK);
                        res.type(Messages.getString("ListingRESTInterface.JsonMimeType")); //$NON-NLS-1$
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
        Spark.delete(
                Messages.getString("ListingRESTInterface.ListingURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String listingId = req.params(Messages
                            .getString("ListingRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        // if it is successful return empty string.
                        requestHandler.deleteListingById(listingId);
                        res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                        return Messages.getString("ListingRESTInterface.EmptyString"); //$NON-NLS-1$
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
