package org.openape.server.database.mongoDB;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.api.resourceDescription.ResourceObject;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.api.group.Group;

/**
 * Type of mongoDB collections used for this application within
 * {@link DatabaseConnection}. Contains the names of the collections used by the
 * database and the class of the documents within the collections.
 */
public enum MongoCollectionTypes {
    USERCONTEXT(
            Messages.getString("MongoCollectionTypes.userContextsCollectionName"), UserContext.class), //$NON-NLS-1$

    ENVIRONMENTCONTEXT(

            Messages.getString("MongoCollectionTypes.environmentContextsCollectionName"), EnvironmentContext.class), //$NON-NLS-1$
    EQUIPMENTCONTEXT(
            Messages.getString("MongoCollectionTypes.equipmentContextsCollectionName"), EquipmentContext.class), //$NON-NLS-1$
    TASKCONTEXT(
            Messages.getString("MongoCollectionTypes.taskContextsCollectionName"), TaskContext.class), //$NON-NLS-1$
    RESOURCEDESCRIPTION(
            Messages.getString("MongoCollectionTypes.resourceDescriptionCollectionName"), ResourceDescription.class), //$NON-NLS-1$
    LISTING(Messages.getString("MongoCollectionTypes.lstingCollectionName"), Listing.class), //$NON-NLS-1$
    GROUPS(Messages
            .getString("MongoCollectionTypes.groupMimeTypesCollectionName"), Group.class), //$NON-NLS-1$

    RESOURCEOBJECTS(
            Messages.getString("MongoCollectionTypes.resourceObjectsCollectionName"), ResourceObject.class), //$NON-NLS-1$
    
    USERS("users", User.class);

    /**
     * Get the collection type of a mongo database collection by its name.
     *
     * @param collectionName
     * @return type of the collection.
     */
    public static MongoCollectionTypes getTypeFromCollectionName(final String collectionName) {
        if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.userContextsCollectionName"))) { //$NON-NLS-1$
            return USERCONTEXT;
        } else if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.environmentContextsCollectionName"))) { //$NON-NLS-1$
            return ENVIRONMENTCONTEXT;
        } else if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.equipmentContextsCollectionName"))) { //$NON-NLS-1$
            return EQUIPMENTCONTEXT;
        } else if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.taskContextsCollectionName"))) { //$NON-NLS-1$
            return TASKCONTEXT;
        } else if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.resourceDescriptionCollectionName"))) { //$NON-NLS-1$
            return RESOURCEDESCRIPTION;
        } else if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.lstingCollectionName"))) { //$NON-NLS-1$
            return LISTING;
        } else if (collectionName.endsWith(Messages
                .getString("MongoCollectionTypes.resourceObjectsCollectionName"))) {//$NON-NLS-1$
            return RESOURCEOBJECTS;
        } else if (collectionName.equals("users")) {
            return USERS;
        } else if (collectionName.equals(Messages
                .getString("MongoCollectionTypes.groupMimeTypesCollectionName"))) {
            return GROUPS;
        } else {
            return null;
        }

    }

    private final Class<? extends DatabaseObject> objectType;

    private final String mongoCollectionName;

    /**
     * Constructor to create a mongo collection type, which contains the name of
     * the collection and the data type stored within the collection.
     *
     * @param collectionName
     * @param objectType
     */
    private <T extends DatabaseObject> MongoCollectionTypes(final String collectionName,
            final Class<? extends DatabaseObject> objectType) {
        this.objectType = objectType;
        this.mongoCollectionName = collectionName;
    }

    /**
     * Get the class of documents stored in the collection from the given type.
     * Null for RESOURCEMIMETYPES.
     *
     * @param type
     *            of the collection.
     * @return class of the documents stored.
     */
    public Class<? extends DatabaseObject> getDocumentType() {
        return this.objectType;
    }

    /**
     * Get the collection name used within the database from the type.
     *
     * @param type
     *            of the collection.
     * @return name of the collection used within the database.
     */
    @Override
    public String toString() {
        return this.mongoCollectionName;
    }
}
