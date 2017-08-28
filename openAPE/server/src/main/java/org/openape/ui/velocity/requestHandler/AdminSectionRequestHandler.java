package org.openape.ui.velocity.requestHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.Document;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import com.mongodb.client.result.UpdateResult;

public class AdminSectionRequestHandler {
    public static final MongoCollectionTypes COLLECTIONTOUSE_USERS = MongoCollectionTypes.USERS;
    public static final MongoCollectionTypes COLLECTIONTOUSE_USERCONTEXTS = MongoCollectionTypes.USERCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_TASKCONTEXTS = MongoCollectionTypes.TASKCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_EQUIPMENTCONTEXTS = MongoCollectionTypes.EQUIPMENTCONTEXT;
    public static final MongoCollectionTypes COLLECTIONTOUSE_ENVIRONMENTCONTEXTS = MongoCollectionTypes.ENVIRONMENTCONTEXT;

    public List<EnvironmentContext> getAllEnvironmentContexts() throws IOException,
            IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final List<DatabaseObject> lilstEnvironmentContextDBObject = databaseConnection
                .getDatabaseObjectsByQuery(AdminSectionRequestHandler.COLLECTIONTOUSE_ENVIRONMENTCONTEXTS, null);
        
        final List<EnvironmentContext> listEnvironmentContexts = new ArrayList<EnvironmentContext>();
        
        for(DatabaseObject environmentContextDBObject : lilstEnvironmentContextDBObject){
        	listEnvironmentContexts.add((EnvironmentContext) environmentContextDBObject);
        }

        return listEnvironmentContexts;
    }

    public List<EquipmentContext> getAllEquipmentContexts() throws IOException,
            IllegalArgumentException {

        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final List<DatabaseObject> lilstEquipmentContextDBObject = databaseConnection
                .getDatabaseObjectsByQuery(AdminSectionRequestHandler.COLLECTIONTOUSE_EQUIPMENTCONTEXTS, null);
        
        final List<EquipmentContext> listEquipmentContexts = new ArrayList<EquipmentContext>();
        
        for(DatabaseObject equipmentContextDBObject : lilstEquipmentContextDBObject){
        	listEquipmentContexts.add((EquipmentContext) equipmentContextDBObject);
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

    public List<UserContext> getAllUsercontexts() throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final List<DatabaseObject> lilstUserContextDBObject = databaseConnection
                .getDatabaseObjectsByQuery(AdminSectionRequestHandler.COLLECTIONTOUSE_USERCONTEXTS, null);
        
        final List<UserContext> listUserContexts = new ArrayList<UserContext>();
        
        for(DatabaseObject userContextDBObject : lilstUserContextDBObject){
        	listUserContexts.add((UserContext) userContextDBObject);
        }

        return listUserContexts;
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
        databaseConnection.deleteDatabaseObject(AdminSectionRequestHandler.COLLECTIONTOUSE_USERS,
                id);
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
