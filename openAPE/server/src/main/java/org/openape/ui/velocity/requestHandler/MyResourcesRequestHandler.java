package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class MyResourcesRequestHandler {

    public static final MongoCollectionTypes COLLECTIONTOUSE_RESOURCEDESCRIPTION = MongoCollectionTypes.RESOURCEDESCRIPTION;

    public List<ResourceDescription> getAllResourceDescriptions() throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final List<DatabaseObject> listResourceDBObjects = databaseConnection
                .getDatabaseObjectsByQuery(
                        MyResourcesRequestHandler.COLLECTIONTOUSE_RESOURCEDESCRIPTION, null);

        final List<ResourceDescription> listResources = new ArrayList<>();

        for (final DatabaseObject dboResource : listResourceDBObjects) {
            listResources.add((ResourceDescription) dboResource);
        }

        return listResources;
    }
}
