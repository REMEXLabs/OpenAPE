package org.openape.server.auth;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openape.api.group.GroupAccessRight;
import org.openape.api.group.GroupAccessRights;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.api.resourceDescription.ResourceObject;
import org.openape.api.user.User;
import org.openape.server.api.group.Group;
import org.openape.server.api.group.GroupMember;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

public class TestResourceAuthService {

    private static DatabaseConnection DATABASE_CONNECTION = DatabaseConnection.getInstance();

    private static User USER_1 = new User();
    private static User USER_2 = new User();
    private static User USER_3 = new User();
    private static User USER_4 = new User();
    private static User USER_OWNER = new User();
    private static User USER_ADMIN = new User();
    private static User USER_WITHOUT_RIGHTS = new User();

    private static Group GROUP_WITH_USER_1_AS_APPLYED;
    private static Group GROUP_WITH_USER_1_AS_IN_PROGRESS;
    private static Group GROUP_WITH_USER_1_AS_MEMBER;
    private static Group GROUP_WITH_USER_1_AS_ADMIN;
    private static Group GROUP_WITH_USER_1_AND_OTHERS;
    private static Group GROUP_WITHOUT_USER_1;
    private static Group GROUP_WITHOUT_USERS;

    private static ResourceObject RESOURCE_OBJECT_READ_RIGHT = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_UPDATE_RIGHT = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_DELETE_RIGHT = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL = new ResourceObject();
    private static ResourceObject RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP = new ResourceObject();

    private static ResourceAuthService RESOURCE_AUTH_SERVICE = new ResourceAuthService();

    private static Map<String, Group> GROUPS_WITH_USER_1;

    private static Map<String, Group> GROUPS_WITH_USER_4;


    public static void setTestUser(final User testUser) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        final Method method = TestResourceAuthService.RESOURCE_AUTH_SERVICE.getClass().getDeclaredMethod("setTestUser",
                User.class);
        method.setAccessible(true);
        method.invoke(TestResourceAuthService.RESOURCE_AUTH_SERVICE, testUser);
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        final Method methodEnableTestMode = TestResourceAuthService.RESOURCE_AUTH_SERVICE.getClass()
                .getDeclaredMethod("enableTestMode");
        methodEnableTestMode.setAccessible(true);
        methodEnableTestMode.invoke(TestResourceAuthService.RESOURCE_AUTH_SERVICE);

        // set user ids
        TestResourceAuthService.USER_1.setId("1");
        TestResourceAuthService.USER_2.setId("2");
        TestResourceAuthService.USER_3.setId("3");
        TestResourceAuthService.USER_4.setId("4");
        TestResourceAuthService.USER_OWNER.setId("5");
        TestResourceAuthService.USER_ADMIN.setId("6");
        final List<String> userAdminRoles = new ArrayList<String>();
        userAdminRoles.add("admin");
        TestResourceAuthService.USER_ADMIN.setRoles(userAdminRoles);
        TestResourceAuthService.USER_WITHOUT_RIGHTS.setId("userWithoutRights");


        // create groups
        final List<GroupMember> groupMembersOfGroupWithUser1AsApplyed = new ArrayList<GroupMember>();
        groupMembersOfGroupWithUser1AsApplyed.add(new GroupMember("1", GroupMembershipStatus.APPLYED));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_APPLYED = new Group("groupWithUser1AsApplyed", "desciption",
                groupMembersOfGroupWithUser1AsApplyed, false, null);

        final List<GroupMember> groupMembersOfGroupWithUser1AsInProgress = new ArrayList<GroupMember>();
        groupMembersOfGroupWithUser1AsInProgress.add(new GroupMember("1", GroupMembershipStatus.IN_PROGRESS));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_IN_PROGRESS = new Group("groupWithUser1AsInProgress", "desciption",
                groupMembersOfGroupWithUser1AsInProgress, false, null);

        final List<GroupMember> groupMembersOfGroupWithUser1AsMember = new ArrayList<GroupMember>();
        groupMembersOfGroupWithUser1AsMember.add(new GroupMember("1", GroupMembershipStatus.MEMBER));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER = new Group("groupWithUser1AsMember", "desciption",
                groupMembersOfGroupWithUser1AsMember, false, null);

        final List<GroupMember> groupMembersOfGroupWithUser1AsAdmin = new ArrayList<GroupMember>();
        groupMembersOfGroupWithUser1AsAdmin.add(new GroupMember("1", GroupMembershipStatus.ADMIN));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_ADMIN = new Group("groupWithUser1AsAdmin", "desciption",
                groupMembersOfGroupWithUser1AsAdmin, false, null);

