package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.conversions.Bson;
import org.bson.json.JsonParseException;
import org.bson.types.ObjectId;
import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.server.MongoConfig;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

/**
 * Singleton database connection class. Connects to the online mongo database to
 * store and load contexts and resources. Gets used by
 * {@link EnvironmentContextRequestHandler},
 * {@link EquipmentContextRequestHandler}, {@link TaskContextRequestHandler},
 * {@link UserContextRequestHandler}.
 */
public class DatabaseConnection implements ServerMonitorListener {

    static Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    static boolean firstTime = true; // only for logging control needed

    /**
     * The url to our mongo database server.
     */
    private static String DATABASEURL = Messages.getString("DatabaseConnection.MongoDBServerAddress"); //$NON-NLS-1$
    /**
     * The standard port for online mongo databases.
     */
    private static String DATABASEPORT = Messages.getString("DatabaseConnection.MongoDBServerPort"); //$NON-NLS-1$

    /**
     * The name of the mongo database holding the relevant data for this
     * application.
     */
    private static String DATABASENAME = Messages.getString("DatabaseConnection.MongoDBDatabaseName"); //$NON-NLS-1$

    /**
     * The user name used by this application to connect to the mongo database.
     */
    private static String DATABASEUSERNAME = Messages.getString("DatabaseConnection.MongoDBDatabaseUsername"); //$NON-NLS-1$

