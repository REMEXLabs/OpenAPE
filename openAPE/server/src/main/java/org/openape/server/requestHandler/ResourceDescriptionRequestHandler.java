package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.Property;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceDescription;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.ResourceDescriptionRESTInterface;
import org.openape.server.rest.ResourceRESTInterface;

/**
 * Class with methods to manage resource descriptions on the server. It is used
 * by the rest API {@link ResourceDescriptionRESTInterface} and uses the server
 * database {@link DatabaseConnection}.
 */
public class ResourceDescriptionRequestHandler {

    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.RESOURCEDESCRIPTION;
    private static final String ResourceURIPropertyName = "resource-uri";
    private static final String ResourceURIPattern = "https://res\\.openurc\\.org/api/resources/[a-zA-Z\\d]*";
    private static final String ResourceURIPatternPrefix = "https://res\\.openurc\\.org/api/resources/";
    private static final String WrongURIFormatMsg = "The property with the name 'resource-uri'"
            + " has to have a value with the pattern 'https://res.openurc.org/api/resources/<resourceID>'";
    private static final String NoResoruceWithThatID = "There is no resource for your resource"
            + " description. Your given resource id was: ";
    private static final String PropertyMissing = "Resource description must contain a property"
            + " with the name 'resource-uri' and a value with the pattern"
            + " 'https://res.openurc.org/api/resources/<resourceID>'";

    /**
     * Method to store a new resource description into the server. It is used by
     * the rest API {@link ResourceDescriptionRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     *
     * @param resourceDescription
     *            to be stored.
     * @return the ID of the stored resource description.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete environment context.
     */
    public String createResourceDescription(ResourceDescription resourceDescription)
            throws IOException, IllegalArgumentException {
        // Check if the resource description has a valid resource.
        this.hasDescriptionResource(resourceDescription);
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseConnection.storeData(ResourceDescriptionRequestHandler.COLLECTIONTOUSE,
                    resourceDescription);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Method to delete an existing resource description from the server. It is
     * used by the rest API {@link ResourceDescriptionRESTInterface} and uses
     * the server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the resource description to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteResourceDescriptionById(String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteData(
                ResourceDescriptionRequestHandler.COLLECTIONTOUSE, id);
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceDescriptionRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * Method to get an existing listing from the server. It is used by the rest
     * API {@link ResourceRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested listing.
     * @return requested listing.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public Listing getListingById(String id) throws IOException, IllegalArgumentException {
        final ListingRequestHandler listingRequestHandler = new ListingRequestHandler();
        return listingRequestHandler.getListingById(id);
    }

    /**
     * Method to get an existing resource description from the server. It is
     * used by the rest API {@link ResourceDescriptionRESTInterface} and uses
     * the server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the requested environment context.
     * @return requested environment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public ResourceDescription getResourceDescriptionById(String id) throws IOException,
            IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getData(
                ResourceDescriptionRequestHandler.COLLECTIONTOUSE, id);

        // If the result is null the id is not found.
        if (result == null) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceDescriptionRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }

        // convert into correct type.
        ResourceDescription returnObject;
        try {
            returnObject = (ResourceDescription) result;
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnObject;
    }

    /**
     * Checks if a resource with the ID given in the resource description is
     * available.
     *
     * @param resourceDescription
     * @throws IllegalArgumentException
     *             if it has no property with the name resource-uri, it has the
     *             wrong form, or no corresponding resource was found.
     */
    private void hasDescriptionResource(ResourceDescription resourceDescription)
            throws IllegalArgumentException, IOException {
        // Search for a property with the right name to contain the resource ID.
        boolean propertyFound = false;
        for (final Property property : resourceDescription.getPropertys()) {
            if (property.getName()
                    .equals(ResourceDescriptionRequestHandler.ResourceURIPropertyName)) {
                propertyFound = true;
                // If a property with the right name is found check if value has
                // the right pattern.
                if (!Pattern.matches(ResourceDescriptionRequestHandler.ResourceURIPattern,
                        property.getValue())) {
                    throw new IllegalArgumentException(
                            ResourceDescriptionRequestHandler.WrongURIFormatMsg);
                }
                // Check if database contains corresponding resource object.
                final String resourceID = property.getValue().replaceAll(
                        ResourceDescriptionRequestHandler.ResourceURIPatternPrefix, "");
                final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
                if (null == databaseConnection.getData(MongoCollectionTypes.RESOURCEOBJECTS,
                        resourceID)) {
                    throw new IllegalArgumentException(
                            ResourceDescriptionRequestHandler.NoResoruceWithThatID + resourceID);
                }
            }
        }
        if (!propertyFound) {
            throw new IllegalArgumentException(ResourceDescriptionRequestHandler.PropertyMissing);
        }
    }

    /**
     * Deletes all resource descriptions the the given ID in their resource uri
     * property. Should only be called when the resource gets deleted.
     * 
     * @param resourceID
     * @throws IOException
     *             if database errors occur.
     */
    public static void deleteAllDescriptionsOfAResource(String resourceID) throws IOException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        //Get all descriptions.
        List<DatabaseObject> resultList = databaseConnection.getAllObjectsOfType(MongoCollectionTypes.RESOURCEDESCRIPTION);
        
    }

    /**
     * Method to update an existing resource description on the server. It is
     * used by the rest API {@link ResourceDescriptionRESTInterface} and uses
     * the server database {@link DatabaseConnection}.
     *
     * @param id
     *            the ID of the resource description to update.
     * @param resourceDescription
     *            new version of the resource description.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id, not assigned or the environment
     *             context is not valid.
     */
    public boolean updateResourceDescriptionById(String id, ResourceDescription resourceDescription)
            throws IOException, IllegalArgumentException {
        // Check if the resource description has a valid resource.
        this.hasDescriptionResource(resourceDescription);
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Update data. If a class cast exception occurs or the return value is
        // false the parameters is not valid and an illegal argument exception
        // is thrown. IO exceptions are thrown through.
        boolean success;
        try {
            success = databaseConnection.updateData(
                    ResourceDescriptionRequestHandler.COLLECTIONTOUSE, resourceDescription, id);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (!success) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceDescriptionRequestHandler.NoObjectWithThatIDErrorMsg")); //$NON-NLS-1$
        }
        return true;
    }
}
