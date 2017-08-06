package org.openape.server.api.group;

import org.openape.api.groups.GroupMembershipStatus;

/**
 * This class defines a group member. Therefore it contains the user's id {@link IUser#getId()} and a flag, whether this
 * user is only a member or also an admin of the group. The group member will be stored in a list of the group
 * {@link IGroup#getMembers()}. Thus this class does not contain a group id {@link IGroup#getId()}. The state of the
 * group member ship is defined by {@link #state}.
 *
 * This class is thread safe.
 *
 * @author Tobias Ableitner
 *
 */
public class GroupMember {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * The members user id {@link IUser#getId()}.
	 */
	private String userId;

	/**
	 * The state of the group member ship.
	 */
	private GroupMembershipStatus state;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Create a group member.
	 *
	 * @param userId
	 *            user id {@link IUser#getId()} of the user, who is the group member. It must not be null or empty.
	 * @param state
	 *            the state of the group member ship. It must not be null!
	 */
	public GroupMember(final String userId, final GroupMembershipStatus state) {
		this.setUserId(userId);
		this.setState(state);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Getter for the user id {@link IUser#getId()} of the group member.
	 *
	 * @return user id of the group member
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * Setter for the user id {@link IUser#getId()} of the group member. It must not be null or empty.
	 *
	 * @param userId
	 *            user id {@link IUser#getId()} of the group member
	 */
	public void setUserId(final String userId) {
		// Checker.checkUserId(userId);
		this.userId = userId;
	}

	/**
	 * Getter for the state {@link #state} of the group member ship.
	 *
	 * @return state of the group member ship
	 */
	public GroupMembershipStatus getState() {
		return this.state;
	}

	/**
	 * Setter for the state {@link #state} of the group member ship.
	 *
	 * @param the
	 *            state of the group member ship. It must not be null!
	 */
	public void setState(final GroupMembershipStatus state) {
		this.state = state;
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