    /**
     * The password used by this application to connect to the mongo database.
     */
    private static String DATABASEPASSWORD = Messages.getString("DatabaseConnection.MongoDBDatabaseUserPassword"); //$NON-NLS-1$

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
            DatabaseConnection.logger.info("new database connection required");
            DatabaseConnection.databaseConnectionInstance = new DatabaseConnection();
        } else {
            DatabaseConnection.logger.info("Found existing database connection.");
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
     * Database collection containing the resources descriptions by the server.
     */
    private MongoCollection<Document> resourceDescriptionContectCollection;
    /**
     * Database collection containing the listings used by the client to search
     * for fitting resource.
     */
    private MongoCollection<Document> listingContextCollection;
    /**
     * Database collection holding the describing objects of the stored
     * resources.
     */
    private MongoCollection<Document> resourceObjectCollection;
    /**
     * Database collection holding the users.
     */
    private MongoCollection<Document> userCollection;
    /**
     * Database collection holding the groups.
     */
    private MongoCollection<Document> groupCollection;

    /**
     * private constructor to create the singleton database connection instance.
     */
    private DatabaseConnection() {
        // import configuration file
        this.readConfigFile();
        try {
            // Create credentials for the openAPE database
            final MongoCredential credential = MongoCredential.createCredential(DatabaseConnection.DATABASEUSERNAME,
                    DatabaseConnection.DATABASENAME, DatabaseConnection.DATABASEPASSWORD.toCharArray());

            // Add MongoDB Monitor with client options
            final MongoClientOptions clientOptions = new MongoClientOptions.Builder().addServerMonitorListener(this)
                    .build();

            // Create database client for the openAPE database
            this.mongoClient = new MongoClient(
                    new ServerAddress(DatabaseConnection.DATABASEURL,
                            Integer.parseInt(DatabaseConnection.DATABASEPORT)),
                    Arrays.asList(credential), clientOptions);

            // Get a reference to the openAPE database.
            this.database = this.mongoClient.getDatabase(DatabaseConnection.DATABASENAME);
            DatabaseConnection.logger.info("Found openAPE dataBase");
        } catch (final Exception e) {
            DatabaseConnection.logger.error("Failed to connect to the openAPE database");
            return;
        }

        // Get references to the database collections.
        try {
            this.userContextCollection = this.database.getCollection(MongoCollectionTypes.USERCONTEXT.toString());
        } catch (final Exception e) {
            DatabaseConnection.logger.error("Couldn't find collection \"" + MongoCollectionTypes.USERCONTEXT + "\"");
        }
        this.environmentContextCollection = this.database
                .getCollection(MongoCollectionTypes.ENVIRONMENTCONTEXT.toString());
        this.equipmentContextCollection = this.database.getCollection(MongoCollectionTypes.EQUIPMENTCONTEXT.toString());
        this.taskContextCollection = this.database.getCollection(MongoCollectionTypes.TASKCONTEXT.toString());
        this.resourceDescriptionContectCollection = this.database
                .getCollection(MongoCollectionTypes.RESOURCEDESCRIPTION.toString());
        this.listingContextCollection = this.database.getCollection(MongoCollectionTypes.LISTING.toString());
        this.resourceObjectCollection = this.database.getCollection(MongoCollectionTypes.RESOURCEOBJECTS.toString());
        this.userCollection = this.database.getCollection(MongoCollectionTypes.USERS.toString());
        this.groupCollection = this.database.getCollection(MongoCollectionTypes.GROUPS.toString());

    }

    /**
     * Converts a MongoDB database object / document to an object of type
     * {@link DatabaseObject}.
     *
     * @param type
     *            the type of the object, to which the database document should
     *            be converted. It must not be null!
     * @param resultDocument
     *            the database object / document, which will be converted into a database object. It must
     *            not be null!
     * @return converted database object
     * @throws com.fasterxml.jackson.core.JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    private DatabaseObject convertDocumentToDatabaseObject(final MongoCollectionTypes type,
            final Document resultDocument)
            throws com.fasterxml.jackson.core.JsonParseException, JsonMappingException, IOException {
        DatabaseObject databaseObject = null;
        try {
            // Remove the MongoDB id field
            final ObjectId oid = (ObjectId) resultDocument.get("_id");
            resultDocument.remove(Messages.getString("DatabaseConnection._id")); //$NON-NLS-1$
            String jsonResult = resultDocument.toJson();
            // reverse mongo special character replacement.
            jsonResult = this.reverseMongoSpecialCharsReplacement(jsonResult);
            final ObjectMapper mapper = new ObjectMapper();
            databaseObject = mapper.readValue(jsonResult, type.getDocumentType());
            // Set MongoDB id field value as id
            databaseObject.setId(oid.toString());
        } catch (CodecConfigurationException | IOException | JsonParseException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return databaseObject;
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
    public boolean deleteData(final MongoCollectionTypes type, final String id) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Create search query.
        final BasicDBObject query = new BasicDBObject();
        query.put(Messages.getString("DatabaseConnection._id"), new ObjectId(id)); //$NON-NLS-1$

        // deleted will be null if no data with the given id is found.
        final Document deleted = collectionToWorkOn.findOneAndDelete(query);
        if (deleted == null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * Make sure indexes for the application are set.
     */
    public void ensureIndexes() {
        // Make sure email is unique for all users
        this.userCollection.createIndex(new BasicDBObject("email", 1), new IndexOptions().unique(true));
        // Make sure username is unique for all users
        this.userCollection.createIndex(new BasicDBObject("username", 1), new IndexOptions().unique(true));
    }

    /**
     * Execute a Mongo BasicDBObject query on the given collection and return
     * the result as a DatabaseObject of the given collection.
     *
     * @param type
     *            the type of the collections DatabaseObject
     * @param collection
     *            the collection in which the object is located.
     * @param query
     *            the Mongo query to be executed
     * @return
     * @throws IOException
     */
    private DatabaseObject executeQuery(final MongoCollectionTypes type, final MongoCollection<Document> collection,
            final BasicDBObject query, final boolean includeId) throws IOException {
        final Iterator<Document> resultIterator = collection.find(query).iterator();
        if (resultIterator.hasNext()) {
            final Document resultDocument = resultIterator.next();
            DatabaseObject result = null;
            try {
                // Remove the MongoDB id field
                final ObjectId oid = (ObjectId) resultDocument.get("_id");
                resultDocument.remove(Messages.getString("DatabaseConnection._id")); //$NON-NLS-1$
                if (includeId) {
                    resultDocument.append("id", oid.toString());
                }
                String jsonResult = resultDocument.toJson();
                // reverse mongo special character replacement.
                jsonResult = this.reverseMongoSpecialCharsReplacement(jsonResult);
                final ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(jsonResult, type.getDocumentType());
            } catch (CodecConfigurationException | IOException | JsonParseException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
            return result;
        } else {
            // If no result is found return null.
            return null;
        }
    }

    public ArrayList<Document> getAllDocuments(final MongoCollectionTypes type) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        final ArrayList<Document> listDocuments = new ArrayList<Document>();
        // Search for object in database.

        final FindIterable<Document> find = collectionToWorkOn.find();

        final MongoCursor<Document> cursor = find.iterator();

        while (cursor.hasNext()) {
            listDocuments.add(cursor.next());
        }
        return listDocuments;
    }

    /*
     * public ArrayList<Document> getAllDocuments(final String string) { // TODO
     * Auto-generated method stub return null; }
     */

    public ArrayList<Document> getAllDocumentsByKey(final MongoCollectionTypes type, final String key)
            throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        final ArrayList<Document> listDocuments = new ArrayList<Document>();
        // Search for object in database.

        final FindIterable<Document> find = collectionToWorkOn.find();

        final MongoCursor<Document> cursor = find.iterator();

        while (cursor.hasNext()) {
            listDocuments.add(cursor.next());
        }
        return listDocuments;
    }

    /**
     * @param type
     *            of data. Needed to choose the right mongo collection.
     * @return all objects in the collection of the given type and their
     *         corresponding ids.
     * @throws IOException
     *             if database or parse error occurs.
     */
    public Map<String, DatabaseObject> getAllObjectsOfType(final MongoCollectionTypes type) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        final Iterator<Document> resultIterator = collectionToWorkOn.find().iterator();
        final Map<String, DatabaseObject> resultMap = new HashMap<String, DatabaseObject>();
        while (resultIterator.hasNext()) {
            final Document resultDocument = resultIterator.next();
            DatabaseObject result = null;
            ObjectId oid = null;
            try {
                // Remove the MongoDB id field
                oid = (ObjectId) resultDocument.get("_id");
                resultDocument.remove(Messages.getString("DatabaseConnection._id")); //$NON-NLS-1$
                String jsonResult = resultDocument.toJson();
                // reverse mongo special character replacement.
                jsonResult = this.reverseMongoSpecialCharsReplacement(jsonResult);
                final ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(jsonResult, type.getDocumentType());
            } catch (CodecConfigurationException | IOException | JsonParseException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
            resultMap.put(oid.toString(), result);
        }
        return resultMap;

    }

    /**
     * Query a collection by a certain attribute and value. Will return the
     * first document matching the query or null if no document matches the
     * query.
     *
     * @param type
     *            the collection in which the object is located.
     * @param attribute
     *            the attribute to query for
     * @param value
     *            the value for the attribute to query for
     * @return
     * @throws IOException
     */
    public DatabaseObject getByUniqueAttribute(final MongoCollectionTypes type, final String attribute,
            final String value) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        // Search for object in database.
        final BasicDBObject query = new BasicDBObject();
        query.put(attribute, value);
        return this.executeQuery(type, collectionToWorkOn, query, true);
    }

    /**
     * Get a mongo collection reference by providing the collection type.
     *
     * @param type
     * @return the collection reference or null if the type is unknown.
     */
    private MongoCollection<Document> getCollectionByType(final MongoCollectionTypes type) {
        if (type.equals(MongoCollectionTypes.USERCONTEXT)) {
            return this.userContextCollection;
        } else if (type.equals(MongoCollectionTypes.ENVIRONMENTCONTEXT)) {
            return this.environmentContextCollection;
        } else if (type.equals(MongoCollectionTypes.EQUIPMENTCONTEXT)) {
            return this.equipmentContextCollection;
        } else if (type.equals(MongoCollectionTypes.TASKCONTEXT)) {
            return this.taskContextCollection;
        } else if (type.equals(MongoCollectionTypes.RESOURCEDESCRIPTION)) {
            return this.resourceDescriptionContectCollection;
        } else if (type.equals(MongoCollectionTypes.LISTING)) {
            return this.listingContextCollection;
        } else if (type.equals(MongoCollectionTypes.RESOURCEOBJECTS)) {
            return this.resourceObjectCollection;
        } else if (type.equals(MongoCollectionTypes.USERS)) {
            return this.userCollection;
        } else if (type.equals(MongoCollectionTypes.GROUPS)) {
            return this.groupCollection;
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
    public DatabaseObject getData(final MongoCollectionTypes type, final String id) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        // Search for object in database.
        final BasicDBObject query = new BasicDBObject();
        query.put(Messages.getString("DatabaseConnection._id"), new ObjectId(id));
        return this.executeQuery(type, collectionToWorkOn, query, false);
    }

    /**
     * Select objects of a given type from the database. It is possible to
     * select all objects or to refine the selection by a query.
     *
     * @param type
     *            the type of the objects, which should be selected from the
     *            database. It must not be null!
     * @param query
     *            the query. It defines which objects of the defined type should
     *            be selected. If all objects of the defined type should be
     *            selected, the query object has to be "empty" or null.
     * @return a map of objects of the defined type, which comply the query
     *         conditions. The objects are the values and the objects database
     *         IDs are the keys. If no object complies the query conditions or
     *         the collection is empty, an empty map will be returned.
     * @throws IOException
     *             if a problem with the database or during the object mapping
     *             occurs.
     */
    public Map<String, DatabaseObject> getDatabaseObjectsByQuery(final MongoCollectionTypes type, BasicDBObject query)
            throws IOException {
        final Map<String, DatabaseObject> databaseObjects = new HashMap<String, DatabaseObject>();
        boolean includeId = false;
        if ((type == MongoCollectionTypes.GROUPS) || (type == MongoCollectionTypes.USERS)) {
            includeId = true;
        }
        if (query == null) {
            query = new BasicDBObject();
        }
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        final MongoCursor<Document> cursor = collectionToWorkOn.find(query).iterator();
        while (cursor.hasNext()) {
            final Document resultDocument = cursor.next();
            final DatabaseObject databaseObject = this.convertDocumentToDatabaseObject(type, resultDocument);
            databaseObjects.put(databaseObject.getId(), databaseObject);
        }
        cursor.close();
        return databaseObjects;
    }

    /**
     * Select objects of a given type from the database. It is possible to
     * select all objects or to refine the selection by a query.
     *
     * @param type
     *            the type of the objects, which should be selected from the
     *            database. It must not be null!
     * @param query
     *            the query. It defines which objects of the defined type should
     *            be selected. If all objects of the defined type should be
     *            selected, the query object has to be "empty" or null.
     * @return a list with objects of the defined type, which comply the query
     *         conditions. If no object complies the query conditions or the collection is empty, an empty list will be
     *         returned.
     * @throws IOException
     *             if a problem with the database or during the object mapping occurs.
     */
    public List<DatabaseObject> getDocumentsByQuery(final MongoCollectionTypes type, BasicDBObject query)
            throws IOException {
        final List<DatabaseObject> databaseObjects = new ArrayList<DatabaseObject>();
        if (query == null) {
            query = new BasicDBObject();
        }
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        final MongoCursor<Document> cursor = collectionToWorkOn.find(query).iterator();
        while (cursor.hasNext()) {
            final Document resultDocument = cursor.next();
            databaseObjects.add(this.convertDocumentToDatabaseObject(type, resultDocument));
        }
        cursor.close();
        return databaseObjects;
    }

    private void readConfigFile() {
        final String name = MongoConfig.getString("databaseName");//$NON-NLS-1$
        if ((name != null) && !name.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASENAME = name;
        } else {
            DatabaseConnection.DATABASENAME = Messages.getString("DatabaseConnection.MongoDBDatabaseName"); //$NON-NLS-1$
        }
        final String address = MongoConfig.getString("databaseURL");//$NON-NLS-1$
        if ((address != null) && !address.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEURL = address;
        } else {
            DatabaseConnection.DATABASEURL = Messages.getString("DatabaseConnection.MongoDBServerAddress"); //$NON-NLS-1$
        }
        final String port = MongoConfig.getString("databasePort");//$NON-NLS-1$
        if ((port != null) && !port.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.logger.debug("Using MongoDB port " + port + " defined in mongo.properties");
            DatabaseConnection.DATABASEPORT = port;
        } else {
            final String standardPort = Messages.getString("DatabaseConnection.MongoDBServerPort"); //$NON-NLS-1$
            DatabaseConnection.logger.debug("Using MongoDB port " + standardPort + " defined in Messages.properties");
            DatabaseConnection.DATABASEPORT = standardPort;
        }
        final String password = MongoConfig.getString("databasePassword");//$NON-NLS-1$
        if ((password != null) && !password.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEPASSWORD = password;
        } else {
            DatabaseConnection.DATABASEPASSWORD = Messages.getString("DatabaseConnection.MongoDBDatabaseUserPassword"); //$NON-NLS-1$
        }
        final String userName = MongoConfig.getString("databaseUsername");//$NON-NLS-1$
        if ((userName != null) && !userName.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEUSERNAME = userName;
        } else {
            DatabaseConnection.DATABASEUSERNAME = Messages.getString("DatabaseConnection.MongoDBDatabaseUsername"); //$NON-NLS-1$
        }

    }

    /***************/

    public void removeData(final MongoCollectionTypes type, final String id) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);
        // Search for object in database.
        final Bson filter = new Document(Messages.getString("DatabaseConnection._id"), new ObjectId(id));
        collectionToWorkOn.deleteOne(filter);
    }

    /**
     * Replaces special chars '.' and '$' with '#046' and '#036".
     *
     * @param jsonToStore
     * @return The modified string.
     * @throws IllegalArgumentException
     *             if it already contains '#046' or '#036".
     */
    private String replaceMongoSpecialChars(String jsonToStore) throws IllegalArgumentException {
        if (jsonToStore.contains(Messages.getString("DatabaseConnection.pointAsciiCode")) //$NON-NLS-1$
                || jsonToStore.contains(Messages.getString("DatabaseConnection.$AsciiCode"))) { //$NON-NLS-1$
            throw new IllegalArgumentException(
                    Messages.getString("DatabaseConnection.specialCharReplacementInUseErrorMsg")); //$NON-NLS-1$
        } else if (jsonToStore.contains(Messages.getString("DatabaseConnection.point")) //$NON-NLS-1$
                || jsonToStore.contains(Messages.getString("DatabaseConnection.$"))) { //$NON-NLS-1$
            jsonToStore = jsonToStore.replace(Messages.getString("DatabaseConnection.point"), //$NON-NLS-1$
                    Messages.getString("DatabaseConnection.pointAsciiCode")); //$NON-NLS-1$
            jsonToStore = jsonToStore.replace(Messages.getString("DatabaseConnection.$"), //$NON-NLS-1$
                    Messages.getString("DatabaseConnection.$AsciiCode")); //$NON-NLS-1$
        }
        return jsonToStore;
    }

    /**
     * Replaces '#046' and '#036" with '.' and '$'.
     *
     * @param jsonFromStorage
     * @return The modified string.
     */
    private String reverseMongoSpecialCharsReplacement(String jsonFromStorage) throws IllegalArgumentException {
        if (jsonFromStorage.contains(Messages.getString("DatabaseConnection.pointAsciiCode")) //$NON-NLS-1$
                || jsonFromStorage.contains(Messages.getString("DatabaseConnection.$AsciiCode"))) { //$NON-NLS-1$
            jsonFromStorage = jsonFromStorage.replace(Messages.getString("DatabaseConnection.pointAsciiCode"), //$NON-NLS-1$
                    Messages.getString("DatabaseConnection.point")); //$NON-NLS-1$
            jsonFromStorage = jsonFromStorage.replace(Messages.getString("DatabaseConnection.$AsciiCode"), //$NON-NLS-1$
                    Messages.getString("DatabaseConnection.$")); //$NON-NLS-1$
        }
        return jsonFromStorage;
    }

    @Override
    public void serverHearbeatStarted(final ServerHeartbeatStartedEvent event) {
        if (DatabaseConnection.firstTime == true) {
            DatabaseConnection.logger.info("Found new heartbeat with connection ID: " + event.getConnectionId());
            DatabaseConnection.firstTime = false;
        } else {
            DatabaseConnection.logger.debug("Found new heartbeat with connection ID: " + event.getConnectionId());
        }
    }

    @Override
    public void serverHeartbeatFailed(final ServerHeartbeatFailedEvent event) {

        DatabaseConnection.logger.error("Connecting to MongoDB at " + DatabaseConnection.DATABASEURL + ":"
                + DatabaseConnection.DATABASEPORT + " failed.\n" + event);
        DatabaseConnection.firstTime = true; // logger can now indicate when new
                                             // connection will be found again.
                                             // connection will be found
                                             // again.

    }

    @Override
    public void serverHeartbeatSucceeded(final ServerHeartbeatSucceededEvent event) {
        DatabaseConnection.logger.debug("Found heartbeat with connection ID: " + event.getConnectionId());

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
    public String storeData(final MongoCollectionTypes type, final DatabaseObject data)
            throws ClassCastException, IOException, IllegalArgumentException {
        // Check if data is of the correct type for the collection.
        if (!type.getDocumentType().equals(data.getClass())) {
            throw new ClassCastException(Messages.getString("DatabaseConnection.doctypeErrorMassage") //$NON-NLS-1$
                    + type.getDocumentType().getName());
        }

        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Create Document from data.
        Document dataDocument = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(data);
            // Deal with special mongoDB characters '.' and '$'.
            jsonData = this.replaceMongoSpecialChars(jsonData);
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
            id = (ObjectId) dataDocument.get(Messages.getString("DatabaseConnection._id")); //$NON-NLS-1$
        } catch (final ClassCastException e) {
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
    public boolean updateData(final MongoCollectionTypes type, final DatabaseObject data, final String id)
            throws ClassCastException, IOException {
        // test if data can be found. Throws exceptions or is null if not.
        if (this.getData(type, id) == null) {
            return false;
        }

        // Check if data is of the correct type for the collection.
        if (!type.getDocumentType().equals(data.getClass())) {
            throw new ClassCastException(Messages.getString("DatabaseConnection.doctypeErrorMassage") //$NON-NLS-1$
                    + type.getDocumentType().getName());
        }

        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Create search query.
        final BasicDBObject query = new BasicDBObject();
        query.put(Messages.getString("DatabaseConnection._id"), new ObjectId(id)); //$NON-NLS-1$

        try {
            // Create document object from data.
            final ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(data);
            // Deal with special mongoDB characters '.' and '$'.
            jsonData = this.replaceMongoSpecialChars(jsonData);
            final Document dataDocument = Document.parse(jsonData);

            // update data.
            collectionToWorkOn.findOneAndReplace(query, dataDocument);
        } catch (IOException | JsonParseException | MongoException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        return true;
    }

    public UpdateResult updateDocument(final MongoCollectionTypes type, final String id, final String indexName,
            final Object indexValue) throws Exception {

        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        final Bson filter = new Document(Messages.getString("DatabaseConnection._id"), new ObjectId(id));
        final Bson newValue = new Document(indexName, indexValue);
        final Bson updateOperationDocument = new Document("$set", newValue);

        UpdateResult updateResult = null;
        try {
            updateResult = collectionToWorkOn.updateOne(filter, updateOperationDocument);
        } catch (final Exception err) {
            throw new Exception(err.getMessage());
        }

        return updateResult;
    }
}
