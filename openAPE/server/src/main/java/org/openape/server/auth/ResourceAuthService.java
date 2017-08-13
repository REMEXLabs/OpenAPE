package org.openape.server.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.openape.api.DatabaseObject;
import org.openape.api.Resource;
import org.openape.api.group.GroupAccessRight;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.api.user.User;
import org.openape.server.api.group.Group;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import com.mongodb.BasicDBObject;

import spark.Request;
import spark.Response;

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

    private static final String READ_RIGHT = "readRight";

    private static final String UPDATE_RIGHT = "updateRight";

    private static final String DELETE_RIGHT = "deleteRight";

    private static final String CHANGE_RIGHTS_RIGHT = "changeRightsRight";




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

    private void allow(final Request request, final Response response, final Resource resource, final String right)
            throws UnauthorizedException, IOException {
        final User user = this.getAuthenticatedUser(request, response);
        if (user.getRoles().contains(ResourceAuthService.ADMIN_ROLE)) {
            return;
        }
        if (resource.getOwner().equals(user.getId())) {
            return;
        }
        // TODO get group access rights from resource
        final List<GroupAccessRight> groupAccessRights = new ArrayList<GroupAccessRight>();
        final Map<String, Group> usersGroups = this.getGroupsWithUserAsMember(user);
        for (final GroupAccessRight groupAccessRight : groupAccessRights) {
            if (usersGroups.containsKey(groupAccessRight.getGroupId())) {
                if (right.equals(ResourceAuthService.READ_RIGHT) && groupAccessRight.hasReadRight()) {
                    return;
                } else if (right.equals(ResourceAuthService.UPDATE_RIGHT) && groupAccessRight.hasUpdateRight()) {
                    return;
                } else if (right.equals(ResourceAuthService.DELETE_RIGHT) && groupAccessRight.hasDeleteRight()) {
                    return;
                } else if (right.equals(ResourceAuthService.CHANGE_RIGHTS_RIGHT)
                        && groupAccessRight.hasChangeRightsRight()) {
                    return;
                }
            }
        }
        throw new UnauthorizedException("You are not allowed to perform this operation");
    }

    /**
     * Check whether the logged in user is allowed to delete a resource. Therefore it checks the user's roles, whether he is the owner of the resource or not and the group access rights.
     * @param request the request. It must not be null!
     * @param response the response. It must not be null!
     * @param resource the resource which the user wants to delete. It must not be null!
     * @throws UnauthorizedException if the logged in user is not allowed to delete the resource.
     * @throws IOException if a problem with the database access occurs during the right check. 
     */
    public void allowDeleting(final Request request, final Response response, final Resource resource)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resource, ResourceAuthService.DELETE_RIGHT);
    }

    public void allowReading(final Request request, final Response response, final Resource resource)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resource, ResourceAuthService.READ_RIGHT);
    }

    public void allowRightsChanging(final Request request, final Response response, final Resource resource)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resource, ResourceAuthService.CHANGE_RIGHTS_RIGHT);
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

    public void allowUpdating(final Request request, final Response response, final Resource resource)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resource, ResourceAuthService.UPDATE_RIGHT);
    }

    private Map<String, Group> getGroupsWithUserAsMember(final User user) throws IOException, UnauthorizedException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final BasicDBObject elemMatch = new BasicDBObject();
        elemMatch.put("userId", user.getId());
        elemMatch.put("state",
                new Document("$in", Arrays.asList(GroupMembershipStatus.MEMBER, GroupMembershipStatus.ADMIN)));
        final BasicDBObject members = new BasicDBObject();
        members.put("$elemMatch", elemMatch);
        final BasicDBObject query = new BasicDBObject();
        query.put("members", members);

        final Map<String, Group> groups = new HashMap<String, Group>();
        for (final DatabaseObject databaseObject : databaseConnection.getDocumentsByQuery(MongoCollectionTypes.GROUPS,
                query, true)) {
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
