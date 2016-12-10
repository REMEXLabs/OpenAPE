package org.openape.server.database;

import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.resource.Resource;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.usercontext.UserContext;

/**
 * Type of mongoDB collections used for this application within
 * {@link DatabaseConnection}. Contains the names of the collections used by the
 * database and the class of the documents within the collections.
 */
public enum MongoCollectionTypesTest {
//    USERCONTEXT("user_contexts", UserContext.class), //
//    ENVIRONMENTCONTEXT("environment_contexts", EnvironmentContext.class), //
//    EQUIPMENTCONTEXT("equipment_contexts", EquipmentContext.class), //
//    TASKCONTEXT("task_contexts", TaskContext.class), //
//    RESOURCEOFFER("resource_offers", Resource.class), //
//    RESOURCEREQUEST("resource_requests", Resource.class); //
//
//    /**
//     * Get the collection type of a mongo database collection by its name.
//     *
//     * @param collectionName
//     * @return type of the collection.
//     */
//    public static MongoCollectionTypesTest getTypeFromCollectionName(String collectionName) {
//        switch (collectionName) {
//        case "user_contexts":
//            return USERCONTEXT;
//        case "environment_contexts":
//            return ENVIRONMENTCONTEXT;
//        case "equipment_contexts":
//            return EQUIPMENTCONTEXT;
//        case "task_contexts":
//            return TASKCONTEXT;
//        case "resource_offers":
//            return RESOURCEOFFER;
//        case "resource_requests":
//            return RESOURCEREQUEST;
//        default:
//            return null;
//        }
//    }
//
//    private final Class<?> objectType;
//
//    private final String mongoCollectionName;
//
//    /**
//     * Constructor to create a mongo collection type, which contains the name of
//     * the collection and the data type stored within the collection.
//     *
//     * @param collectionName
//     * @param objectType
//     */
//    private <T> MongoCollectionTypesTest(String collectionName, Class<T> objectType) {
//        this.objectType = objectType;
//        this.mongoCollectionName = collectionName;
//    }
//
//    /**
//     * Get the class of documents stored in the collection from the given type.
//     *
//     * @param type
//     *            of the collection.
//     * @return class of the documents stored.
//     */
//    public Class<?> getDocumentType() {
//        return this.objectType;
//    }
//
//    /**
//     * Get the collection name used within the database from the type.
//     *
//     * @param type
//     *            of the collection.
//     * @return name of the collection used within the database.
//     */
//    @Override
//    public String toString() {
//        return this.mongoCollectionName;
//    }
}
