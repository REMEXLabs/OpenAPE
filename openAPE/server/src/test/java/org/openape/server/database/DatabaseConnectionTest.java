package org.openape.server.database;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.bson.Document;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.json.JsonParseException;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.openape.api.DatabaseObject;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton database connection class. Connects to the online mongo database to
 * store and load contexts and resources. Gets used by
 * {@link EnvironmentContextRequestHandler},
 * {@link EquipmentContextRequestHandler}, {@link TaskContextRequestHandler},
 * {@link UserContextRequestHandler}.
 */
public class DatabaseConnectionTest {
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
    private static DatabaseConnectionTest databaseConnectionInstance;

    /**
     * Get the singleton database connection.
     *
     * @return the database connection.
     */
    public static DatabaseConnectionTest getInstance() {
        if (DatabaseConnectionTest.databaseConnectionInstance == null) {
            DatabaseConnectionTest.databaseConnectionInstance = new DatabaseConnectionTest();
        }
        return DatabaseConnectionTest.databaseConnectionInstance;
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
    private MongoCollection<Document> userContextCollection;
    /**
     * Database collection containing the environment contexts.
     */
    private MongoCollection<Document> environmentContextCollection;
    /**
     * Database collection containing the equipment contexts.
     */
    private MongoCollection<Document> equipmentContextCollection;
    /**
     * Database collection containing the task contexts.
     */
    private MongoCollection<Document> taskContextCollection;
    /**
     * Database collection containing the resources offered by the server.
     */
    private MongoCollection<Document> resourceOfferContectCollection;

    /**
     * Database collection containing the incomplete request resources used by
     * the client to search for fitting resource.
     */
    private MongoCollection<Document> resourceRequestContextCollection;

    /**
     * private constructor to create the singleton database connection instance.
     */
    // It is checked by the try catch block.
    private DatabaseConnectionTest() {
        // Create credentials for the openAPE database
        MongoCredential credential = MongoCredential.createCredential(
                DatabaseConnectionTest.DATABASUSERNAME, DatabaseConnectionTest.DATABASENAME,
                DatabaseConnectionTest.DATABASEPASSWORD.toCharArray());

        // Create database client for the openAPE database
        this.mongoClient = new MongoClient(new ServerAddress(DatabaseConnectionTest.DATABASEURL,
                DatabaseConnectionTest.DATABASEPORT), Arrays.asList(credential));

        // Get a reference to the openAPE database.
        this.database = this.mongoClient.getDatabase(DatabaseConnectionTest.DATABASENAME);
        // Get references to the database collections.
        this.userContextCollection = this.database.getCollection(MongoCollectionTypes.USERCONTEXT
                .toString());
        this.environmentContextCollection = this.database
                .getCollection(MongoCollectionTypes.ENVIRONMENTCONTEXT.toString());
        this.equipmentContextCollection = this.database
                .getCollection(MongoCollectionTypes.EQUIPMENTCONTEXT.toString());
        this.taskContextCollection = this.database.getCollection(MongoCollectionTypes.TASKCONTEXT
                .toString());
        this.resourceOfferContectCollection = this.database
                .getCollection(MongoCollectionTypes.RESOURCEOFFER.toString());
        this.resourceRequestContextCollection = this.database
                .getCollection(MongoCollectionTypes.RESOURCEREQUEST.toString());

    }

    /**
     * Delete a database object, either a context or a resource, from the
     * database. Choose the object via id and the collection via the collection
     * type.
     *
     * @param type
     *            the collection in which the object is located.
     * @param id
     *            the database id within the collection of the object
     * @return true if successful of false if the object is not found.
     * @throws IOException
     *             if a database problem occurs.
     */
    public boolean deleteData(MongoCollectionTypes type, String id) throws IOException {
        MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Create search query.
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        // deleted will be null if no data with the given id is found.
        Document deleted = collectionToWorkOn.findOneAndDelete(query);
        if (deleted == null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * Get a mongo collection reference by providing the collection type.
     *
     * @param type
     * @return the collection reference or null if the type is unknown.
     */
    private MongoCollection<Document> getCollectionByType(MongoCollectionTypes type) {
        if (type.equals(MongoCollectionTypes.USERCONTEXT)) {
            return this.userContextCollection;
        } else if (type.equals(MongoCollectionTypes.ENVIRONMENTCONTEXT)) {
            return this.environmentContextCollection;
        } else if (type.equals(MongoCollectionTypes.EQUIPMENTCONTEXT)) {
            return this.equipmentContextCollection;
        } else if (type.equals(MongoCollectionTypes.TASKCONTEXT)) {
            return this.taskContextCollection;
        } else if (type.equals(MongoCollectionTypes.RESOURCEOFFER)) {
            return this.resourceOfferContectCollection;
        } else if (type.equals(MongoCollectionTypes.RESOURCEREQUEST)) {
            return this.resourceRequestContextCollection;
        } else {
            return null; // Should never occur.
        }
    }

    /**
     * Request a database object, either a context or a resource, from the
     * database. Choose the object via id and the collection via the collection
     * type. The object will remain in the database.
     *
     * @param type
     *            the collection in which the object is located.
     * @param id
     *            the database id within the collection of the object.
     * @return the database object or null if no data with that id is found.
     * @throws IOException
     *             if a database problem occurs.
     */
    public DatabaseObject getData(MongoCollectionTypes type, String id) throws IOException {
        MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Search for object in database.
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        FindIterable<Document> resultIteratable = collectionToWorkOn.find(query);

        Iterator<Document> resultInterator = resultIteratable.iterator();
        if (!resultInterator.hasNext()) {
            // If no result is found return null.
            return null;
        } else {
            // get the first result. Souldn't ever be more than one since _ids
            // are supposed to be unique.
            Document resultDocument = resultInterator.next();

            DatabaseObject result = null;
            try {
                // Remove the automatically added id.
                resultDocument.remove("_id");
                String jsonResult = resultDocument.toJson();
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(jsonResult, DatabaseObject.class);
            } catch (CodecConfigurationException | IOException | JsonParseException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
            return result;
        }
    }

    /**
     * Store a database object, either a context or a resource, into the
     * database. Choose the collection via the collection type.
     *
     * @param type
     *            the collection to store it into.
     * @param data
     *            the object to be stored.
     * @return the id of the stored object within the collection.
     * @throws ClassCastException
     *             if the object class doesn't match the given collection type.
     * @throws IOException
     *             if a database problem occurs.
     */
    public String storeData(MongoCollectionTypes type, DatabaseObject data)
            throws ClassCastException, IOException {
        // Check if data is of the correct type for the collection.
        if (!type.getDocumentType().equals(data.getClass())) {
            throw new ClassCastException();
        }

        MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Create Document from data.
        Document dataDocument = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonData = mapper.writeValueAsString(data);
            dataDocument = Document.parse(jsonData);
            // Insert the document.
            collectionToWorkOn.insertOne(dataDocument);
        } catch (IOException | JsonParseException | MongoException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        // Get the automatically appended id.
        ObjectId id = null;
        try {
            id = (ObjectId) dataDocument.get("_id");
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        return id.toHexString();
    }

    /**
     * Update a database object, either a context or a resource, in the
     * database. Choose the object via id and the collection via the collection
     * type.
     *
     * @param type
     *            the collection in which the object is located.
     * @param data
     *            the new version of the object.
     * @param id
     *            the database id within the collection of the object.
     * @return true if successful or false if no object with that id is found
     *         and nothing is stored.
     * @throws ClassCastException
     *             if the object class doesn't match the given collection type.
     * @throws IOException
     *             if a database problem occurs.
     */
    public boolean updateData(MongoCollectionTypes type, DatabaseObject data, String id)
            throws ClassCastException, IOException {
        // Check if data is of the correct type for the collection.
        if (!type.getDocumentType().equals(data.getClass())) {
            throw new ClassCastException();
        }

        MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Create search query.
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        try {
            // Create document object from data.
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonData = mapper.writeValueAsString(data);
            Document dataDocument = Document.parse(jsonData);

            // update data.
            collectionToWorkOn.findOneAndReplace(query, dataDocument);
        } catch (IOException | JsonParseException | MongoException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        return true;
    }

}