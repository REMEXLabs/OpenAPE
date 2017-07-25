package org.openape.server.requestHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.server.auth.PasswordEncoder;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileHandler {
	static Logger logger = LoggerFactory.getLogger(ProfileHandler.class);
public 	static String createUser(User user) throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            String hashedPassword = PasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            id = databaseconnection.storeData(MongoCollectionTypes.USERS, user);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return id;
    }

public static User getUser(String userName) throws IOException{
	final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	
    final DatabaseObject result = databaseConnection.getByUniqueAttribute(MongoCollectionTypes.USERS, "username", userName );

    // If the result is null no user exists with the givn user name
    if (result == null) {
        logger.info("User with user name \"" + userName + "\" does not exist.");
    	return null;
    }
	
		User user = (User)result; 
	return user;
}

}
