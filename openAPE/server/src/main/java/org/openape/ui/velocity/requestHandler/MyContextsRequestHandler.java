package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.taskcontext.TaskContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class MyContextsRequestHandler {
    public static final MongoCollectionTypes COLLECTIONTOUSE_USERCONTEXTS = MongoCollectionTypes.USERCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_TASKCONTEXTS = MongoCollectionTypes.TASKCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_EQUIPMENTCONTEXTS = MongoCollectionTypes.EQUIPMENTCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_ENVIRONMENTCONTEXTS = MongoCollectionTypes.ENVIRONMENTCONTEXT;

    public ArrayList<String[]> getAllEnvironmentContexts() throws IOException,
            IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(MyContextsRequestHandler.COLLECTIONTOUSE_ENVIRONMENTCONTEXTS);

        final ArrayList<String[]> listEnvironmentContexts = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {
            final String id = entry.getObjectId("_id").toString();
            final String userId = entry.getString("owner").toString();

            final boolean isPublic = entry.getBoolean("public");

            String stringIsPublic = "";

            if (isPublic == false) {
                stringIsPublic = "false";
            } else {
                stringIsPublic = "true";
            }

            final String[] myStringArray = { id, userId, stringIsPublic };
            listEnvironmentContexts.add(myStringArray);
        }
        return listEnvironmentContexts;
    }

    public ArrayList<String[]> getAllEquipmentContexts() throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(MyContextsRequestHandler.COLLECTIONTOUSE_EQUIPMENTCONTEXTS);

        final ArrayList<String[]> listEquipmentContexts = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {
            final String id = entry.getObjectId("_id").toString();
            final String userId = entry.getString("owner").toString();

            final boolean isPublic = entry.getBoolean("public");

            String stringIsPublic = "";

            if (isPublic == false) {
                stringIsPublic = "false";
            } else {
                stringIsPublic = "true";
            }

            final String[] myStringArray = { id, userId, stringIsPublic };
            listEquipmentContexts.add(myStringArray);
        }
        return listEquipmentContexts;
    }

    public List<TaskContext> getAllTaskContexts() throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final List<DatabaseObject> lilstTaskContextDBObject = databaseConnection
                .getDatabaseObjectsByQuery(AdminSectionRequestHandler.COLLECTIONTOUSE_TASKCONTEXTS, null);
        
        final List<TaskContext> listTaskContexts = new ArrayList<TaskContext>();
        
        for(DatabaseObject taskContextDBObject : lilstTaskContextDBObject){
        	listTaskContexts.add((TaskContext) taskContextDBObject);
        }

        return listTaskContexts;
    }

    public ArrayList<String[]> getAllUsercontextsByUserId(final String userId) throws IOException,
            IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(MyContextsRequestHandler.COLLECTIONTOUSE_USERCONTEXTS);

        final ArrayList<String[]> listContext = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {
            final String userid = entry.getString("owner").toString();

            System.out.println(userid.equals(userId));
            if (userid.equals(userId)) {
                final String id = entry.getObjectId("_id").toString();
                final boolean isPublic = entry.getBoolean("public");
                String stringIsPublic = "";
                if (isPublic == false) {
                    stringIsPublic = "false";
                } else {
                    stringIsPublic = "true";
                }

                final String[] myStringArray = { userid, id, stringIsPublic };
                listContext.add(myStringArray);
            }

        }
        return listContext;
    }
}
