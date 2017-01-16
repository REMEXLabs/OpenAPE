package org.openape.server.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Part;

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
    private static final String RESOURCEFOLDERPATH = "src" + File.separator + "resources";

    // List containing all resources stored on the file system.
    private List<String> resourceNameList = new LinkedList<String>();

    /**
     * @return the list containing all names of resources stored on the file
     *         system.
     */
    public List<String> getResourceNameList() {
        return resourceNameList;
    }

    /**
     * Singleton instance of this class.
     */
    private static ResourceList resourceListInstance;

    /**
     * Get the singleton database connection.
     *
     * @return the database connection.
     */
    public static ResourceList getInstance() {
        if (ResourceList.resourceListInstance == null) {
            ResourceList.resourceListInstance = new ResourceList();
        }
        return ResourceList.resourceListInstance;
    }

    /**
     * Private constructor, filling the resourceList with the filenames of the
     * resources already in the resource directory.
     */
    private ResourceList() {
        // Add all filenames of resources in the resource folder to resource
        // list.
        File folder = new File(RESOURCEFOLDERPATH);
        File[] listOfFiles = folder.listFiles();
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
        final String fileName = getFileName(filePart);

        // Check if filename exists.
        if (fileName == null) {
            throw new IllegalArgumentException("Filename could not be determined.");
        }

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            // Specify where to store the file.
            File fileToWrite = new File(RESOURCEFOLDERPATH + File.separator + fileName);

            // Check if file already exists.
            if (fileToWrite.exists() && !fileToWrite.isDirectory()) {
                throw new IllegalArgumentException("Filename is in use.");
            }

            // Read file content and write it inot resource file.
            out = new FileOutputStream(fileToWrite);
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (FileNotFoundException fne) {
            throw new IllegalArgumentException("You did not specify a file to upload.");
        } finally {
            // try to close streams.
            try {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            } catch (IOException e) {
                System.err.println("Resource file creation streams could not be closed.");
            }

        }
        resourceNameList.add(fileName);
        return fileName;
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
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
