package org.openape.server.requestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openape.api.EnvironmentContextList;
import org.openape.api.EquipmentContextList;
import org.openape.api.Messages;
import org.openape.api.UserContextList;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.rest.EquipmentContextRESTInterface;

import com.mongodb.BasicDBObject;

/**
 * Class with methods to manage equipment context on the server. It is used by
 * the rest API {@link EquipmentContextRESTInterface} and uses the server
 * database {@link DatabaseConnection}.
 */
public class EquipmentContextRequestHandler extends ContextRequestHandler<EquipmentContext,EquipmentContextList> {
private EquipmentContextRequestHandler() {
    super( MongoCollectionTypes.EQUIPMENTCONTEXT);
}
private static EquipmentContextRequestHandler instance = null;

public static EquipmentContextRequestHandler getInstance() {
	if (instance == null) {
		instance = new EquipmentContextRequestHandler();
	}
	return instance;
}
}