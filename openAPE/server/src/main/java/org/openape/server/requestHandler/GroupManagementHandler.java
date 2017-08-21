package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.Messages;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.server.api.group.Group;
import org.openape.server.api.group.GroupMember;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class GroupManagementHandler {

    public static void addMember(final String userId, final GroupMembershipStatus status,
            final Group group) throws IOException {
        // TODO Auto-generated method stub
        final List<GroupMember> members = new LinkedList<GroupMember>();
        members.add(new GroupMember(userId, status));
        group.setMembers(members);
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try {
            databaseConnection.updateDatabaseObject(MongoCollectionTypes.GROUPS, group, group.getId());
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    public static String createGroup(final String groupName, final String description,
            final String entryRequirements, final String ownerId) throws IllegalArgumentException,
            IOException {
        final org.openape.server.api.group.GroupMember admin = new org.openape.server.api.group.GroupMember(
                ownerId, GroupMembershipStatus.ADMIN);
        final List<org.openape.server.api.group.GroupMember> groupMembers = new LinkedList<org.openape.server.api.group.GroupMember>();
        groupMembers.add(admin);
        final org.openape.server.api.group.Group group = new org.openape.server.api.group.Group(
                groupName, description, groupMembers);

        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            id = databaseconnection.storeDatabaseObject(MongoCollectionTypes.GROUPS, group);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return id;
    }
    
    
    public boolean deleteGroupById(final String id) throws IOException,
		    IllegalArgumentException {
		// get database connection.
		final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		
		final boolean success = databaseConnection.deleteDatabaseObject(
				MongoCollectionTypes.GROUPS, id);
		if (!success) {
		    throw new IllegalArgumentException("No Group with that id"); //$NON-NLS-1$
		}
		return true;
	}
}