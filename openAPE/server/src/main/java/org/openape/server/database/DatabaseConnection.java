package org.openape.server.database;

import java.util.Arrays;

import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.resource.Resource;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.usercontext.UserContext;
import org.openape.server.EnvironmentContextRequestHandler;
import org.openape.server.EquipmentContextRequestHandler;
import org.openape.server.TaskContextRequestHandler;
import org.openape.server.UserContextRequestHandler;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton database connection class. Connects to the online mongo database to
 * store and load contexts and resources. Gets used by
 * {@link EnvironmentContextRequestHandler},
 * {@link EquipmentContextRequestHandler}, {@link TaskContextRequestHandler},
 * {@link UserContextRequestHandler}.
 */
public class DatabaseConnection {

    /**
     * The url to our mongo database server.
     */
    private static final String DATABASEURL = "localhost"; // TODO replace by
                                                           // the mongoDB url.
    /**
     * The standard port for online mongo databases.
     */
    private static final int DATABASEPORT = 27017;

    /**
     * The name of the mongo database holding the relevant data for this
     * application.
     */
    private static final String DATABASENAME = "openAPE";

    /**
     * The user name used by this application to connect to the mongo database.
     */
    private static final String DATABASUSERNAME = "openAPE";

    /**
     * The password used by this application to connect to the mongo database.
     */
    private static final String DATABASEPASSWORD = "1234";

    /**
     * Singleton instance of this class.
     */
    private static DatabaseConnection databaseConnectionInstance;

    /**
     * Get the singleton database connection.
     * 
     * @return the database connection.
     */
    public static DatabaseConnection getInstance() {
        if (DatabaseConnection.databaseConnectionInstance == null) {
            DatabaseConnection.databaseConnectionInstance = new DatabaseConnection();
        }
        return DatabaseConnection.databaseConnectionInstance;
    }

    /**
     * Database Client used to connect to the openAPE Database.
     */
    private MongoClient mongoClient;

    /**
     * Reference to the openAPE database.
     */
    private MongoDatabase database;
    /**
     * Database collection containing the user contexts.
     */
    private MongoCollection<UserContext> userContextCollection;
    /**
     * Database collection containing the environment contexts.
     */
    private MongoCollection<EnvironmentContext> environmentContextCollection;
    /**
     * Database collection containing the equipment contexts.
     */
    private MongoCollection<EquipmentContext> equipmentContextCollection;
    /**
     * Database collection containing the task contexts.
     */
    private MongoCollection<TaskContext> taskContextCollection;
    /**
     * Database collection containing the resources offered by the server.
     */
    private MongoCollection<Resource> resourceOfferContectCollection;

    /**
     * Database collection containing the incomplete request resources used by
     * the client to search for fitting resource.
     */
    private MongoCollection<Resource> resourceRequestContextCollection;

    /**
     * private constructor to create the singleton database connection instance.
     */
    @SuppressWarnings("unchecked") // It is checked by the try catch block.
    private DatabaseConnection() {
        // Create credentials for the openAPE database
        MongoCredential credential = MongoCredential.createCredential(DATABASUSERNAME, DATABASENAME,
                DATABASEPASSWORD.toCharArray());

        // Create database client for the openAPE database
        this.mongoClient = new MongoClient(new ServerAddress(DATABASEURL, DATABASEPORT), Arrays.asList(credential));

        // Get a reference to the openAPE database.
        this.database = this.mongoClient.getDatabase(DATABASENAME);
        // Get references to the database collections.
        try {
            this.userContextCollection = (MongoCollection<UserContext>) this.database.getCollection(
                    MongoCollectionTypes.USERCONTEXT.toString(), MongoCollectionTypes.USERCONTEXT.getDocumentType());
            this.environmentContextCollection = (MongoCollection<EnvironmentContext>) this.database.getCollection(
                    MongoCollectionTypes.ENVIRONMENTCONTEXT.toString(),
                    MongoCollectionTypes.ENVIRONMENTCONTEXT.getDocumentType());
            this.equipmentContextCollection = (MongoCollection<EquipmentContext>) this.database.getCollection(
                    MongoCollectionTypes.EQUIPMENTCONTEXT.toString(),
                    MongoCollectionTypes.EQUIPMENTCONTEXT.getDocumentType());
            this.taskContextCollection = (MongoCollection<TaskContext>) this.database.getCollection(
                    MongoCollectionTypes.TASKCONTEXT.toString(), MongoCollectionTypes.TASKCONTEXT.getDocumentType());
            this.resourceOfferContectCollection = (MongoCollection<Resource>) this.database.getCollection(
                    MongoCollectionTypes.RESOURCEOFFER.toString(),
                    MongoCollectionTypes.RESOURCEOFFER.getDocumentType());
            this.resourceRequestContextCollection = (MongoCollection<Resource>) this.database.getCollection(
                    MongoCollectionTypes.RESOURCEREQUEST.toString(),
                    MongoCollectionTypes.RESOURCEREQUEST.getDocumentType());
        } catch (ClassCastException e) {
            e.printStackTrace();
            System.exit(0);// TODO handle exception.
        }

    }

    /**
     * Get a mongo collection reference by providing the collection type.
     * 
     * @param type
     * @return the collection reference or null if the type is unknown.
     */
    private MongoCollection<?> getCollectionByType(MongoCollectionTypes type) {
        if (type.equals(MongoCollectionTypes.USERCONTEXT))
            return this.userContextCollection;
        else if (type.equals(MongoCollectionTypes.ENVIRONMENTCONTEXT))
            return this.environmentContextCollection;
        else if (type.equals(MongoCollectionTypes.EQUIPMENTCONTEXT))
            return this.equipmentContextCollection;
        else if (type.equals(MongoCollectionTypes.TASKCONTEXT))
            return this.taskContextCollection;
        else if (type.equals(MongoCollectionTypes.RESOURCEOFFER))
            return this.resourceOfferContectCollection;
        else if (type.equals(MongoCollectionTypes.RESOURCEREQUEST))
            return this.resourceRequestContextCollection;
        else
            return null; // Should never occur.
    }

}
