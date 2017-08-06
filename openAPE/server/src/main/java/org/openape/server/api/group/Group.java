package org.openape.server.api.group;

import java.util.List;

import org.openape.api.DatabaseObject;
import org.openape.api.groups.GroupMembershipStatus;

/**
 * This class defines a group. A group has members. Some of those members can
 * also be admins of the group. Group admins are allowed to edit a group and
 * their memberships. The OpenAPE server uses groups to manage the access rights
 * for the resources. For each resource it is defined which group is allowed to
 * read, update and delete it and change the access rights for it. Thus which
 * access right an user has, depends on his group member ships.
 *
 * This class is not thread safe.
 *
 * @author Tobias Ableitner
 *
 */
public class Group extends DatabaseObject {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     *
     */
    private static final long serialVersionUID = -4045513910309710151L;

    /**
     * Group's id.
     */
    private String id;

    /**
     * Group's name.
     */
    private String name;

    /**
     * Users who are members of the group.
     */
    private List<GroupMember> members;

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Create a new group and set's the value of {@link Group#id} to null. This
     * constructor should be used, to instantiate groups, which are not already
     * stored in the database.
     *
     * @param name
     *            the name of the group. It must not be null or empty.
     * @param members
     *            list with the members of this the group. If the group has no
     *            member(s) it can be empty but not null.
     */
    public Group(final String name, final List<GroupMember> members) {
        this(null, name, members);
    }

    /**
     * Create a new group. This constructor should be used, to instantiate
     * groups, which are already stored in the database.
     *
     * @param id
     *            the id of the group. It must not be null or empty.
     * @param name
     *            the name of the group. It must not be null or empty.
     * @param members
     *            list with the members of this the group. If the group has no
     *            member(s) it can be empty but not null.
     */
    public Group(final String id, final String name, final List<GroupMember> members) {
        this.setId(id);
        this.setName(name);
        this.setMembers(members);
    }

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // getters and setters
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Getter for the group's id.
     *
     * @return the group's id, which is a string. If the group is not already
     *         stored in the database, null can be returned.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for the group's id. It must not be empty and should not be null.
     * Null is only allowed, if the group is not already stored in the database.
     *
     * @param id
     *            the group's id
     */
    public void setId(final String id) {
        // Checker.checkIntegerGreaterEquals(id, "id", -1);
        this.id = id;
    }

    /**
     * Getter for the group's name.
     *
     * @return the groups name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for the group's name. It must not be null or empty.
     *
     * @param name
     *            the group's name
     */
    public void setName(final String name) {
        // Checker.checkNullAndEmptiness(name, "name");
        this.name = name;
    }

    /**
     * Getter for the group's members. If the group has no members, an empty
     * list will be returned.
     *
     * @return a list with the group's members
     */
    public List<GroupMember> getMembers() {
        return this.members;
    }

    /**
     * Setter for the group's members. If the group has no members, it can be
     * empty, but not null.
     *
     * @param members
     *            the group's members
     */
    public void setMembers(final List<GroupMember> members) {
        // Checker.checkNull(members, "members");
        this.members = members;
    }

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

    /**
     * Checks, whether an user with the given id is an admin of this group or
     * not. The userId must not be empty or null. If the user is not a member of
     * this group, false will be returned.
     *
     * @param userId
     *            the user's id
     * @return true if the user is an admin of this group and false if not or if
     *         the user is not a member of the group.
     */
    public boolean isUserGroupAdmin(final String userId) {
        // Checker.checkUserId(userId);
        boolean isGroupAdmin = false;
        for (final GroupMember groupMember : this.members) {
            if (groupMember.getUserId().equals(userId)) {
                if (groupMember.getState() == GroupMembershipStatus.ADMIN) {
                    isGroupAdmin = true;
                }
                break;
            }
        }
        return isGroupAdmin;
    }

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // public methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

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

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

}
