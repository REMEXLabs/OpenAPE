package org.openape.api.groups;
/* Request that must be send by a client to the OpenAPE server in order to request a membership in a resource group
 *@author Lukas Smirek
 */


public class GroupMembershipRequest {
    public String getUserId() {
		return userId;
	}
	public GroupMembershipStatus getStatus() {
		return status;
	}
	private String userId;
    private GroupMembershipStatus status;
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setStatus(GroupMembershipStatus status) {
		this.status = status;
	}
}
