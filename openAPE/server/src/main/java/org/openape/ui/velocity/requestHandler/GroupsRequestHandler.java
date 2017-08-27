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
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.user.User;
import org.openape.server.api.group.Group;
import org.openape.server.api.group.GroupAccessRight;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupsRequestHandler {
	
    public static final MongoCollectionTypes COLLECTIONTOUSE_GROUPS = MongoCollectionTypes.GROUPS;
    
	 public List<Group> getAllGroups() throws IOException,
     IllegalArgumentException {
		 // get database connection.
		 final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		
		 // Get the requested data.
		 List<DatabaseObject> listGroupDBObjects = databaseConnection
		         .getDatabaseObjectsByQuery(COLLECTIONTOUSE_GROUPS, null);
		 
		 List<Group> listGroups = new ArrayList<>();
		
		 for(DatabaseObject dboResource: listGroupDBObjects){
			 listGroups.add((Group) dboResource);
		 }

		return listGroups;
	 }
}
