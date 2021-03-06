package org.openape.server.requestHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.user.User;
import org.openape.server.auth.PasswordEncoder;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileHandler {
    static Logger logger = LoggerFactory.getLogger(ProfileHandler.class);

    public static String createUser(final User user) throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            final String hashedPassword = PasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            id = databaseconnection.storeDatabaseObject(MongoCollectionTypes.USERS, user);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (final InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return id;
    }

    public static List<User> getAllUsers() throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final List<DatabaseObject> result = databaseConnection.getDatabaseObjectsByQuery(
                MongoCollectionTypes.USERS, null);
        final List<User> listUsers = new ArrayList<User>();
        User user = null;
        for (final DatabaseObject dboEntry : result) {
            user = (User) dboEntry;
            listUsers.add(user);
        }
        return listUsers;
    }

    public static User getUser(final String userName) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final DatabaseObject result = databaseConnection.getDatabaseObjectByUniqueAttribute(
                MongoCollectionTypes.USERS, "username", userName);

        // If the result is null no user exists with the givn user name
        if (result == null) {
            ProfileHandler.logger.info("User with user name \"" + userName + "\" does not exist.");
            return null;
        }

        final User user = (User) result;
        return user;
    }

    public static User getUserById(final String userId) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final DatabaseObject result = databaseConnection.getDatabaseObjectById(
                MongoCollectionTypes.USERS, userId);

        // If the result is null no user exists with the givn user name
        if (result == null) {
            ProfileHandler.logger.info("User with user name \"" + userId + "\" does not exist.");
            return null;
        }

        final User user = (User) result;
        return user;
    }

    public static void updateUser(final User user) {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        try {
            databaseconnection.updateDatabaseObject(MongoCollectionTypes.USERS, user, user.getId());
            //TODO exception handling
        } catch (final ClassCastException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean userExists(final String userId) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final User user = (User) databaseConnection.getDatabaseObjectByUniqueAttribute(
                MongoCollectionTypes.USERS, "userid", userId);
        if (user == null) {
            return false;
        } else {
            return true;
        }

    }

}
