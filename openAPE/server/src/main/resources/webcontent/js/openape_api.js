/**This file provides all data types required for the communication between an OpenAPE client and an OpenAPE server.
 *@Author Lukas Smirek 
 */


var openape_api = {
	AccessRight: function (groupId, resourceId, readRight, updateRight, deleteRight, changeRight) {
		this.groupId = groupId;
		this.resourceId = resourceId;
		this.readRight = readRight;
		this.updateRight = updateRight;
				this.deleteRight = deleteRight;
				this.changeRight = changeRight;
	}
};
