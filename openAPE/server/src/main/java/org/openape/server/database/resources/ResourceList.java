package org.openape.server.database.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.openape.api.Messages;
import org.openape.api.group.GroupAccessRights;
import org.openape.api.resourceDescription.ResourceObject;
import org.openape.api.user.User;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.ResourceAuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.requestHandler.ResourceRequestHandler;
import org.openape.server.rest.ResourceRESTInterface;
import org.pac4j.core.profile.CommonProfile;

/**
 * {@link ResourceList} is a singleton class that contains a list holding all
 * names of resources stored on the file system. It allows get the List, store
 * resources and load resources. It is used by the
 * {@link ResourceRequestHandler} which is used by the
 * {@link ResourceRESTInterface}. The {@link ResourceRESTInterface} is used by
 * the outside user to store load and delete resources.
 *
 */
public class ResourceList {
    private static final String RESOURCE_DOES_NOT_EXIST_MSG = "Resource does not exist.";
    private static final String RESOURCEFOLDERPATH = Messages.getString("ResourceList.rootFolder") + File.separator //$NON-NLS-1$
            + Messages.getString("ResourceList.ResourceFolder"); //$NON-NLS-1$
    private static final String FILE_NAME_AND_OWNER_ID_CANNOT_BE_UPDATED = "The file name and / or the owner id of a resource cannot be updated!";
    
    /**
     * Singleton instance of this class.
     */
    private static ResourceList resourceListInstance;

    /**
     * Get the singleton database connection.
     *
     * @return the database connection.
     * @throws IOException
     *             if unable to create resource folder.
     */
    public static ResourceList getInstance() throws IOException {
        if (ResourceList.resourceListInstance == null) {
            ResourceList.resourceListInstance = new ResourceList();
        }
        return ResourceList.resourceListInstance;
    }

    // List containing all resources stored on the file system.
    private final List<String> resourceNameList = new LinkedList<String>();

