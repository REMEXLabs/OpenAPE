package org.openape.server;

import java.io.IOException;

import org.openape.api.usercontext.UserContext;
import org.openape.server.database.DatabaseConnection;
import org.openape.server.rest.UserContextRESTInterface;

/**
 * Class with methods
 */
public class UserContextRequestHandler {

    /**
     * Method to store a new user context into the server. It is used by the
     * rest API {@link UserContextRESTInterface} and uses the server database
     * {@link DatabaseConnection}.
     * 
     * @param userContext
     *            to be stored.
     * @return the ID of the stored user context.
     * @throws IOException
     *             if a storage problem occurs-
     * @throws IllegalArgumentException
     *             if the parameter is not a complete user context.
     */
    public String createUserContext(Object userContext) throws IOException,
            IllegalArgumentException {
        return null;
    }

    public UserContext getUserContextById(String id) {
        return null;
    }

    public boolean updateUserContextById(String id, Object useContext) {
        return false;
    }

    public boolean deleteUserContextById(String id) {
        return false;
    }
}
