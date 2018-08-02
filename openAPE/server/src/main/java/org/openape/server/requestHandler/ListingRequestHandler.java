package org.openape.server.requestHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.Messages;
import org.openape.api.contexts.ContextObject;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.Property;
import org.openape.api.listing.ListingRequest;
import org.openape.api.listing.ListingResponse;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.ListingRESTInterface;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * Class with methods to manage listings on the server. It is used by the rest
 * API {@link ListingRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class ListingRequestHandler {

    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.LISTING;

    /**
     * TODO implement matchmaking
     *
     * @param value
     * @return
     */
    public boolean confirmUserRating(final float value) {
        return false;
    }

    /**
     * Method to store a new listing into the server. It is used by the rest API
     * {@link ListingRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param listing
     *            to be stored.
     * @return the ID of the stored listing.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete environment context.
     */
    public String createListing(final ListingRequest listingrequest) throws IOException, IllegalArgumentException {
BasicDBObject query =    	 createQueryfromRequest(listingrequest);
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        List<DatabaseObject> resourceDescriptionList  = databaseConnection.getDatabaseObjectsByQuery(MongoCollectionTypes.RESOURCEDESCRIPTION , query);
        		List<URI> resourceDescriptionUris = new LinkedList<>();
        for (DatabaseObject o: resourceDescriptionList) {
        	URI uri = null;
			try {
				uri = new URI( Messages.getString("ListingRESTInterface.ListingURLWithoutID") + "/" + o.getId()	);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        resourceDescriptionUris.add(uri);
        }
        		
        		ListingResponse listing = new ListingResponse();
        listing.setResource(resourceDescriptionUris);
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseConnection.storeDatabaseObject(ListingRequestHandler.COLLECTIONTOUSE,
                    (DatabaseObject) listing);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    private BasicDBObject createQueryfromRequest(ListingRequest listingRequest) {
    	
    	BasicDBObject taskQuery = getQuery(listingRequest.getTaskContextUris(), MongoCollectionTypes.TASKCONTEXT ); 
		return taskQuery;
	}

	private BasicDBObject getQuery(List<String> contextUris, MongoCollectionTypes collection) {
		BasicDBList or = new BasicDBList();
		
		for (String uri: contextUris) {
			String id = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
			ContextObject context;
			List<Property> propertyList = new LinkedList<>();
			try {
				context = (ContextObject) DatabaseConnection.getInstance().getDatabaseObjectById(collection, id);
				propertyList = context.getPropertys();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Property property:  propertyList ) {
				or.add(new BasicDBList().put(	property.getName(), 				property.getValue()) );
			}
			
		}
		return  new BasicDBObject("$or", or);
				
		
	}

	/**
     * Method to delete an existing listing from the server. It is used by the
     * rest API {@link ListingRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the listing to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteListingById(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteDatabaseObject(
                ListingRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("ListingRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * Method to get an existing listing from the server. It is used by the rest
     * API {@link ListingRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested environment context.
     * @return requested listing.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public ListingResponse getListingById(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getDatabaseObjectById(
                ListingRequestHandler.COLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(
                    Messages.getString("ListingRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }

        // convert into correct type.
        ListingResponse returnObject;
        try {
            returnObject = (ListingResponse) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;
    }
}
