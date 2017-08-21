package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openape.api.DatabaseObject;
import org.openape.api.Resource;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyResourcesRequestHandler {
	
    public static final MongoCollectionTypes COLLECTIONTOUSE_RESOURCEDESCRIPTION = MongoCollectionTypes.RESOURCEDESCRIPTION;
    
	 public List<ResourceDescription> getAllResourceDescriptions() throws IOException,
     IllegalArgumentException {
		 // get database connection.
		 final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		
		 // Get the requested data.
		 List<DatabaseObject> listResourceDBObjects = databaseConnection
		         .getDatabaseObjectsByQuery(COLLECTIONTOUSE_RESOURCEDESCRIPTION, null);
		 
		 List<ResourceDescription> listResources = new ArrayList<>();
		
		 for(DatabaseObject dboResource: listResourceDBObjects){
			 listResources.add((ResourceDescription) dboResource);
		 }

		return listResources;
	 }
}
