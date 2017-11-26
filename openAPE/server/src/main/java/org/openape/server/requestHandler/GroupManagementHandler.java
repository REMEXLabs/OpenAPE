package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.databaseObjectBase.ImplementationParameters;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.server.api.group.Group;
import org.openape.server.api.group.GroupMember;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class GroupManagementHandler {

    public static void addMember(final String userId, final GroupMembershipStatus status,
            final Group group) throws IOException {
        // final List<GroupMember> members = new LinkedList<GroupMember>();
        // members.add(new GroupMember(userId, status));
        // group.setMembers(members);
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try {
            databaseConnection.updateDatabaseObject(MongoCollectionTypes.GROUPS, group,
                    group.getId());
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    public static String createGroup(final String groupName, final String description,
            final String entryRequirements, final String ownerId, final boolean openAcces)
            throws IllegalArgumentException, IOException {
        final GroupMember admin = new GroupMember(ownerId,
                GroupMembershipStatus.ADMIN);
        final List<GroupMember> groupMembers = new LinkedList<GroupMember>();
        groupMembers.add(admin);
        ImplementationParameters implementationParameters = new ImplementationParameters();
        implementationParameters.setOwner(ownerId);
        implementationParameters.setPublic(openAcces);
        final Group group = new Group(
                groupName, description, groupMembers, openAcces, implementationParameters);
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            id = databaseconnection.storeDatabaseObject(MongoCollectionTypes.GROUPS, group);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return id;
    }

    public static boolean updateGroupById(final String id, final Group group) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateDatabaseObject(MongoCollectionTypes.GROUPS, group,
                    id);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!success) {
            throw new IllegalArgumentException("No Group with that Id"); //$NON-NLS-1$
        }
        return true;
    }

    public boolean deleteGroupById(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteDatabaseObject(
                MongoCollectionTypes.GROUPS, id);
        if (!success) {
            throw new IllegalArgumentException("No Group with that id"); //$NON-NLS-1$
        }
        return true;
    }

    public List<Group> getAllGroups() throws IOException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        final List<DatabaseObject> result = databaseConnection.getDatabaseObjectsByQuery(
                MongoCollectionTypes.GROUPS, null);
        final List<Group> returnValue = new ArrayList<Group>();
        for (final DatabaseObject databaseObject : result) {
            try {
                returnValue.add((Group) databaseObject);
            } catch (final ClassCastException e) {
                throw new IOException(e.getMessage());
            }
        }
        return returnValue;

    }

    public Group getGroupById(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getDatabaseObjectById(
                MongoCollectionTypes.GROUPS, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(" No Group with that id found"); //$NON-NLS-1$
        }

        // convert into correct type.
        Group returnObject;
        try {
            returnObject = (Group) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;
    }
}