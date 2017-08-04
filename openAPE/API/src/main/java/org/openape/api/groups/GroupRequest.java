package org.openape.api.groups;

public class GroupRequest {

	private String groupname;
	private String description;
	private String entryRequirements;
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntryRequirements() {
		return entryRequirements;
	}
	public void setEntryRequirements(String entryRequirements) {
		this.entryRequirements = entryRequirements;
	} 
}
