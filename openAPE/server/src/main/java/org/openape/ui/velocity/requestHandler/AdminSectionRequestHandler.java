package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.UserContextRESTInterface;

import com.mongodb.client.result.UpdateResult;

/**
 * Class with methods to manage user context on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class AdminSectionRequestHandler {
    public static final MongoCollectionTypes COLLECTIONTOUSE_USERS = MongoCollectionTypes.USERS;
    public static final MongoCollectionTypes COLLECTIONTOUSE_USERCONTEXTS = MongoCollectionTypes.USERCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_TASKCONTEXTS = MongoCollectionTypes.TASKCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_EQUIPMENTCONTEXTS = MongoCollectionTypes.EQUIPMENTCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_ENVIRONMENTCONTEXTS = MongoCollectionTypes.ENVIRONMENTCONTEXT;

    public void removeUser(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        databaseConnection.removeData(AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, id);
    }

    public ArrayList<User> getAllUsers() throws IOException, IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_USERS);

        User user = new User();
        final ArrayList<User> listUsers = new ArrayList<User>();
        for (final Document entry : listDocuments) {

            user = new User();
            user.setEmail(entry.getString("email"));
            user.setUsername(entry.getString("username"));
            user.setId(entry.getObjectId("_id").toString());

            final List<String> listRoles = Arrays.asList(entry.get("roles").toString()
                    .replace("[", "").replace("]", ""));
            user.setRoles(listRoles);

            listUsers.add(user);
        }
        return listUsers;
    }

    public ArrayList<String[]> getAllUsercontexts() throws IOException, IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_USERCONTEXTS);

        final ArrayList<String[]> listContext = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {

            final Document documentContext = (Document) entry.get("contexts");
            final Document documentDefault = (Document) documentContext.get("default");

            final String userid = entry.getString("owner").toString();
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

        return listContext;
    }

    public ArrayList<String[]> getAllTaskContexts() throws IOException, IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_TASKCONTEXTS);

        final ArrayList<String[]> listTaskContexts = new ArrayList<String[]>();

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
            listTaskContexts.add(myStringArray);
        }
        return listTaskContexts;
    }

    public ArrayList<String[]> getAllEquipmentContexts() throws IOException,
            IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_EQUIPMENTCONTEXTS);

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
            System.out.println(myStringArray);
        }
        return listEquipmentContexts;
    }

    public ArrayList<String[]> getAllEnvironmentContexts() throws IOException,
            IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_ENVIRONMENTCONTEXTS);

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
            System.out.println(myStringArray);
        }
        return listEnvironmentContexts;
    }

    public UpdateResult updateUser(final String id, final String indexName, final String indexValue)
            throws Exception {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        UpdateResult updateResult = null;

        updateResult = databaseConnection.updateDocument(
                AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, id, indexName, indexValue);

        return updateResult;
    }
}
