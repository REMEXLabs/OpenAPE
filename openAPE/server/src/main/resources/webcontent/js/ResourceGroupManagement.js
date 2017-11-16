/**
 * 
 */
$(document).ready(function() {

	$('#btnAddResourceToGroup').click( function(){
		
	var groupId = $('#inputGroupId').val;
	var resourceId = 
	var accesssRights = new openape_api.AccessRight(groupId, resourceId , false, false, false, false, false);
	$('#resourceGroupTable').appent( addTableRowWithAccessRights(accessRights);)
})

addTableRowWithAccessRights = function (accessRights){
	return '<tr>'
	+ '<td></td>'
	+ '<td></td>'
	+ "<td id='accessRight_" + accessRights.groupId + "_groupIdd'>" +      >' + accessRights.groupId  + '</td>'
		+ "<td id='accessRight_" + accessRights.groupId + "_read'>" +      >' + accessRights.readRight  + '</td>'
				+ "<td id='accessRight_" + accessRights.groupId + "_update'>" +      >' + accessRights.updateRight  + '</td>'
		+ "<td id='accessRight_" + accessRights.groupId + "_change'>" +      >' + accessRights.changeRight  + '</td>'
		+ "<td id='accessRight_" + accessRights.groupId + "_delete'>" +      >' + accessRights.deleteRight  + '</td>';
}
})