    /**
     * Private constructor, filling the resourceList with the filenames of the
     * resources already in the resource directory.
     *
     * @throws IOException
     *             if unable to create resource folder.
     */
    private ResourceList() throws IOException {
        // Add all filenames of resources in the resource folder to resource
        // list.
        final File folder = new File(ResourceList.RESOURCEFOLDERPATH);
        createFolderIfNotExistend(folder);
        final File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            // There can be directories that would be listed, too. Therefore
            // check if file.
            if (listOfFiles[i].isFile()) {
                this.resourceNameList.add(listOfFiles[i].getName());
            }
            // Iterate through user folders and add files to list.
            if (listOfFiles[i].isDirectory()) {
                final File[] listOfUserFiles = listOfFiles[i].listFiles();
                for (int j = 0; j < listOfUserFiles.length; j++) {
                    if (listOfUserFiles[j].isFile()) {
                        // Add 'user id / file name' to list.
                        this.resourceNameList.add(listOfFiles[i].getName() + File.separator
                                + listOfUserFiles[j].getName());
                    }
                }
            }
        }
    }

    /**
     * @param folder
     * @throws IOException
     *             if access denied
     */
    private void createFolderIfNotExistend(final File folder) throws IOException {
        final File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            // If directory does not exist, create
            final boolean success = folder.mkdirs();
            if (!success) {
                throw new IOException(
                        Messages.getString("ResourceList.CouldNotCreateResourceFolderErrorMassage")); //$NON-NLS-1$
            }
            return;
        }
    }

    /**
     * Adds resource to file system and resource list.
     *
     * @param resource
     *            received file from the rest interface
     * @param mimeType
     *            mime type of the data to store
     * @param user
     *            owner of the resource
     * @param groupAccessRight
     * @return id.
     * @throws IllegalArgumentException
     *             if the file name is taken or no file is sent.
     * @throws IOException
     *             if a storing error occurs.
     */
    public String addResource(final FileItem resource, final String mimeType, final User user,
            GroupAccessRights groupAccessRights) throws IllegalArgumentException, IOException {
        final String fileName = resource.getName();

        // Check if filename exists.
        if (fileName == null) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.NoFileNameErrorMassage")); //$NON-NLS-1$
        }
        // Create resource reference object for the database.
        final ResourceObject resourceObject = new ResourceObject(fileName, user.getId(), mimeType,
                groupAccessRights);
        // set owner.
        resourceObject.setOwnerId(user.getId());
        // store database resource object
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String id = null;
        try {
            id = databaseConnection.storeDatabaseObject(MongoCollectionTypes.RESOURCEOBJECTS, resourceObject);
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }
        // Add id to resource object and store again.
        resourceObject.setId(id);
        try {
            databaseConnection.updateDatabaseObject(MongoCollectionTypes.RESOURCEOBJECTS, resourceObject, id);
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }

        // store file on disk.
        final OutputStream out = null;
        final InputStream filecontent = null;

        try {
            // Check if file already exists.
            if (this.resourceExists(resourceObject)) {
                throw new IllegalArgumentException(
                        Messages.getString("ResourceList.FilenameInUseErrorMassage")); //$NON-NLS-1$
            }

            // Specify where to store the file.
            final File folder = new File(resourceObject.getFolder());
            this.createFolderIfNotExistend(folder);
            final File fileToWrite = new File(resourceObject.getPath());

            // Read file content and write it into resource file.
            resource.write(fileToWrite);

        } catch (final FileNotFoundException fne) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.NoUploadFileErrorMassage")); //$NON-NLS-1$
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            // try to close streams.
            try {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            } catch (final IOException e) {
                System.err.println(Messages
                        .getString("ResourceList.StreamsCouldNotBeClousedErrorMassage")); //$NON-NLS-1$
            }

        }
        this.resourceNameList.add(resourceObject.getOwnerId() + File.separator + fileName);
        return id;
    }

    /**
     * If found the method deletes the resource with the given name.
     *
     * @param id
     *            id of the file.
     * @param profile
     *            of the user who requests to delete the resource.
     * @return true if successful.
     * @throws IllegalArgumentException
     *             if the file is not found.
     * @throws IOException
     */
    public boolean deleteResource(final String id, final CommonProfile profile)
            throws IllegalArgumentException, IOException, UnauthorizedException {
        // get corresponding resource reference object.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        ResourceObject resourceObject = null;
        try {
            resourceObject = (ResourceObject) databaseConnection.getDatabaseObjectById(
                    MongoCollectionTypes.RESOURCEOBJECTS, id);
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }
        if (resourceObject == null) {
            throw new IllegalArgumentException(RESOURCE_DOES_NOT_EXIST_MSG);
        }

        // Check if user is allowed to delete the resource
        final AuthService auth = new AuthService();
        auth.allowAdminAndOwner(profile, resourceObject.getOwnerId());

        if (this.resourceExists(resourceObject)) {
            new File(resourceObject.getPath()).delete();
            this.resourceNameList.remove(resourceObject.getOwnerId() + File.separator
                    + resourceObject.getFileName());
            // delete reference object from database.
            databaseConnection.deleteDatabaseObject(MongoCollectionTypes.RESOURCEOBJECTS, id);
        } else {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.FileNotFoundErrorMassage")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * Updates an existing resource on the server. Note that the resource's id cannot be changed and that the file,
     * which is associated with the resource will not be updated. Thus you should not try to update the file
     * itself, its name or owner id with this method!
     * 
     * @param resourceObjectUpdate
     *            the resource which contains the updates.
     * @param profile
     *            of the user who requests to update the resource.
     * @return true if the resource was updated successfully.
     * @throws IllegalArgumentException
     *             if there exists no resource with the given id.
     * @throws IOException
     *             if a problem with the database occurs.
     * @throws UnauthorizedException
     *             if the user has no right to update the resource.
     */
    public boolean updateResource(final ResourceObject resourceObjectUpdate, final CommonProfile profile)
            throws IllegalArgumentException, IOException, UnauthorizedException {
        // get corresponding resource
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        ResourceObject resourceObjectOld = null;
        try {
            resourceObjectOld = (ResourceObject) databaseConnection
                    .getDatabaseObjectById(MongoCollectionTypes.RESOURCEOBJECTS, resourceObjectUpdate.getId());
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }

        // check if resource exist
        if (resourceObjectOld == null) {
            throw new IllegalArgumentException(RESOURCE_DOES_NOT_EXIST_MSG);
        }

        // this can be removed, when also the file and its path are updated by this method
        if (resourceObjectOld.getId() != resourceObjectUpdate.getId()
                || !resourceObjectOld.getFileName().equals(resourceObjectUpdate.getFileName())) {
            throw new IllegalArgumentException(FILE_NAME_AND_OWNER_ID_CANNOT_BE_UPDATED);
        }

        // Check if user is allowed to update the resource
        final ResourceAuthService resourceAuthService = new ResourceAuthService();
        resourceAuthService.allowAdminAndOwner(profile, resourceObjectUpdate.getOwnerId());

        // update resource
        databaseConnection.updateDatabaseObject(MongoCollectionTypes.RESOURCEOBJECTS, resourceObjectUpdate,
                resourceObjectUpdate.getId());

        return true;
    }

    /**
     * @return the list containing all names of resources stored on the file
     *         system.
     */
    public List<String> getResourceNameList() {
        return this.resourceNameList;
    }

    /**
     * Returns resource file of the given name.
     *
     * @param id
     * @return resource file of the given name.
     * @throws IllegalArgumentException
     *             if file is non existent.
     */
    public GetResourceReturnType getResoureFile(final String id) throws IllegalArgumentException,
            IOException {
        // get resource description object.
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        ResourceObject resourceObject = null;
        try {
            resourceObject = (ResourceObject) databaseConnection.getDatabaseObjectById(
                    MongoCollectionTypes.RESOURCEOBJECTS, id);
        } catch (final ClassCastException e) {
            throw new IOException(e.getMessage());
        }
        if (this.resourceExists(resourceObject)) {
            final File file = new File(resourceObject.getPath());
            return new GetResourceReturnType(file, resourceObject);
        } else {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.FileNotFoundErrorMassage")); //$NON-NLS-1$
        }

    }

    /**
     * Checks if resource is available on the file system.
     *
     * @param resourceObject
     *            reference object, database store able.
     * @return true if file is false false if not.
     */
    private boolean resourceExists(final ResourceObject resourceObject) {
        final File file = new File(resourceObject.getPath());
        if (file.exists() && !file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }
}
