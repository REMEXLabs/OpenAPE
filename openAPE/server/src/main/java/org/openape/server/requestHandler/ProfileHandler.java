package org.openape.server.requestHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.openape.api.DatabaseObject;
import org.openape.api.user.User;
import org.openape.server.auth.PasswordEncoder;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.internal.thread.DaemonThreadFactory;

public class ProfileHandler {
    static Logger logger = LoggerFactory.getLogger(ProfileHandler.class);

    public static String createUser(final User user) throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            final String hashedPassword = PasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            id = databaseconnection.storeData(MongoCollectionTypes.USERS, user);
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

    public static User getUser(final String userName) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final DatabaseObject result = databaseConnection.getByUniqueAttribute(
                MongoCollectionTypes.USERS, "username", userName);

        // If the result is null no user exists with the givn user name
        if (result == null) {
            ProfileHandler.logger.info("User with user name \"" + userName + "\" does not exist.");
            return null;
        }

        final User user = (User) result;
        return user;
    }

    public static void updateUser(final User user) {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        try {
            databaseconnection.updateData(MongoCollectionTypes.USERS, user, user.getId());
        } catch (final ClassCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	public static boolean userExists(String userId) throws IOException {
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		
		User user = (User) databaseConnection.getByUniqueAttribute(MongoCollectionTypes.USERS, "userid", userId);
		if (user == null) {
			return false;
		} else {
			return true;
		}
		
		
		
	}

}