        final List<GroupMember> groupMembersOfGroupWithUser1AndOthers = new ArrayList<GroupMember>();
        groupMembersOfGroupWithUser1AndOthers.add(new GroupMember("1", GroupMembershipStatus.MEMBER));
        groupMembersOfGroupWithUser1AndOthers.add(new GroupMember("2", GroupMembershipStatus.MEMBER));
        groupMembersOfGroupWithUser1AndOthers.add(new GroupMember("3", GroupMembershipStatus.MEMBER));
        TestResourceAuthService.GROUP_WITH_USER_1_AND_OTHERS = new Group("groupWithUser1AndOthers", "desciption",
                groupMembersOfGroupWithUser1AndOthers, false, null);

        final List<GroupMember> groupMembersOfGroupWithoutUser1 = new ArrayList<GroupMember>();
        groupMembersOfGroupWithoutUser1.add(new GroupMember("2", GroupMembershipStatus.MEMBER));
        groupMembersOfGroupWithoutUser1.add(new GroupMember("3", GroupMembershipStatus.MEMBER));
        TestResourceAuthService.GROUP_WITHOUT_USER_1 = new Group("groupWithoutUser1", "desciption",
                groupMembersOfGroupWithoutUser1, false, null);

        TestResourceAuthService.GROUP_WITHOUT_USERS = new Group("groupWithoutUsers", "desciption", null, false, null);

        // store groups in database
        TestResourceAuthService.GROUP_WITH_USER_1_AS_APPLYED
                .setId(TestResourceAuthService.DATABASE_CONNECTION.storeDatabaseObject(MongoCollectionTypes.GROUPS,
                        TestResourceAuthService.GROUP_WITH_USER_1_AS_APPLYED));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_IN_PROGRESS
                .setId(TestResourceAuthService.DATABASE_CONNECTION.storeDatabaseObject(MongoCollectionTypes.GROUPS,
                        TestResourceAuthService.GROUP_WITH_USER_1_AS_IN_PROGRESS));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.setId(TestResourceAuthService.DATABASE_CONNECTION
                .storeDatabaseObject(MongoCollectionTypes.GROUPS, TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER));
        TestResourceAuthService.GROUP_WITH_USER_1_AS_ADMIN.setId(TestResourceAuthService.DATABASE_CONNECTION
                .storeDatabaseObject(MongoCollectionTypes.GROUPS, TestResourceAuthService.GROUP_WITH_USER_1_AS_ADMIN));
        TestResourceAuthService.GROUP_WITH_USER_1_AND_OTHERS
                .setId(TestResourceAuthService.DATABASE_CONNECTION.storeDatabaseObject(MongoCollectionTypes.GROUPS,
                        TestResourceAuthService.GROUP_WITH_USER_1_AND_OTHERS));
        TestResourceAuthService.GROUP_WITHOUT_USER_1.setId(TestResourceAuthService.DATABASE_CONNECTION
                .storeDatabaseObject(MongoCollectionTypes.GROUPS, TestResourceAuthService.GROUP_WITHOUT_USER_1));
        TestResourceAuthService.GROUP_WITHOUT_USERS.setId(TestResourceAuthService.DATABASE_CONNECTION
                .storeDatabaseObject(MongoCollectionTypes.GROUPS, TestResourceAuthService.GROUP_WITHOUT_USERS));

        // load groups of user 1 for testing
        final Method method = ResourceAuthService.class.getDeclaredMethod("getGroupsWithUserAsMember", User.class);
        method.setAccessible(true);
        TestResourceAuthService.GROUPS_WITH_USER_1 = (Map<String, Group>) method
                .invoke(TestResourceAuthService.RESOURCE_AUTH_SERVICE, TestResourceAuthService.USER_1);

        // load groups of user 4 for testing
        TestResourceAuthService.GROUPS_WITH_USER_4 = (Map<String, Group>) method
                .invoke(TestResourceAuthService.RESOURCE_AUTH_SERVICE, TestResourceAuthService.USER_4);

