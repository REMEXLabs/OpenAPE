package org.openape.server.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.openape.api.DatabaseObject;
import org.openape.api.Resource;
import org.openape.api.user.User;
import org.openape.server.api.group.Group;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import spark.Request;
import spark.Response;

import com.mongodb.BasicDBObject;

/**
 * This class is the authentication service for resources. It checks if the
 * logged in user is allowed to do an action with an resource or not. Therefore
 * it checks, whether the logged in user is an OpenAPE admin, the owner of the
 * resource or a member of a group, which has the needed right.
 *
 * @author Tobias Ableitner
 *
 */
public class ResourceAuthService extends AuthService {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    private static final String ADMIN_ROLE = "admin";

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // getters and setters
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // abstract methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // override methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // public methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    private void allow(final Request request, final Response response, final Resource resource,
            final String right) throws UnauthorizedException {
        final User user = this.getAuthenticatedUser(request, response);
        if (user.getRoles().contains(ResourceAuthService.ADMIN_ROLE)) {
            return;
        }
        if (resource.getOwner().equals(user.getId())) {
            return;
        }
        throw new UnauthorizedException("You are not allowed to perform this operation");
    }

    public void allowDeleting(final Request request, final Response response,
            final Resource resource) throws UnauthorizedException {
        this.allow(request, response, resource, "delete");
    }

    public void allowReading(final Request request, final Response response, final Resource resource)
            throws UnauthorizedException {
        this.allow(request, response, resource, "read");
    }

    public void allowRightsChanging(final Request request, final Response response,
            final Resource resource) throws UnauthorizedException {
        this.allow(request, response, resource, "chnageRights");
    }

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // protected methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // private methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    public void allowUpdating(final Request request, final Response response,
            final Resource resource) throws UnauthorizedException {
        this.allow(request, response, resource, "update");
    }

    private Map<String, Group> getGroupsWithUserAsMember(final User user) throws IOException,
            UnauthorizedException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final BasicDBObject elemMatch = new BasicDBObject();
        elemMatch.put("userId", user.getId());
        // TODO remove hard coded state values
        elemMatch.put("state", new Document("$in", Arrays.asList("MEMBER", "ADMIN")));
        final BasicDBObject members = new BasicDBObject();
        members.put("$elemMatch", elemMatch);
        final BasicDBObject query = new BasicDBObject();
        query.put("members", members);

        final Map<String, Group> groups = new HashMap<String, Group>();
        for (final DatabaseObject databaseObject : databaseConnection.getDocumentsByQuery(
                MongoCollectionTypes.GROUPS, query, true)) {
            if (databaseObject instanceof Group) {
                final Group group = (Group) databaseObject;
                groups.put(group.getId(), group);
            }
        }

        return groups;
    }

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

}
