package org.openape.api;

import java.io.Serializable;

import com.mongodb.BasicDBObject;

/**
 * Abstract class used to identify object types that can be stored in the
 * database.
 */
public abstract class DatabaseObject implements Serializable {
    private static final long serialVersionUID = -404247926481623440L;
}
