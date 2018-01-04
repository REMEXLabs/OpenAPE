/**This file provides all data types required for the communication between an OpenAPE client and an OpenAPE server.
 *@Author Lukas Smirek 
 */


var openape_api = {
	AccessRight: function (groupId, readRight, updateRight, deleteRight, changeRightsRight) {
		this.groupId = groupId;
		this.readRight = readRight;
		this.updateRight = updateRight;
		this.deleteRight = deleteRight;
		this.changeRightsRight = changeRightsRight;
	}
};
