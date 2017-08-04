package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.group.Group;
import org.openape.api.group.GroupMember;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class GroupManagementHandler {

	public static String createGroup(final String groupName, final String description, final String entryRequirements,
			final String ownerId) throws IllegalArgumentException, IOException {
		final GroupMember admin = new GroupMember(ownerId, true);
		final List<GroupMember> groupMembers = new LinkedList<GroupMember>();
		groupMembers.add(admin);
		final Group group = new Group(groupName, groupMembers);

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