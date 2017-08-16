package org.openape.server.api.group;

import java.util.List;

import org.openape.api.DatabaseObject;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.server.database.mongoDB.DatabaseConnection;

/**
 * This class defines a group. A group has members. Some of those members can
 * also be admins of the group. Group admins are allowed to edit a group and
 * their memberships. If the access to the group is open, users can assign at
 * their own to the group. If the access is not open, the group admins or the
 * openAPE admins have to assign users to the group. Whether the assess is open
 * or not, can be specified by {@link #openAccess}. The OpenAPE server uses
 * groups to manage the access rights for the resources. For each resource it is
 * defined which group is allowed to read, update and delete it and change the
 * access rights for it. Thus which access right an user has, depends on his
 * group member ships.
 *
 * This class is not thread safe.
 *
 * @author Tobias Ableitner
 *
 */
public class Group extends DatabaseObject {

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
     * Group's description.
     */
    private String description;

    /**
     * Users who are members of the group.
     */
    private List<GroupMember> members;

    /**
     * True if users can assign them self to this group and false if not.
     */
    private boolean openAccess;

    /**
     * Empty constructor. It is needed for the object mapping in
     * {@link DatabaseConnection}:
     */
    public Group() {

    }

    /**
     * Create a new group and set's the value of {@link Group#id} to null. This
     * constructor should be used, to instantiate groups, which are not already
     * stored in the database.
     *
     * @param name
     *            the name of the group. It must not be null or empty.
     * @param description
     *            the description of the group. It is optional. Thus it can be null, but must not be empty!
     * @param members
     *            list with the members of this the group. If the group has no
     *            member(s) it can be empty but not null.
     */
    public Group(final String name, final String description, final List<GroupMember> members) {
        this(null, name, description, members);
    }

    /**
     * Create a new group. This constructor should be used, to instantiate
     * groups, which are already stored in the database.
     *
     * @param id
     *            the id of the group. It must not be null or empty.
     * @param name
     *            the name of the group. It must not be null or empty.
     * @param description
     *            the description of the group. It is optional. Thus it can be null, but must not be empty!
     * @param members
     *            list with the members of this the group. If the group has no
     *            member(s) it can be empty but not null.
     */
    public Group(final String id, final String name, final String description, final List<GroupMember> members) {
        this.setId(id);
        this.setName(name);
        this.setGroupDescription(description);
        this.setMembers(members);
        this.setOpenAccess(false);
    }

    private boolean executeIsUserAssigend(final String userId, final GroupMembershipStatus state) {
        boolean result = false;
        for (final GroupMember groupMember : this.members) {
            if (groupMember.getUserId().equals(userId)) {
                if (state != null) {
                    if (groupMember.getState() == state) {
                        result = true;
                    }
                } else {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    /**
     * Getter for the group's description.
     *
     * @return the group's description or null
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for the group's id.
     *
     * @return the group's id, which is a string. If the group is not already
     *         stored in the database, null can be returned.
     */
    @Override
    public String getId() {
        return this.id;
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
     * Getter for the group's name.
     *
     * @return the group's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter whether the group has an open access or not. Open access means,
     * that users can assign them self to the group. If the access is not open,
     * a group or openAPE admin has to assign them to the group.
     *
     * @return true if the access is open and false if not.
     */
    public boolean isOpenAccess() {
        return this.openAccess;
    }

    /**
     * Checks, whether an user with the given id is assigned to this group or
     * not. The userId must not be empty or null. If the user is not assigned to
     * this group, false will be returned.
     *
     * @param userId
     *            the user's id
     * @return true if the user is assigned to this group and false if not.
     */
    public boolean isUserAssigend(final String userId) {
        return this.executeIsUserAssigend(userId, null);
    }

    /**
     * Checks, whether an user with the given id and state is assigned to this
     * group or not. The userId must not be empty or null. Also the state must
     * not be empty. If the user is not assigned with the given state to this
     * group, false will be returned.
     *
     * @param userId
     *            the user's id
     * @param state
     *            the state of the group member ship
     * @return true if the user is assigned with the given state to this group
     *         and false if not.
     */
    public boolean isUserAssigendAs(final String userId, final GroupMembershipStatus state) {
        return this.executeIsUserAssigend(userId, state);
    }

    /**
     * Setter for the group's description. The group description is optional. Thus it can be set to null.
     *
     * @param description
     *            the group's description. It can be null but must not be empty!
     */
    public void setGroupDescription(final String description) {
        this.description = description;
    }

    /**
     * Setter for the group's id. It must not be empty and should not be null.
     * Null is only allowed, if the group is not already stored in the database.
     *
     * @param id
     *            the group's id
     */
    @Override
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Setter for the group's members. If the group has no members, it can be
     * empty, but not null.
     *
     * @param members
     *            the group's members
     */
    public void setMembers(final List<GroupMember> members) {
        this.members = members;
    }

    /**
     * Setter for the group's name. It must not be null or empty.
     *
     * @param name
     *            the group's name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Setter whether the group has an open access or not. Open access means,
     * that users can assign them self to the group. If the access is not open,
     * a group or openAPE admin has to assign them to the group.
     *
     * @param openAccess
     *            true if the access should be open and false if not.
     */
    public void setOpenAccess(final boolean openAccess) {
        this.openAccess = openAccess;
    }

}
