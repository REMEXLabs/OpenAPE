package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.server.api.group.Group;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class GroupsRequestHandler {

    public static final MongoCollectionTypes COLLECTIONTOUSE_GROUPS = MongoCollectionTypes.GROUPS;

    public List<Group> getAllGroups() throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final List<DatabaseObject> listGroupDBObjects = databaseConnection
                .getDatabaseObjectsByQuery(GroupsRequestHandler.COLLECTIONTOUSE_GROUPS, null);

        final List<Group> listGroups = new ArrayList<>();

        for (final DatabaseObject dboResource : listGroupDBObjects) {
            listGroups.add((Group) dboResource);
        }

        return listGroups;
    }
}
