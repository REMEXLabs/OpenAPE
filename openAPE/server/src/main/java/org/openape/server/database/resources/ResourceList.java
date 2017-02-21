package org.openape.server.database.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Part;

import org.openape.api.Messages;
import org.openape.server.requestHandler.ResourceRequestHandler;
import org.openape.server.rest.ResourceRESTInterface;

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
    private static final String RESOURCEFOLDERPATH = Messages.getString("ResourceList.rootFolder") + File.separator + Messages.getString("ResourceList.ResourceFolder"); //$NON-NLS-1$ //$NON-NLS-2$

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
    private List<String> resourceNameList = new LinkedList<String>();

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
        final File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            // If directory does not exist, create
            final boolean success = (new File(ResourceList.RESOURCEFOLDERPATH)).mkdirs();
            if (!success) {
                throw new IOException(
                        Messages.getString("ResourceList.CouldNotCreateResourceFolderErrorMassage")); //$NON-NLS-1$
            }
            return;
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            // There can be directories that would be listed, too. Therefore
            // check if file.
            if (listOfFiles[i].isFile()) {
                this.resourceNameList.add(listOfFiles[i].getName());
            }
        }
    }

    /**
     * Adds resource to file system and resource list.
     *
     * @param resource
     *            received file from the rest interface
     * @return filename.
     * @throws IllegalArgumentException
     *             if the file name is taken or no file is sent.
     * @throws IOException
     *             if a storing error occurs.
     */
    public String addResource(Part resource) throws IllegalArgumentException, IOException {
        final Part filePart = resource;
        final String fileName = this.getFileName(filePart);

        // Check if filename exists.
        if (fileName == null) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.NoFileNameErrorMassage")); //$NON-NLS-1$
        }

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            // Specify where to store the file.
            final File fileToWrite = new File(ResourceList.RESOURCEFOLDERPATH + File.separator
                    + fileName);

            // Check if file already exists.
            if (!this.resourceExists(fileName)) {
                throw new IllegalArgumentException(
                        Messages.getString("ResourceList.FilenameInUseErrorMassage")); //$NON-NLS-1$
            }

            // Read file content and write it inot resource file.
            out = new FileOutputStream(fileToWrite);
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();

        } catch (final FileNotFoundException fne) {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.NoUploadFileErrorMassage")); //$NON-NLS-1$
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
        this.resourceNameList.add(fileName);
        return fileName;
    }

    /**
     * If found the method deletes the resource with the given name.
     *
     * @param fileName
     *            name of the resource including file ending.
     * @return true if successful.
     * @throws IllegalArgumentException
     *             if the file is not found.
     * @throws IOException
     */
    public boolean deleteResource(String fileName) throws IllegalArgumentException, IOException {
        if (this.resourceExists(fileName)) {
            new File(ResourceList.RESOURCEFOLDERPATH + File.separator + fileName).delete();
            this.resourceNameList.remove(fileName);
        } else {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.FileNotFoundErrorMassage")); //$NON-NLS-1$
        }
        return true;

    }

    /**
     * Returns string file name of a {@link Part} or null if no file header or
     * file name is fund.
     *
     * @param part
     *            to get the filename from.
     * @return string file name of a {@link Part} or null if no file header or
     *         file name is fund.
     */
    private String getFileName(final Part part) {
        for (final String content : part
                .getHeader(Messages.getString("ResourceList.content-distribution")).split(Messages.getString("ResourceList.spitter"))) { //$NON-NLS-1$ //$NON-NLS-2$
            if (content.trim().startsWith(Messages.getString("ResourceList.filename"))) { //$NON-NLS-1$
                return content
                        .substring(content.indexOf('=') + 1)
                        .trim()
                        .replace(
                                Messages.getString("ResourceList.invertedCommas"), Messages.getString("ResourceList.EmptyString")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return null;
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
     * @param fileName
     * @return resource file of the given name.
     * @throws IllegalArgumentException
     *             if file is non existent.
     */
    public File getResoureFile(String fileName) throws IllegalArgumentException {
        if (this.resourceExists(fileName)) {
            return new File(ResourceList.RESOURCEFOLDERPATH + File.separator + fileName);
        } else {
            throw new IllegalArgumentException(
                    Messages.getString("ResourceList.FileNotFoundErrorMassage")); //$NON-NLS-1$
        }
    }

    /**
     * Checks if resource is available on the file system.
     *
     * @param resourceName
     *            name of the resource containing the file ending.
     * @return true if file is false false if not.
     */
    private boolean resourceExists(final String resourceName) {
        final File file = new File(ResourceList.RESOURCEFOLDERPATH + File.separator + resourceName);
        if (file.exists() && !file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }
}
