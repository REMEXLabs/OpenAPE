package org.openape.server.auth;

import java.util.List;

import org.openape.api.groups.GroupMembershipStatus;
import org.openape.api.user.User;
import org.openape.server.api.group.Group;

import spark.Request;
import spark.Response;

/**
 * This class is the authentication service for the group management. It checks
 * if the logged in user is allowed to edit a group or not. Therefore it checks,
 * whether the logged in user is an OpenAPE admin or the group admin.
 *
 * @author Tobias Ableitner
 *
 */
public class GroupAuthService extends AuthService {

    /**
     * Checks whether the logged in user is an openAPE admin or an admin of the
     * group.
     *
     * Checks whether the logged in user is an openAPE admin or an admin of the group.
     *
     * @param request
     *            the request. It must not be null!
     * @param response
     *            the response. It must not be null!
     * @param group
     *            the group, for which should be checked, whether the logged in
     *            user is allowed to edit it or not. It must not be null!
     * @throws UnauthorizedException
     *             if the the logged in user is neither an openAPE admin nor an
     *             admin of the group.
     */
    public void allowOpenAPEAndGroupAdmin(final Request request, final Response response,
            final Group group) throws UnauthorizedException {
        final User user = this.getAuthenticatedUser(request, response);
        final List<String> roles = user.getRoles();
        if (!group.isUserAssigendAs(user.getId(), GroupMembershipStatus.ADMIN)
                && !roles.contains("admin")) {
            throw new UnauthorizedException("You are not allowed to perform this operation");
        }
    }
    
}