        // prepare resource objects for testing
        TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT.setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT.setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT.setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT
                .setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE
                .setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE
                .setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY
                .setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL
                .setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP
                .setOwnerId(TestResourceAuthService.USER_OWNER.getId());
        TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId(),
                                "resourceObjectReadRight", true, false, false, false));
                    }
                }));
        TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId(),
                                "resourceObjectUpdateRight", false, true, false, false));
                    }
                }));
        TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId(),
                                "resourceObjectDeleteRight", false, false, true, false));
                    }
                }));
        TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId(),
                                "resourceObjectChangeRightsRight", false, false, false, true));
                    }
                }));
        TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId(),
                                "resourceObjectWithAllGroupAccessRightsTrue", true, true, true, true));
                    }
                }));
        TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId(),
                                "resourceObjectWithAllGroupAccessRightsFalse", false, false, false, false));
                    }
                }));
        TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>()));
        TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL.setGroupAccessRights(null);
        TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP
                .setGroupAccessRights(new GroupAccessRights(new ArrayList<GroupAccessRight>() {
                    {
                        this.add(new GroupAccessRight(TestResourceAuthService.GROUP_WITHOUT_USER_1.getId(),
                                "resourceObjectWithAllGroupAccessRightsTrueButForTheWrongGroup", true, true, true,
                                true));
                    }
                }));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        final Method methodDisableTestMode = TestResourceAuthService.RESOURCE_AUTH_SERVICE.getClass()
                .getDeclaredMethod("disableTestMode");
        methodDisableTestMode.setAccessible(true);
        methodDisableTestMode.invoke(TestResourceAuthService.RESOURCE_AUTH_SERVICE);

        // delete groups from database
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITH_USER_1_AS_APPLYED.getId());
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITH_USER_1_AS_IN_PROGRESS.getId());
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId());
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITH_USER_1_AS_ADMIN.getId());
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITH_USER_1_AND_OTHERS.getId());
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITHOUT_USER_1.getId());
        TestResourceAuthService.DATABASE_CONNECTION.deleteDatabaseObject(MongoCollectionTypes.GROUPS,
                TestResourceAuthService.GROUP_WITHOUT_USERS.getId());
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    // test allowDeleting
    @Test
    public void testAllowDeleteWithAdminAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowDeleteWithAdminAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }


    @Test
    public void testAllowDeleteWithAdminAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowDeleteWithOwnerAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowDeleteWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowDeleting(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    // test allowReading
    @Test
    public void testAllowReadWithAdminAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowReadWithAdminAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowReadWithOwnerAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowReadWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowReading(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    // test allowRightsChanging
    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowRightsChangeWithAdminAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowRightsChangeWithOwnerAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowRightsChangeWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowRightsChanging(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    // test allowUpdating
    @Test
    public void testAllowUpdateWithAdminAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowUpdateWithAdminAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_ADMIN);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test
    public void testAllowUpdateWithOwnerAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_OWNER);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithGroupAccessRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_1);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectChangeRightsRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_CHANGE_RIGHTS_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectDeleteRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_DELETE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectReadRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_READ_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectUpdateRight()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_UPDATE_RIGHT);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsButForTheWrongGroup()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE_BUT_FOR_THE_WRONG_GROUP);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsFalse()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCES_RIGHTS_FALSE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectWithAllGroupAccessRightsTrue()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_ALL_GROUP_ACCESS_RIGHTS_TRUE);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsEmpty()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_EMPTY);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAllowUpdateWithUserWithoutRightsAndResourceObjectWithGroupAccessRightsNull()
            throws UnauthorizedException, IOException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        TestResourceAuthService.setTestUser(TestResourceAuthService.USER_WITHOUT_RIGHTS);
        TestResourceAuthService.RESOURCE_AUTH_SERVICE.allowUpdating(null, null,
                TestResourceAuthService.RESOURCE_OBJECT_WITH_GROUP_ACCESS_RIGHTS_NULL);
    }

    @Test
    public void testGROUP_WITHOUT_USERS() {
        Assert.assertEquals(false, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITHOUT_USERS.getId()));
    }

    @Test
    public void testGroupWithoutUser1() {
        Assert.assertEquals(false, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITHOUT_USER_1.getId()));
    }

    @Test
    public void testGroupWithUser1AndOthers() {
        Assert.assertEquals(true, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITH_USER_1_AND_OTHERS.getId()));
    }

    @Test
    public void testUserWhichGroupMembershipStateIsAdmin() {
        Assert.assertEquals(true, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITH_USER_1_AS_ADMIN.getId()));
    }

    @Test
    public void testUserWhichGroupMembershipStateIsApplyed() {
        Assert.assertEquals(false, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITH_USER_1_AS_APPLYED.getId()));
    }

    @Test
    public void testUserWhichGroupMembershipStateIsInProgress() {
        Assert.assertEquals(false, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITH_USER_1_AS_IN_PROGRESS.getId()));
    }

    @Test
    public void testUserWhichGroupMembershipStateIsMember() {
        Assert.assertEquals(true, TestResourceAuthService.GROUPS_WITH_USER_1
                .containsKey(TestResourceAuthService.GROUP_WITH_USER_1_AS_MEMBER.getId()));
    }

    @Test
    public void testUserWhichIsNoGroupMember() {
        Assert.assertEquals(true, TestResourceAuthService.GROUPS_WITH_USER_4.isEmpty());
    }

}
