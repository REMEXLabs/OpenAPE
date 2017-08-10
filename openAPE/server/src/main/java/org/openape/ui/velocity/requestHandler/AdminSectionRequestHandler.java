package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.Document;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import com.mongodb.client.result.UpdateResult;

public class AdminSectionRequestHandler {
    public static final MongoCollectionTypes COLLECTIONTOUSE_USERS = MongoCollectionTypes.USERS;
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
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_ENVIRONMENTCONTEXTS);

        final ArrayList<String[]> listEnvironmentContexts = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {
            final String id = entry.getObjectId("_id").toString();
            final String userId = entry.getString("owner").toString();

            final boolean isPublic = entry.getBoolean("public");
            final long unixSeconds = entry.getObjectId("_id").getTimestamp();
            final Date date = new Date(unixSeconds * 1000L); // *1000 is to
                                                             // convert seconds
                                                             // to milliseconds
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the
                                                                             // format
                                                                             // of
                                                                             // your
                                                                             // date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone
                                                            // reference for
                                                            // formating (see
                                                            // comment at the
                                                            // bottom
            final String formattedDate = sdf.format(date);

            String stringIsPublic = "";

            if (isPublic == false) {
                stringIsPublic = "false";
            } else {
                stringIsPublic = "true";
            }

            final String[] myStringArray = { id, userId, stringIsPublic, formattedDate };
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
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_EQUIPMENTCONTEXTS);

        final ArrayList<String[]> listEquipmentContexts = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {
            final String id = entry.getObjectId("_id").toString();
            final String userId = entry.getString("owner").toString();

            final boolean isPublic = entry.getBoolean("public");

            String stringIsPublic = "";

            final long unixSeconds = entry.getObjectId("_id").getTimestamp();
            final Date date = new Date(unixSeconds * 1000L); // *1000 is to
                                                             // convert seconds
                                                             // to milliseconds
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the
                                                                             // format
                                                                             // of
                                                                             // your
                                                                             // date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone
                                                            // reference for
                                                            // formating (see
                                                            // comment at the
                                                            // bottom
            final String formattedDate = sdf.format(date);

            if (isPublic == false) {
                stringIsPublic = "false";
            } else {
                stringIsPublic = "true";
            }

            final String[] myStringArray = { id, userId, stringIsPublic, formattedDate };
            listEquipmentContexts.add(myStringArray);

        }
        return listEquipmentContexts;
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

            final long unixSeconds = entry.getObjectId("_id").getTimestamp();
            final Date date = new Date(unixSeconds * 1000L); // *1000 is to
                                                             // convert seconds
                                                             // to milliseconds
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the
                                                                             // format
                                                                             // of
                                                                             // your
                                                                             // date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone
                                                            // reference for
                                                            // formating (see
                                                            // comment at the
                                                            // bottom
            final String formattedDate = sdf.format(date);

            String stringIsPublic = "";

            if (isPublic == false) {
                stringIsPublic = "false";
            } else {
                stringIsPublic = "true";
            }

            final String[] myStringArray = { id, userId, stringIsPublic, formattedDate };
            listTaskContexts.add(myStringArray);
        }
        return listTaskContexts;
    }

    public ArrayList<String[]> getAllUsercontexts() throws IOException, IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final ArrayList<Document> listDocuments = databaseConnection
                .getAllDocuments(AdminSectionRequestHandler.COLLECTIONTOUSE_USERCONTEXTS);

        final ArrayList<String[]> listContext = new ArrayList<String[]>();

        for (final Document entry : listDocuments) {

            final String userid = entry.getString("owner").toString();
            final String id = entry.getObjectId("_id").toString();

            final long unixSeconds = entry.getObjectId("_id").getTimestamp();
            final Date date = new Date(unixSeconds * 1000L); // *1000 is to
                                                             // convert seconds
                                                             // to milliseconds
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the
                                                                             // format
                                                                             // of
                                                                             // your
                                                                             // date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone
                                                            // reference for
                                                            // formating (see
                                                            // comment at the
                                                            // bottom
            final String formattedDate = sdf.format(date);

            final boolean isPublic = entry.getBoolean("public");
            String stringIsPublic = "";

            if (isPublic == false) {
                stringIsPublic = "false";
            } else {
                stringIsPublic = "true";
            }

            final String[] myStringArray = { userid, id, stringIsPublic, formattedDate };

            listContext.add(myStringArray);
        }

        return listContext;
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

    public void removeUser(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        databaseConnection.removeData(AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, id);
    }

    public UpdateResult updateUser(final String id, final String indexName, final Object indexValue)
            throws Exception {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        UpdateResult updateResult = null;

        updateResult = databaseConnection.updateDocument(
                AdminSectionRequestHandler.COLLECTIONTOUSE_USERS, id, indexName, indexValue);

        return updateResult;
    }
}
