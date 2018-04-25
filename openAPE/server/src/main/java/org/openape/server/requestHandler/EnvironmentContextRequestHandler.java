package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.EnvironmentContextList;
import org.openape.api.Messages;
import org.openape.api.UserContextList;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.EnvironmentContextRESTInterface;

import com.mongodb.BasicDBObject;

/**
 * Class with methods to manage environment context on the server. It is used by
 * the rest API {@link EnvironmentContextRESTInterface} and uses the server
 * database {@link DatabaseConnection}.
 */
public class EnvironmentContextRequestHandler extends ContextRequestHandler<EnvironmentContext, EnvironmentContextList> {
private EnvironmentContextRequestHandler() {
    super( MongoCollectionTypes.ENVIRONMENTCONTEXT);
}
    private static EnvironmentContextRequestHandler instance = null;
    public static EnvironmentContextRequestHandler getInstance() {
    	if (instance == null) {
    		instance = new EnvironmentContextRequestHandler();
    	}
    	return instance;
    }
    }