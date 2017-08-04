package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;


public class GroupManagementHandler {


	public static String createGroup(String groupName, String description, String entryRequirements, String ownerId)
			throws IllegalArgumentException, IOException {
		org.openape.server.api.group.GroupMember admin = new org.openape.server.api.group.GroupMember(ownerId, true);
		List<org.openape.server.api.group.GroupMember> groupMembers = new LinkedList<org.openape.server.api.group.GroupMember>();
		groupMembers.add(admin);
		org.openape.server.api.group.Group group = new org.openape.server.api.group.Group(groupName, groupMembers);

		final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
		String id;
		try {
			id = databaseconnection.storeData(MongoCollectionTypes.GROUPS, group);
		} catch (final ClassCastException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		return id;
	}
}