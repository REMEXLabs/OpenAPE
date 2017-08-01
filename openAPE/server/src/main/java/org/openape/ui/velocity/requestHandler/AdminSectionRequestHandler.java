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
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * Class with methods to manage user context on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class AdminSectionRequestHandler {
	public static final MongoCollectionTypes COLLECTIONTOUSE_USERS = MongoCollectionTypes.USERS;
	public static final MongoCollectionTypes COLLECTIONTOUSE_USERCONTEXTS = MongoCollectionTypes.USERCONTEXT;
	public static final MongoCollectionTypes COLLECTIONTOUSE_TASKCONTEXTS = MongoCollectionTypes.TASKCONTEXT;
	public static final MongoCollectionTypes COLLECTIONTOUSE_EQUIPMENTCONTEXTS = MongoCollectionTypes.EQUIPMENTCONTEXT;
	public static final MongoCollectionTypes COLLECTIONTOUSE_ENVIRONMENTCONTEXTS = MongoCollectionTypes.ENVIRONMENTCONTEXT;

    public void removeUser(String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        databaseConnection.removeData(
                AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, id);
    }
    
    public ArrayList<User> getAllUsers() throws IOException, IllegalArgumentException {
    	
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                AdminSectionRequestHandler.COLLECTIONTOUSE_USERS);
        
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
    
    public ArrayList<String[]> getAllUsercontexts() throws IOException, IllegalArgumentException {
    	
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                AdminSectionRequestHandler.COLLECTIONTOUSE_USERCONTEXTS);
        
        ArrayList<String[]> listContext = new ArrayList<String[]>();
        
        for(Document entry : listDocuments){
        	
        	
        	Document documentContext = (Document) entry.get("contexts");
        	Document documentDefault = (Document) documentContext.get("default");
        	
        	String userid = entry.getString("owner").toString();
        	String id = entry.getObjectId("_id").toString();
        	boolean isPublic = entry.getBoolean("public");
        	String stringIsPublic = "";
        	
        	if(isPublic == false){
        		stringIsPublic = "false";
        	} else {
        		stringIsPublic = "true";
        	}
        	
        	String[] myStringArray = {userid,id,stringIsPublic};
        	listContext.add(myStringArray);
        }
        
        return listContext;
    }
    
    public ArrayList<String[]> getAllTaskContexts() throws IOException, IllegalArgumentException {
    	
        // get database connection.
         final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                AdminSectionRequestHandler.COLLECTIONTOUSE_TASKCONTEXTS);
        
        ArrayList<String[]> listTaskContexts = new ArrayList<String[]>();
        
        for(Document entry : listDocuments){
        	String id = entry.getObjectId("_id").toString();
        	String userId = entry.getString("owner").toString();

        	boolean isPublic = entry.getBoolean("public");
        	
        	String stringIsPublic = "";
        	
        	if(isPublic == false){
        		stringIsPublic = "false";
        	} else {
        		stringIsPublic = "true";
        	}
        	
        	String[] myStringArray = {id, userId, stringIsPublic};
        	listTaskContexts.add(myStringArray);
        }
        return listTaskContexts;
    }
    

    public ArrayList<String[]> getAllEquipmentContexts() throws IOException, IllegalArgumentException {
    	
        // get database connection.
         final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                AdminSectionRequestHandler.COLLECTIONTOUSE_EQUIPMENTCONTEXTS);
        
        ArrayList<String[]> listEquipmentContexts = new ArrayList<String[]>();
        
        for(Document entry : listDocuments){
        	String id = entry.getObjectId("_id").toString();
        	String userId = entry.getString("owner").toString();

        	boolean isPublic = entry.getBoolean("public");
        	
        	String stringIsPublic = "";
        	
        	if(isPublic == false){
        		stringIsPublic = "false";
        	} else {
        		stringIsPublic = "true";
        	}
        	
        	String[] myStringArray = {id, userId, stringIsPublic};
        	listEquipmentContexts.add(myStringArray);
        	System.out.println(myStringArray);
        }
        return listEquipmentContexts;
    }
    
    public ArrayList<String[]> getAllEnvironmentContexts() throws IOException, IllegalArgumentException {
    	
        // get database connection.
         final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                AdminSectionRequestHandler.COLLECTIONTOUSE_ENVIRONMENTCONTEXTS);
        
        ArrayList<String[]> listEnvironmentContexts = new ArrayList<String[]>();
        
        for(Document entry : listDocuments){
        	String id = entry.getObjectId("_id").toString();
        	String userId = entry.getString("owner").toString();

        	boolean isPublic = entry.getBoolean("public");
        	
        	String stringIsPublic = "";
        	
        	if(isPublic == false){
        		stringIsPublic = "false";
        	} else {
        		stringIsPublic = "true";
        	}
        	
        	String[] myStringArray = {id, userId, stringIsPublic};
        	listEnvironmentContexts.add(myStringArray);
        	System.out.println(myStringArray);
        }
        return listEnvironmentContexts;
    }
    
    
 public UpdateResult updateUser(String id, String indexName, String indexValue) throws Exception {
    	
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        UpdateResult updateResult = null;
	
		updateResult = databaseConnection.updateDocument(
	                AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, id, indexName, indexValue); 
		
        return updateResult;
    }
}
