package org.openape.api.contexts;

import java.io.IOException;

import org.openape.api.databaseObjectBase.DatabaseObject;

public abstract class AbstractContext extends
DatabaseObject{
public abstract String getFrontendJson() throws IOException;
}
