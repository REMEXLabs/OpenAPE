package org.openape.server.requestHandler;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.ContextList;
import org.openape.api.Messages;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.UserContextRESTInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ContextRequestHandler<T, Y extends ContextList> {
	private static Logger logger = LoggerFactory.getLogger(ContextRequestHandler.class);
	MongoCollectionTypes COLLECTIONTOUSE = null;
public ContextRequestHandler(MongoCollectionTypes collectionToUse) {
	this.COLLECTIONTOUSE = collectionToUse;
}
	
    
    /**
	 * 
	 * @param userId
	 * @param url
	 *            - the URL of the server on which the contexts are located,,
	 *            usually the server on which this application is running on
	 * @return
	 * @throws IOException
	 */
	public Y getContextListOfUser(String userId, String url) throws IOException {
	    	
	    	final BasicDBObject query = new BasicDBObject();
	        query.put("implementation-parameters.owner", userId);

		return getContextList(query, url);
	}
	/**Returns all contexts of a certain user
	 * @param userId
	 * the userId of the user
	 * @return
	 * @throws IOException
	 */
	public List<T> getContextsOfUser(String userId) throws IOException {
		final BasicDBObject query = new BasicDBObject();
	    query.put("implementation-parameters.owner", userId);
	
		return getContexts(query);
	}
	/**Returns a list with all context elements that match the query
	 * @param query
	 * selector for the context elements
	 * @return
	 * @throws IOException
	 */
	public List<T> getContexts 
(BasicDBObject query) throws IOException {
	    	final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	logger.info("Requesting "+ COLLECTIONTOUSE);
	        final List<DatabaseObject> result = databaseConnection.getDatabaseObjectsByQuery(
	               COLLECTIONTOUSE, query);
	        final List<T> contexts = new ArrayList<T>();
	        
	        for (final DatabaseObject databaseObject : result) {
	            contexts.add((T) databaseObject);
	        }
	logger.info("found " + contexts.size() + " contexts");
	 return contexts;
	        
	    }
	
	public Y getCompleteContextList(final String url) throws IOException {
	    return getContextList(null, url);
	}
	
	public Y getContextList(final BasicDBObject query, final String url) throws IOException {
		List<T> contexts = getContexts( query);
		Y instance = null;
		try {
				    instance = (Y) ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).newInstance();
		instance.addContexts(contexts);
		} catch (InstantiationException | IllegalAccessException e) {
			
			e.printStackTrace();
		}
		return instance;
	
	}
	
	/**
	 * Method to store a new user context into the server. It is used by the
	 * rest API {@link UserContextRESTInterface} and uses the server database
	 * {@link DatabaseConnection}.
	 *
	 * @param userContext
	 *            to be stored.
	 * @return the ID of the stored user context.
	 * @throws IOException
	 *             if a storage problem still occurs, after to many tries.
	 * @throws IllegalArgumentException
	 *             if the parameter is not a complete user context.
	 */
	public String createContext(final Object userContext) throws IOException, IllegalArgumentException {
	    // get database connection.
	    final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
	    // try to store data. Class cast exceptions will be thrown as illegal
	    // argument exceptions. IO exceptions will just be thrown through.
	    String id = null;
	    try {
	        id = databaseconnection.storeDatabaseObject(this.COLLECTIONTOUSE,
	                (DatabaseObject) userContext);
	        UserContextRequestHandler.logger.debug("New user context in database with id \"" + id
	                + "\".");
	    } catch (final ClassCastException e) {
	        throw new IllegalArgumentException(e.getMessage());
	    }
	    return id;
	}
	/**
	 * Method to delete an existing user context from the server. It is used by
	 * the rest API {@link UserContextRESTInterface} and uses the server
	 * database {@link DatabaseConnection}.
	 *
	 * @param id
	 *            the ID of the user context to delete.
	 * @return true if successful. Else an exception is thrown.
	 * @throws IOException
	 *             if a storage problem still occurs, after to many tries.
	 * @throws IllegalArgumentException
	 *             if the id is no valid id or not assigned.
	 */
	public boolean deleteContextById(final String id) throws IOException, IllegalArgumentException {
	    // get database connection.
	    final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	
	    final boolean success = databaseConnection.deleteDatabaseObject(
	            this.COLLECTIONTOUSE, id);
	    if (!success) {
	        throw new IllegalArgumentException(
	                Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
	    }
	    return true;
	}
	
	/**
	 * Method to get an existing user context from the server. It is used by the
	 * rest API {@link UserContextRESTInterface} and uses the server database
	 * {@link DatabaseConnection}.
	 *
	 * @param id
	 *            the ID of the requested user context.
	 * @return requested user context.
	 * @throws IOException
	 *             if a storage problem still occurs, after to many tries.
	 * @throws IllegalArgumentException
	 *             if the id is no valid id or not assigned.
	 */
	public T getContextById(final String id) throws IOException, IllegalArgumentException {
	    // get database connection.
	    final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	
	    // Get the requested data.
	    final DatabaseObject result = databaseConnection.getDatabaseObjectById(
	            this.COLLECTIONTOUSE, id);
	
	    // If the result is null the id is not found.
	    if (result == null) {
	        throw new IllegalArgumentException(
	                Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
	    }
	
	    // convert into correct type.
	    T returnObject;
	    try {
	        returnObject = (T) result;
	    } catch (final ClassCastException e) {
	        e.printStackTrace();
	        throw new IOException(e.getMessage());
	    }
	    return returnObject;
	
	}
	
	public Y getPublicContextList(String url) throws IOException {
	    final BasicDBObject query = new BasicDBObject();
	    query.put("implementation-parameters.public", true);
	    return getContextList(query, url);
	}
	/**
	 * Method to update an existing user context on the server. It is used by
	 * the rest API {@link UserContextRESTInterface} and uses the server
	 * database {@link DatabaseConnection}.
	 *
	 * @param id
	 *            the ID of the user context to update.
	 * @param userContext
	 *            new version of the user context.
	 * @return true if successful. Else an exception is thrown.
	 * @throws IOException
	 *             if a storage problem still occurs, after to many tries.
	 * @throws IllegalArgumentException
	 *             if the id is no valid id, not assigned or the user context is
	 *             not valid.
	 */
	public boolean updateContextById(final String id, final Object userContext) throws IOException, IllegalArgumentException {
	    // get database connection.
	    final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	
	    // Update data. If a class cast exception occurs or the return value is
	    // false the parameters is not valid and an illegal argument exception
	    // is thrown. IO exceptions are thrown through.
	    boolean success;
	    try {
	        success = databaseConnection.updateDatabaseObject(
	                this.COLLECTIONTOUSE, (DatabaseObject) userContext, id);
	    } catch (final ClassCastException e) {
	        throw new IllegalArgumentException(e.getMessage());
	    }
	    if (!success) {
	        throw new IllegalArgumentException(
	                Messages.getString("UserContextRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
	    }
	    return true;
	}

public List<T> getPublicContexts() throws IOException {
	logger.info("lusm: getPublicContexts");
	final BasicDBObject query = new BasicDBObject();
    query.put("PUBLIC", "public");
    return getContexts(query);
}


public Y getOverAllContextListOfUser(String userId,String[][] filters, String url) throws IOException{
	final BasicDBObject queryPublic = new BasicDBObject();
    queryPublic.put("PUBLIC", "public");
	final BasicDBObject queryOwner = new BasicDBObject();
    queryOwner.put("implementation-parameters.owner", userId);
    BasicDBList or = new BasicDBList();
    or.add(queryPublic);
    or.add(queryOwner);
    BasicDBObject query = new BasicDBObject("$or", or);
    for (String[] s: filters) {
    	query.put(s[0],s[1]);
    }
	return getContextList(query,url);
}


}
