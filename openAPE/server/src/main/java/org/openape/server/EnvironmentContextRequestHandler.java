package org.openape.server;

import java.io.IOException;

import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.server.database.DatabaseConnection;
import org.openape.server.rest.EnvironmentContextRESTInterface;

/**
 * Class with methods to manage environment context on the server. It is used by the
 * rest API {@link EnvironmentContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class EnvironmentContextRequestHandler {

    /**
     * Method to store a new environment context into the server. It is used by the
     * rest API {@link EnvironmentContextRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     * 
     * @param environmentContext
     *            to be stored.
     * @return the ID of the stored environment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the parameter is not a complete environment context.
     */
    public String createEnvironmentContext(Object environmentContext) throws IOException,
            IllegalArgumentException {
        return null;
    }

    /**
     * Method to get an existing environment context from the server. It is used by the
     * rest API {@link EnvironmentContextRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     * 
     * @param id
     *            the ID of the requested environment context.
     * @return requested environment context.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public EnvironmentContext getEnvironmentContextById(String id) throws IOException,
            IllegalArgumentException {
        return null;
    }

    /**
     * Method to update an existing environment context on the server. It is used by
     * the rest API {@link EnvironmentContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     * 
     * @param id
     *            the ID of the environment context to update.
     * @param environmentContext
     *            new version of the environment context.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id, not assigned or the environment context is
     *             not valid.
     */
    public boolean updateEnvironmentContextById(String id, Object environmentContext)
            throws IOException, IllegalArgumentException {
        return false;
    }

    /**
     * Method to delete an existing environment context from the server. It is used by
     * the rest API {@link EnvironmentContextRESTInterface} and uses the server
     * database {@link DatabaseConnection}.
     * 
     * @param id
     *            the ID of the environment context to delete.
     * @return true if successful. Else an exception is thrown.
     * @throws IOException
     *             if a storage problem still occurs, after to many tries.
     * @throws IllegalArgumentException
     *             if the id is no valid id or not assigned.
     */
    public boolean deleteEnvironmentContextById(String id) throws IOException,
            IllegalArgumentException {
        return false;
    }
}