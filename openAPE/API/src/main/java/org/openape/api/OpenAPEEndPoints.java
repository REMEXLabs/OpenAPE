package org.openape.api;

public class OpenAPEEndPoints {

    // REST endpoints
    public static final String USER_ROLES = "/users/:userId/roles";
    public static final String USER_PASSWORD = "/users/:userId/password";
    public static final String USER_ID = ":userid";
    public static final String MY_ID = "/users/myId";
    public static final String USER_DETAILS = "/users/openape/:userId";
    public static final String USERS_DETAILS = "/users";

    // messages
    public static final String USER_ROLES_CHANGED = "User roles successfully changed";
    public static final String GROUPS = "openape/groups";
    public static final String GROUP_MEMBER = "openape/:groupId/members/:userId";
    public static final String GROUP_DOES_NOT_EXIST = "The requested group does not exist";
    public static final String GROUP_ID = "openape/groups/:groupId";
    public static final String UserContext = "api/user-contexts";
	public static final String TASK_CONTEXTS = "api/task-contexts";
	public static final String ENVIRONMENT_CONTEXTS = "api/environment-contexts";

    public static String userDoesNotExist(final String userId) {
        return "User with Id \"" + userId + "\" does not exist.";
    }

}
