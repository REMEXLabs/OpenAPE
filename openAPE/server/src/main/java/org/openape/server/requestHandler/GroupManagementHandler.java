package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.groups.GroupMembershipStatus;
import org.openape.server.api.group.Group;
import org.openape.server.api.group.GroupMember;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class GroupManagementHandler {

    public static String createGroup(final String groupName, final String description,
            final String entryRequirements, final String ownerId) throws IllegalArgumentException,
            IOException {
        final org.openape.server.api.group.GroupMember admin = new org.openape.server.api.group.GroupMember(
                ownerId, GroupMembershipStatus.ADMIN);
        final List<org.openape.server.api.group.GroupMember> groupMembers = new LinkedList<org.openape.server.api.group.GroupMember>();
        groupMembers.add(admin);
        final org.openape.server.api.group.Group group = new org.openape.server.api.group.Group(
                groupName, groupMembers);

        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            id = databaseconnection.storeData(MongoCollectionTypes.GROUPS, group);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return id;
    }

	public static void addMember(String userId, GroupMembershipStatus status, Group group) {
		// TODO Auto-generated method stub
        List<GroupMember> members = new LinkedList<GroupMember>();
        members.add(new GroupMember(userId, status));
        group.setMembers(members);
DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
databaseConnection.updateData(MongoCollectionTypes.GROUPS , group, group.getId() );

	}
}