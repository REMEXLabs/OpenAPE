package org.openape.server.requestHandler;

import org.openape.api.UserContextList;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBObject;

/**
 * Class with methods to manage user contexts on the server. It is used by the
 * rest API {@link UserContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
/**
 * @author Lukas
 *
 */
public class UserContextRequestHandler extends ContextRequestHandler<UserContext, UserContextList> {
    static Logger logger = LoggerFactory.getLogger(UserContextRequestHandler.class);


    public static UserContextRequestHandler  instance = null;
    private UserContextRequestHandler() {
super(    MongoCollectionTypes.USERCONTEXT);    	
    }
    
    public static UserContextRequestHandler getInstance() {
    	if (instance == null){
    instance = new UserContextRequestHandler();
    	} 
    		return instance;
    	
    }
}