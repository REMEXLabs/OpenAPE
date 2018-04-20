package org.openape.server.requestHandler;

import org.openape.api.TaskContextList;
import org.openape.api.taskcontext.TaskContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.TaskContextRESTInterface;

/**
 * Class with methods to manage task context on the server. It is used by the
 * rest API {@link TaskContextRESTInterface} and uses the server database
 * {@link DatabaseConnection}.
 */
public class TaskContextRequestHandler extends ContextRequestHandler<TaskContext, TaskContextList> {

    private static final MongoCollectionTypes COLLECTIONTOUSE = MongoCollectionTypes.TASKCONTEXT;
private static TaskContextRequestHandler instance = null;

public static TaskContextRequestHandler getInstance() {
	if (instance == null) {
		instance = new TaskContextRequestHandler();
	}
	return instance;
}
}