package org.openape.ui.velocity.requestHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.bson.Document;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import com.mongodb.client.result.UpdateResult;

public class MyContextsRequestHandler {
	public static final MongoCollectionTypes COLLECTIONTOUSE_USERCONTEXTS = MongoCollectionTypes.USERCONTEXT;
	public static final MongoCollectionTypes COLLECTIONTOUSE_TASKCONTEXTS = MongoCollectionTypes.TASKCONTEXT;
	public static final MongoCollectionTypes COLLECTIONTOUSE_EQUIPMENTCONTEXTS = MongoCollectionTypes.EQUIPMENTCONTEXT;
	public static final MongoCollectionTypes COLLECTIONTOUSE_ENVIRONMENTCONTEXTS = MongoCollectionTypes.ENVIRONMENTCONTEXT;

   
    public ArrayList<String[]> getAllUsercontextsByUserId(String userId) throws IOException, IllegalArgumentException {
    	
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                MyContextsRequestHandler.COLLECTIONTOUSE_USERCONTEXTS);
        
        ArrayList<String[]> listContext = new ArrayList<String[]>();
        
        for(Document entry : listDocuments){        	
        	String userid = entry.getString("owner").toString();
        	
        	System.out.println(userid.equals(userId));
        	if(userid.equals(userId)){
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
        	
        }
        return listContext;
    }
    
    public ArrayList<String[]> getAllTaskContexts() throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                MyContextsRequestHandler.COLLECTIONTOUSE_TASKCONTEXTS);
        
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
                MyContextsRequestHandler.COLLECTIONTOUSE_EQUIPMENTCONTEXTS);
        
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
        }
        return listEquipmentContexts;
    }
    
    public ArrayList<String[]> getAllEnvironmentContexts() throws IOException, IllegalArgumentException {
    	
        // get database connection.
         final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection.getAllDocuments(
                MyContextsRequestHandler.COLLECTIONTOUSE_ENVIRONMENTCONTEXTS);
        
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
        }
        return listEnvironmentContexts;
    }
}
