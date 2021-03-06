package org.openape.server.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.openape.api.databaseObjectBase.DatabaseObject;
import org.openape.api.group.GroupAccessRight;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.api.resourceDescription.ResourceObject;
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
    /**
     * Constant for the role value admin.
     */
    private static final String ADMIN_ROLE = "admin";
    
    /**
     * Constant for the parameter value read right.
     */
    private static final String READ_RIGHT = "readRight";

    /**
     * Constant for the parameter value update right.
     */
    private static final String UPDATE_RIGHT = "updateRight";

    /**
     * Constant for the parameter value delete right.
     */
    private static final String DELETE_RIGHT = "deleteRight";

    /**
     * Constant for the parameter value change rights right.
     */
    private static final String CHANGE_RIGHTS_RIGHT = "changeRightsRight";
    
    /**
     * True if the test mode is enabeld and false if not. If the test mode is enabled the user {@link #testUser} will be
     * used instead of the user, which would be returned by {@link #getAuthenticatedUser(Request, Response)}.
     */
    private boolean testModeEnabled = false;
    
    /**
     * The user, which will be used if the {@link #testMode} is enabled instead of the user, which would be returned by
     * {@link #getAuthenticatedUser(Request, Response)}.
     */
    private User testUser;

    private void allow(final Request request, final Response response, final ResourceObject resourceObject, final String right) throws UnauthorizedException, IOException {
        final User user = this.getUser(request, response);
        if (user.getRoles() != null && user.getRoles().contains(ResourceAuthService.ADMIN_ROLE)) {
            return;
        }
        if (resourceObject.getOwnerId().equals(user.getId())) {
            return;
        }
        if (resourceObject.getGroupAccessRights() != null) {
            final List<GroupAccessRight> groupAccessRights = resourceObject.getGroupAccessRights()
                    .getGroupAccessRights();
            final Map<String, Group> usersGroups = this.getGroupsWithUserAsMember(user);
            for (final GroupAccessRight groupAccessRight : groupAccessRights) {
                if (usersGroups.containsKey(groupAccessRight.getGroupId())) {
                    if (right.equals(ResourceAuthService.READ_RIGHT) && groupAccessRight.hasReadRight()) {
                        return;
                    } else if (right.equals(ResourceAuthService.UPDATE_RIGHT) && groupAccessRight.hasUpdateRight()) {
                        return;
                    } else if (right.equals(ResourceAuthService.DELETE_RIGHT) && groupAccessRight.hasDeleteRight()) {
                        return;
                    } else if (right.equals(ResourceAuthService.CHANGE_RIGHTS_RIGHT) && groupAccessRight.hasChangeRightsRight()) {
                        return;
                    }
                }
            }
        }
        throw new UnauthorizedException("You are not allowed to perform this operation");
    }
    
    /**
     * Check whether the logged in user is allowed to delete a resource. Therefore it checks the user's roles, whether
     * he is the owner of the resource or not and the group access rights.
     *
     * @param request
     *            the request. It must not be null!
     * @param response
     *            the response. It must not be null!
     * @param resourceObject
     *            the resource which the user wants to delete. It must not be null!
     * @throws UnauthorizedException
     *             if the logged in user is not allowed to delete the resource.
     * @throws IOException
     *             if a problem with the database access occurs during the right check.
     */
    public void allowDeleting(final Request request, final Response response, final ResourceObject resourceObject)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resourceObject, ResourceAuthService.DELETE_RIGHT);
    }

    /**
     * Check whether the logged in user is allowed to read a resource. Therefore it checks the user's roles, whether he
     * is the owner of the resource or not and the group access rights.
     *
     * @param request
     *            the request. It must not be null!
     * @param response
     *            the response. It must not be null!
     * @param resourceObject
     *            the resource which the user wants to read. It must not be null!
     * @throws UnauthorizedException
     *             if the logged in user is not allowed to read the resource.
     * @throws IOException
     *             if a problem with the database access occurs during the right check.
     */
    public void allowReading(final Request request, final Response response, final ResourceObject resourceObject)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resourceObject, ResourceAuthService.READ_RIGHT);
    }

    /**
     * Check whether the logged in user is allowed to change the group access rights for a resource. Therefore it checks
     * the user's roles, whether he is the owner of the resource or not and the group access rights.
     *
     * @param request
     *            the request. It must not be null!
     * @param response
     *            the response. It must not be null!
     * @param resourceObject
     *            the resource for which the user wants to change the group access rights. It must not be null!
     * @throws UnauthorizedException
     *             if the logged in user is not allowed to change the group access rights for the resource.
     * @throws IOException
     *             if a problem with the database access occurs during the right check.
     */
    public void allowRightsChanging(final Request request, final Response response, final ResourceObject resourceObject)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resourceObject, ResourceAuthService.CHANGE_RIGHTS_RIGHT);
    }

    /**
     * Check whether the logged in user is allowed to update a resource. Therefore it checks the user's roles, whether
     * he is the owner of the resource or not and the group access rights.
     *
     * @param request
     *            the request. It must not be null!
     * @param response
     *            the response. It must not be null!
     * @param resourceObject
     *            the resource which the user wants to update. It must not be null!
     * @throws UnauthorizedException
     *             if the logged in user is not allowed to update the resource.
     * @throws IOException
     *             if a problem with the database access occurs during the right check.
     */
    public void allowUpdating(final Request request, final Response response, final ResourceObject resourceObject)
            throws UnauthorizedException, IOException {
        this.allow(request, response, resourceObject, ResourceAuthService.UPDATE_RIGHT);
    }

    private void enableTestMode() {
        this.testModeEnabled = true;
    }
    
    private void setTestUser(User testUser){
        this.testUser = testUser;
    }

    private void disableTestMode() {
        this.testModeEnabled = false;
    }

    /**
     * Load all groups where the user is a member.
     *
     * @param user
     *            the user
     * @return a map with the groups, where the user is a member, as value and their group ids as key.
     * @throws IOException
     *             if a problem with the database access occurs.
     */
    private Map<String, Group> getGroupsWithUserAsMember(final User user) throws IOException {
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        final BasicDBObject elemMatch = new BasicDBObject();
        elemMatch.put("userId", user.getId());
        elemMatch.put("state",
                new Document("$in", Arrays.asList(GroupMembershipStatus.MEMBER.toString(), GroupMembershipStatus.ADMIN.toString())));
        final BasicDBObject members = new BasicDBObject();
        members.put("$elemMatch", elemMatch);
        final BasicDBObject query = new BasicDBObject();
        query.put("members", members);

        final Map<String, Group> groups = new HashMap<String, Group>();
        for (final DatabaseObject databaseObject : databaseConnection.getDatabaseObjectsByQuery(MongoCollectionTypes.GROUPS,
                query)) {
            if (databaseObject instanceof Group) {
                final Group group = (Group) databaseObject;
                groups.put(group.getId(), group);
            }
        }
        return groups;
    }
    
    private User getUser(Request request, Response response) throws UnauthorizedException{
        User user;
        if(this.testModeEnabled){
            user = this.testUser;
        }else{
            user = this.getAuthenticatedUser(request, response);
        }
        return user;
    }

}
