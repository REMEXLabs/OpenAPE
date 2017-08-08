package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    public String createResourceDescription(final ResourceDescription resourceDescription)
            throws IOException, IllegalArgumentException {
        // Check if the resource description has a valid resource.
        this.hasDescriptionResource(resourceDescription);
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        // try to store data. Class cast exceptions will be thrown as illegal
        // argument exceptions. IO exceptions will just be thrown through.
        String id = null;
        try {
            id = databaseConnection.storeData(ResourceDescriptionRequestHandler.COLLECTIONTOUSE, resourceDescription);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

    /**
     * Deletes all resource descriptions the the given ID in their resource uri
     * property. Should only be called when the resource gets deleted.
     *
     * @param resourceID
     * @throws IOException
     *             if database errors occur.
     */
    public void deleteAllDescriptionsOfAResource(final String resourceID) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        // Get all descriptions.
        final Map<String, DatabaseObject> resultMap = databaseConnection
                .getAllObjectsOfType(MongoCollectionTypes.RESOURCEDESCRIPTION);
        final Set<String> descriptionIDs = resultMap.keySet();
        // Cast.
        final Map<String, ResourceDescription> descriptions = new HashMap<String, ResourceDescription>();
        try {
            for (final String descriptionID : descriptionIDs) {
                descriptions.put(descriptionID, (ResourceDescription) resultMap.get(descriptionID));
            }
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }
        // Get all descriptions belonging to the resource.
        for (final String descriptionID : descriptionIDs) {
            final ResourceDescription description = descriptions.get(descriptionID);
            final String resourceCompare = this.getResourceID(description);
            if (resourceCompare.equals(resourceID)) {
                // If resource id fitts, delete the description
                databaseConnection.deleteData(MongoCollectionTypes.RESOURCEDESCRIPTION, descriptionID);
            }
        }
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
    public boolean deleteResourceDescriptionById(final String id) throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final boolean success = databaseConnection.deleteData(ResourceDescriptionRequestHandler.COLLECTIONTOUSE, id);
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
    public Listing getListingById(final String id) throws IOException, IllegalArgumentException {
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
    public ResourceDescription getResourceDescriptionById(final String id)
            throws IOException, IllegalArgumentException {
        // get database connection.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Get the requested data.
        final DatabaseObject result = databaseConnection.getData(ResourceDescriptionRequestHandler.COLLECTIONTOUSE, id);

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
     * @param resourceDescription
     * @return the id of the resource mentioned in the properties of the given
     *         resource description.
     * @throws IllegalArgumentException
     *             if it has no property with the name resource-uri or it has
     *             the wrong form.
     */
    private String getResourceID(final ResourceDescription resourceDescription) throws IllegalArgumentException {
        // Search for a property with the right name to contain the resource ID.
        boolean propertyFound = false;
        String resourceID = null;
        for (final Property property : resourceDescription.getPropertys()) {
            if (property.getName().equals(ResourceDescriptionRequestHandler.ResourceURIPropertyName)) {
                propertyFound = true;
                // If a property with the right name is found check if value has
                // the right pattern.
                if (!Pattern.matches(ResourceDescriptionRequestHandler.ResourceURIPattern, property.getValue())) {
                    throw new IllegalArgumentException(ResourceDescriptionRequestHandler.WrongURIFormatMsg);
                }
                resourceID = property.getValue().replaceAll(ResourceDescriptionRequestHandler.ResourceURIPatternPrefix,
                        "");
            }
        }
        if (!propertyFound) {
            throw new IllegalArgumentException(ResourceDescriptionRequestHandler.PropertyMissing);
        }
        return resourceID;
    }

    /**
     * Checks if a resource with the ID given in the resource description is
     * available.
     *
     * @param resourceDescription
     * @throws IllegalArgumentException
     *             no corresponding resource was found.
     * @throws IOException
     *             if database error occurs.
     */
    private void hasDescriptionResource(final ResourceDescription resourceDescription)
            throws IllegalArgumentException, IOException {
        final String resourceID = this.getResourceID(resourceDescription);
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        if (null == databaseConnection.getData(MongoCollectionTypes.RESOURCEOBJECTS, resourceID)) {
            throw new IllegalArgumentException(ResourceDescriptionRequestHandler.NoResoruceWithThatID + resourceID);
        }
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
    public boolean updateResourceDescriptionById(final String id, final ResourceDescription resourceDescription)
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
            success = databaseConnection.updateData(ResourceDescriptionRequestHandler.COLLECTIONTOUSE,
                    resourceDescription, id);
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
