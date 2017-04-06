package org.openape.server.database.mongoDB;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.bson.Document;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.json.JsonParseException;
import org.bson.types.ObjectId;
import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.server.MongoConfig;
import org.openape.server.requestHandler.EnvironmentContextRequestHandler;
import org.openape.server.requestHandler.EquipmentContextRequestHandler;
import org.openape.server.requestHandler.TaskContextRequestHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class DatabaseConnection {
    /**
     * The url to our mongo database server.
     */
    private static String DATABASEURL = Messages
            .getString("DatabaseConnection.MongoDBServerAddress"); //$NON-NLS-1$
    /**
     * The standard port for online mongo databases.
     */
    private static String DATABASEPORT = Messages.getString("DatabaseConnection.MongoDBServerPort"); //$NON-NLS-1$

    /**
     * The name of the mongo database holding the relevant data for this
     * application.
     */
    private static String DATABASENAME = Messages
            .getString("DatabaseConnection.MongoDBDatabaseName"); //$NON-NLS-1$

    /**
     * The user name used by this application to connect to the mongo database.
     */
    private static String DATABASEUSERNAME = Messages
            .getString("DatabaseConnection.MongoDBDatabaseUsername"); //$NON-NLS-1$

    /**
     * The password used by this application to connect to the mongo database.
     */
    private static String DATABASEPASSWORD = Messages
            .getString("DatabaseConnection.MongoDBDatabaseUserPassword"); //$NON-NLS-1$

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
     * Database collection holding the mime types of the stored resources.
     */
    private MongoCollection<Document> resourceMimeTypesCollection;
    /**
     * Database collection holding the users.
     */
    private MongoCollection<Document> userCollection;

    /**
     * private constructor to create the singleton database connection instance.
     */
    private DatabaseConnection() {
        // import configuration file
        final String name = MongoConfig.getString("databaseName");//$NON-NLS-1$
        if (name != null && !name.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASENAME = name;
        } else {
            DatabaseConnection.DATABASENAME = Messages
                    .getString("DatabaseConnection.MongoDBDatabaseName"); //$NON-NLS-1$
        }
        final String address = MongoConfig.getString("databaseURL");//$NON-NLS-1$
        if (address != null
                && !address.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEURL = address;
        } else {
            DatabaseConnection.DATABASEURL = Messages
                    .getString("DatabaseConnection.MongoDBServerAddress"); //$NON-NLS-1$
        }
        final String port = MongoConfig.getString("databasePort");//$NON-NLS-1$
        if (port != null && !port.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEPORT = port;
        } else {
            DatabaseConnection.DATABASEPORT = Messages
                    .getString("DatabaseConnection.MongoDBServerPort"); //$NON-NLS-1$
        }
        final String password = MongoConfig.getString("databasePassword");//$NON-NLS-1$
        if (password != null
                && !password.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEPASSWORD = password;
        } else {
            DatabaseConnection.DATABASEPASSWORD = Messages
                    .getString("DatabaseConnection.MongoDBDatabaseUserPassword"); //$NON-NLS-1$
        }
        final String userName = MongoConfig.getString("databaseUsername");//$NON-NLS-1$
        if (userName != null
                && !userName.equals(Messages.getString("DatabaseConnection.EmptyString"))) {//$NON-NLS-1$
            DatabaseConnection.DATABASEUSERNAME = userName;
        } else {
            DatabaseConnection.DATABASEUSERNAME = Messages
                    .getString("DatabaseConnection.MongoDBDatabaseUsername"); //$NON-NLS-1$
        }
        // Create credentials for the openAPE database
        final MongoCredential credential = MongoCredential.createCredential(
                DatabaseConnection.DATABASEUSERNAME, DatabaseConnection.DATABASENAME,
                DatabaseConnection.DATABASEPASSWORD.toCharArray());

        // Create database client for the openAPE database
        this.mongoClient = new MongoClient(new ServerAddress(DatabaseConnection.DATABASEURL,
                Integer.parseInt(DatabaseConnection.DATABASEPORT)), Arrays.asList(credential));

        // Get a reference to the openAPE database.
        this.database = this.mongoClient.getDatabase(DatabaseConnection.DATABASENAME);
        // Get references to the database collections.
        this.userContextCollection = this.database.getCollection(MongoCollectionTypes.USERCONTEXT
                .toString());
        this.environmentContextCollection = this.database
                .getCollection(MongoCollectionTypes.ENVIRONMENTCONTEXT.toString());
        this.equipmentContextCollection = this.database
                .getCollection(MongoCollectionTypes.EQUIPMENTCONTEXT.toString());
        this.taskContextCollection = this.database.getCollection(MongoCollectionTypes.TASKCONTEXT
                .toString());
        this.resourceDescriptionContectCollection = this.database
                .getCollection(MongoCollectionTypes.RESOURCEDESCRIPTION.toString());
        this.listingContextCollection = this.database.getCollection(MongoCollectionTypes.LISTING
                .toString());
        this.resourceMimeTypesCollection = this.database
                .getCollection(MongoCollectionTypes.RESOURCEMIMETYPES.toString());
        this.userCollection = this.database
                .getCollection(MongoCollectionTypes.USERS.toString());

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
     * Delete a mime type of a stored resource.
     *
     * @param fileName
     *            file name of the resource, used as id.
     * @return true if successful of false if the object is not found.
     * @throws IOException
     *             if a database problem occurs.
     */
    public boolean deleteMimeType(String fileName) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this
                .getCollectionByType(MongoCollectionTypes.RESOURCEMIMETYPES);

        // Create search query.
        final BasicDBObject query = new BasicDBObject();
        query.put(Messages.getString("DatabaseConnection._id"), fileName); //$NON-NLS-1$

        // deleted will be null if no data with the given id is found.
        final Document deleted = collectionToWorkOn.findOneAndDelete(query);
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
        } else if (type.equals(MongoCollectionTypes.RESOURCEDESCRIPTION)) {
            return this.resourceDescriptionContectCollection;
        } else if (type.equals(MongoCollectionTypes.LISTING)) {
            return this.listingContextCollection;
        } else if (type.equals(MongoCollectionTypes.RESOURCEMIMETYPES)) {
            return this.resourceMimeTypesCollection;
        } else if (type.equals(MongoCollectionTypes.USERS)) {
            return this.userCollection;
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
        final MongoCollection<Document> collectionToWorkOn = this.getCollectionByType(type);

        // Search for object in database.
        final BasicDBObject query = new BasicDBObject();
        query.put(Messages.getString("DatabaseConnection._id"), new ObjectId(id)); //$NON-NLS-1$
        final FindIterable<Document> resultIteratable = collectionToWorkOn.find(query);

        final Iterator<Document> resultInterator = resultIteratable.iterator();
        if (!resultInterator.hasNext()) {
            // If no result is found return null.
            return null;
        } else {
            // get the first result. Souldn't ever be more than one since _ids
            // are supposed to be unique.
            final Document resultDocument = resultInterator.next();

            DatabaseObject result = null;
            try {
                // Remove the automatically added id.
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
            return result;
        }
    }

    /**
     * Get stored mime type of a resource stored in the file system.
     *
     * @param fileName
     *            of the resource, used as id.
     * @return mime type as string or null if none is found.
     * @throws IOException
     *             if an error arouses.
     */
    public String getMimeType(String fileName) throws IOException {
        final MongoCollection<Document> collectionToWorkOn = this
                .getCollectionByType(MongoCollectionTypes.RESOURCEMIMETYPES);

        // Search for object in database.
        final BasicDBObject query = new BasicDBObject();
        query.put(Messages.getString("DatabaseConnection._id"), fileName); //$NON-NLS-1$
        final FindIterable<Document> resultIteratable = collectionToWorkOn.find(query);

        final Iterator<Document> resultInterator = resultIteratable.iterator();
        if (!resultInterator.hasNext()) {
            // If no result is found return null.
            return null;
        } else {
            // get the first result. Souldn't ever be more than one since _ids
            // are supposed to be unique.
            final Document resultDocument = resultInterator.next();
            String mimetype = null;
            try {
                // Remove the automatically added id.
                resultDocument.remove(Messages.getString("DatabaseConnection._id")); //$NON-NLS-1$
                String jsonResult = resultDocument.toJson();
                // reverse mongo special character replacement.
                jsonResult = this.reverseMongoSpecialCharsReplacement(jsonResult);
                final ObjectMapper mapper = new ObjectMapper();
                final DatabaseObject mimeTypeObject = mapper.readValue(jsonResult,
                        MimeTypeDatabaseObject.class);
                mimetype = ((MimeTypeDatabaseObject) mimeTypeObject).getMimeType();
            } catch (CodecConfigurationException | IOException | JsonParseException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
            return mimetype;
        }
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
        if (jsonToStore.contains(Messages.getString("DatabaseConnection.pointAsciiCode")) || jsonToStore.contains(Messages.getString("DatabaseConnection.$AsciiCode"))) { //$NON-NLS-1$ //$NON-NLS-2$
            throw new IllegalArgumentException(
                    Messages.getString("DatabaseConnection.specialCharReplacementInUseErrorMsg")); //$NON-NLS-1$
        } else if (jsonToStore.contains(Messages.getString("DatabaseConnection.point")) || jsonToStore.contains(Messages.getString("DatabaseConnection.$"))) { //$NON-NLS-1$ //$NON-NLS-2$
            jsonToStore = jsonToStore
                    .replace(
                            Messages.getString("DatabaseConnection.point"), Messages.getString("DatabaseConnection.pointAsciiCode")); //$NON-NLS-1$ //$NON-NLS-2$
            jsonToStore = jsonToStore
                    .replace(
                            Messages.getString("DatabaseConnection.$"), Messages.getString("DatabaseConnection.$AsciiCode")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return jsonToStore;
    }

    /**
     * Replaces '#046' and '#036" with '.' and '$'.
     *
     * @param jsonFromStorage
     * @return The modified string.
     */
    private String reverseMongoSpecialCharsReplacement(String jsonFromStorage)
            throws IllegalArgumentException {
        if (jsonFromStorage.contains(Messages.getString("DatabaseConnection.pointAsciiCode")) || jsonFromStorage.contains(Messages.getString("DatabaseConnection.$AsciiCode"))) { //$NON-NLS-1$ //$NON-NLS-2$
            jsonFromStorage = jsonFromStorage
                    .replace(
                            Messages.getString("DatabaseConnection.pointAsciiCode"), Messages.getString("DatabaseConnection.point")); //$NON-NLS-1$ //$NON-NLS-2$
            jsonFromStorage = jsonFromStorage
                    .replace(
                            Messages.getString("DatabaseConnection.$AsciiCode"), Messages.getString("DatabaseConnection.$")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return jsonFromStorage;
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
            throws ClassCastException, IOException, IllegalArgumentException {
        // Check if data is of the correct type for the collection.
        if (!type.getDocumentType().equals(data.getClass())) {
            throw new ClassCastException(
                    Messages.getString("DatabaseConnection.doctypeErrorMassage") //$NON-NLS-1$
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
     * Used to store a string mime type of a stored resource.
     *
     * @param fileName
     *            name of the file, used as id.
     * @param mimeType
     *            string mime type of the resource with the given name.
     * @return true if successful else a exception will be thrown.
     * @throws IOException
     * @throws IllegalArgumentException
     *             if filename is already in use as a key.
     */
    public boolean storeMimeType(String fileName, String mimeType) throws IOException,
            IllegalArgumentException {
        // check if key is in use.
        if (this.getMimeType(fileName) != null) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.FilenameInUseErrorMassage"));
        }
        final MongoCollection<Document> collectionToWorkOn = this
                .getCollectionByType(MongoCollectionTypes.RESOURCEMIMETYPES);
        // Object representation of the string. Needed for storage.
        final MimeTypeDatabaseObject data = new MimeTypeDatabaseObject(mimeType);
        // Create Document from data.
        Document dataDocument = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(data);
            // Deal with special mongoDB characters '.' and '$'.
            jsonData = this.replaceMongoSpecialChars(jsonData);
            dataDocument = Document.parse(jsonData);
            dataDocument.append(Messages.getString("DatabaseConnection._id"), fileName);//$NON-NLS-1$
            // Insert the document.
            collectionToWorkOn.insertOne(dataDocument);
        } catch (IOException | JsonParseException | MongoException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return true;
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
        // test if data can be found. Throws exceptions or is null if not.
        if (this.getData(type, id) == null) {
            return false;
        }

        // Check if data is of the correct type for the collection.
        if (!type.getDocumentType().equals(data.getClass())) {
            throw new ClassCastException(
                    Messages.getString("DatabaseConnection.doctypeErrorMassage") //$NON-NLS-1$
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
}
