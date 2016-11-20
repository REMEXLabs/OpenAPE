package org.openape.server.database;

import java.util.Arrays;

import org.openape.server.EnvironmentContextRequestHandler;
import org.openape.server.EquipmentContextRequestHandler;
import org.openape.server.TaskContextRequestHandler;
import org.openape.server.UserContextRequestHandler;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
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
     * Database Client used to connect to the openAPE Database.
     */
    private MongoClient mongoClient;
    /**
     * Reference to the openAPE database.
     */
    private MongoDatabase database;

    /**
     * private constructor to create the singleton database connection instance.
     */
    private DatabaseConnection() {
        // Create credentials for the openAPE database
        MongoCredential credential = MongoCredential.createCredential(
                DATABASUSERNAME, DATABASENAME, DATABASEPASSWORD.toCharArray());

        // Create database client for the openAPE database
        mongoClient = new MongoClient(new ServerAddress(DATABASEURL,
                DATABASEPORT), Arrays.asList(credential));

        // Get a reference to the openAPE database.
        database = mongoClient.getDatabase(DATABASENAME);

    }

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

}
