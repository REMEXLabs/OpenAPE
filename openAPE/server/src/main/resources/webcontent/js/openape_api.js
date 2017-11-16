/**This file provides all data types required for the communication between an OpenAPE client and an OpenAPE server.
 *@Author Lukas Smirek 
 */


var openape_api = {
	AccessRight : function(groupId, resourceIdread,update,delete,change) {
		this.groupID = groupId;
		this.resourceId = resourceId;
		this.readRight = read;
		this.updateRight = update;
				this.delete = delete;
				this.changeRight = change;
	}
}
