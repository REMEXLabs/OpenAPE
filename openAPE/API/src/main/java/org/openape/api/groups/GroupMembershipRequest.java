package org.openape.api.groups;

/* Request that must be send by a client to the OpenAPE server in order to request a membership in a resource group
 *@author Lukas Smirek
 */

public class GroupMembershipRequest {
    private String userId;
    private String status;
    
    public String getUserId() {
        return this.userId;
    }

    /*
    public GroupMembershipStatus getStatus() {
        return this.status;
    }



    public void setStatus(final GroupMembershipStatus status) {
        this.status = status;
    }
*/
    public void setUserId(final String userId) {
        this.userId = userId;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
