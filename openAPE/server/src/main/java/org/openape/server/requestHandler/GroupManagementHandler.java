package org.openape.server.requestHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.group.Group;
import org.openape.api.group.GroupMember;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;


public class GroupManagementHandler {

	
	public static String createGroup(String groupName, String description, String entryRequirements, String ownerId) throws IllegalArgumentException, IOException{
GroupMember admin = new GroupMember(ownerId, true);
List<GroupMember> groupMembers = new LinkedList<GroupMember>();
groupMembers.add(admin);
		Group group = new Group(groupName ,groupMembers );
		
		final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try{
        	id = databaseconnection.storeData(MongoCollectionTypes.GROUPS	 , group);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        
        return id;
	

}
}