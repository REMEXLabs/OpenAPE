package org.openape.ui.velocity.requestHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.openape.server.rest.UserContextRESTInterface;

import com.mongodb.client.result.UpdateResult;

/**
 * Class with methods to manage user context on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class AdminSectionRequestHandler {
	public static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.USERS;

    public void removeUser(String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        databaseConnection.removeData(
                AdminSectionRequestHandler.COLLECTIONTOUSE, id);
    }
    
    public ArrayList<User> getAllUsers() throws IOException, IllegalArgumentException {
    	
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                AdminSectionRequestHandler.COLLECTIONTOUSE);
        
        User user = new User();
        ArrayList<User> listUsers = new ArrayList<User>();
        for(Document entry : listDocuments){
        	
        	user = new User();
        	user.setEmail(entry.getString("email"));
        	user.setUsername(entry.getString("username"));
        	user.setId(entry.getObjectId("_id").toString());
        	
        	List<String> listRoles = Arrays.asList(entry.get("roles").toString().replace("[", "").replace("]", ""));
        	user.setRoles(listRoles);
        	
        	listUsers.add(user);
        }
        return listUsers;
    }
    
 public UpdateResult updateUser(String id, String indexName, String indexValue) throws Exception {
    	
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        UpdateResult updateResult = null;
	
		updateResult = databaseConnection.updateDocument(
	                AdminSectionRequestHandler.COLLECTIONTOUSE, id, indexName, indexValue); 

       
        return updateResult;
    }
}
