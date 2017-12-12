var url = window.location.origin;
var objStatus = {};
var lisErrNoStatus = [];
var listObjMember = [];
$(document).ready(function() {
	
	
	var groupTable = $('#groupDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
		"responsive": true
    } ); 
	
	var userGroupDataTable = $('#userGroupDataTable').DataTable( {
		"lengthMenu": [[3], [3]],
		 "bLengthChange": false,
		 "bInfo": false,
   } ); 
	
	var deleteGroupMemberDataTable = $('#deleteGroupMemberDataTable').DataTable( {
		"lengthMenu": [[3], [3]],
		 "bLengthChange": false,
		 "bInfo": false,
   } ); 
	
	$('#addGroupMemberModal').on('hidden.bs.modal', function () {
		$('.loading').css("display", "block");
		
		setTimeout(function(){ 
			$('.loading').css("display", "none");
     		location.reload();
    	}, 1000);
	});
	
	
	$('#deleteGroupMemberModal').on('hidden.bs.modal', function () {
		$('.loading').css("display", "block");
		
		setTimeout(function(){ 
			$('.loading').css("display", "none");
     		location.reload();
    	}, 1000);
	});
	
	$('#deleteGroupMemberDataTable').css("width", "10% !important");
	
	if(window.location.pathname == "/myGroups"){
		var listGroupIds = [];
		var listGroupIdsWithUser = [];
		var i = 1;
		
		$('#groupDataTable tr').each(function(){
			if($('#groupDataTable tr:eq('+i+') td:eq(0)').text() != ""){
				var groupId = $('#groupDataTable tr:eq('+i+') td:eq(0)').text();
				listGroupIds.push(groupId);
				var objGroup = getGroupFromDB(groupId);
				var members = objGroup.members;
				for(var k = 0; k<members.length;k++){
					
					if(members[k].userId == localStorage.getItem("userid")){
						listGroupIdsWithUser.push(groupId);
					}
				}
				
			}
			i++;
		})
		
		 //compares two lists and returns the user not in group
		 var listForeignGroups = listGroupIds.filter(function(v) {
		     return listGroupIdsWithUser.indexOf(v) == -1;
		 })
		 
		 
		 for(var i = 0; i<listForeignGroups.length; i++){
			 $('#groupDataTable').DataTable().row( $('#'+listForeignGroups[i]).closest("tr"))
		        .remove()
		        .draw(); 
		 }

	}
	
	$('#addGroupModal').on('hidden.bs.modal', function () {	
		$('#addFormGoupName').removeClass("has-error has-feedback");
		$('#addFormGoupDescription').removeClass("has-error has-feedback");
		$('#addGroupMainErrSection').empty();
		
	});
	
	$('#editGroupModal').on('hidden.bs.modal', function () {	
		$('#editFormGoupName').removeClass("has-error has-feedback");
		$('#editFormGoupDescription').removeClass("has-error has-feedback");
		$('#editGroupMainErrSection').empty();
	});
	; 
	
	$('#btnAddGroup').click(function () {
		$('#addGroupModal').modal("show");
	})
	
	
	
	if(window.location.hash == "#groups"){
		openCity(event, "groups");
	}
	
	$('#btnConfirmAddGroup').click(function () {
		var groupName = $('#addGroupNameInput').val();
		var groupDescription = $('#addGroupDescriptionInput').val();
		var openAccess = $('#cbAddOpenAcces').prop( "checked" );
		var objGroup = {};
		
		objGroup.description = groupDescription;
		objGroup.groupname = groupName;
		objGroup.openAccess = openAccess;
		
		if(validateFields("add", groupName, groupDescription)){
			if(addGroupToDB(JSON.stringify(objGroup)) == 201){
				$('.loading').css("display", "block");
				$('#addGroupModal').modal('hide');
	             setTimeout(function(){ 
	            	 $('.loading').css("display", "none");
	         		location.reload();
	        		}, 1000);
			}
		} 

	})
	
	$('#btnConfirmDeleteGroup').click(function () {
		if(removeGroupFromDB(window.eventId) == 200){
	   		$('#deleteGroupModal').modal('hide');
	   		$('.loading').css("display", "block");
			
			setTimeout(function(){ 
				$('.loading').css("display", "none");
	     		location.reload();
	    	}, 1000);
		}
	})
	
	$('#btnConfirmAddGroupMember').click(function () {
		var groupId = window.groupId;
		var userId = $('#addGroupMemberNameInput').val();
		
		console.log(listObjMember);
		//addGroupMemberToDB(groupId, userId);
	})
	
	$('#btnConfirmEditGroup').click(function () {
		var groupName = $('#editGroupNameInput').val();
		var groupDescription = $('#editGroupDescriptionInput').val();
		var openAccess = $('#cbEditOpenAcces').prop( "checked" );
		
		var objGroup = {};
		
		objGroup.description = groupDescription;
		objGroup.name = groupName;
		objGroup.openAccess = openAccess;
		
		if(validateFields("edit", groupName, groupDescription)){
			if(updateGroupDB(window.groupId, JSON.stringify(objGroup)) == 200){
				
				$('#editGroupModal').modal('hide');
				$('.loading').css("display", "block");
				setTimeout(function(){ 
					$('.loading').css("display", "none");
		     		location.reload();
		    	}, 1000);
			}
		} 
			
	
	})
	
	
	$('#btnConfirmCloseAddGroupMemberModal').click(function () {
		$('.loading').css("display", "block");
		
		setTimeout(function(){ 
			$('.loading').css("display", "none");
     		location.reload();
    	}, 1000);
        
		
	})
	
	$('#btnConfirmCloseRemoveGroupMemberModal').click(function () {
		$('.loading').css("display", "block");
        setTimeout(function(){ 
        	$('.loading').css("display", "none");
     		location.reload();
    		}, 500);
	})
	//groupTable.column( 0 ).visible( false );
	
})


function addUserToGroup (event){
	var userId = event.attributes[0].value.substring(event.attributes[0].value.indexOf("_")+1);
	var groupId = window.groupId;
	var objGroup = getGroupFromDB(userId);
	var objUser = {};
	var isStatusSet = false;
	objUser.userId = userId;
	
	if(listObjMember.length != 0){
		listObjMember.forEach(function(element){
			if(element.userId == userId){
				objUser.status = element.status;
				isStatusSet = true;
			} 
		})
	} else {
		isStatusSet = false;
	}
	
	
	if(isStatusSet) {
		if(addGroupMemberToDB(groupId, userId, objUser) == 200){
			$('#userGroupDataTable').DataTable().row( $(event).closest('tr') )
	        .remove()
	        .draw();
		}
	} else {
		$('#tdUsername_'+userId).empty();
		$('#tdUsername_'+userId).append("<img width='20px' src='img/attention_icon.png'>&nbsp;" );
		
		lisErrNoStatus.push(userId);

		$('#addGroupMemberModalMainErrSection').empty();
		$('#addGroupMemberModalMainErrSection').append("<img width='20px' src='img/attention_icon.png'>&nbsp; Please choose a status");
	}
}

function addGroupMember (event){
	var groupId = event.id;
	var objGroup = getGroupFromDB(groupId);
	var objAllUsers = getAllUsersFromDB();
	var listAllUsersIdsInDB = [];
	var listAllUsersIdsInGroup = [];
	
	console.log(event);
	var members = objGroup.members;
	$('#addGroupMemberModal').modal("show");
	
	window.groupId = groupId;
	window.members = members;	
	
	$('#userGroupDataTable').DataTable().clear().draw();
	
	 
	 for(var i = 0; i<objAllUsers.length; i++){
		 listAllUsersIdsInDB.push(objAllUsers[i].id);
	 }
	 
	 for(var i = 0; i<members.length; i++){
		 listAllUsersIdsInGroup.push(members[i].userId.replace("'", ""));
	 }
	 
	 //compares two lists and returns the user not in group
	 var listUserNotInDb = listAllUsersIdsInDB.filter(function(v) {
	     return listAllUsersIdsInGroup.indexOf(v) == -1;
	 })
	 
	  $('#userGroupDataTable')
		 .DataTable()
		 .clear()
		 .draw();
	 
	 for(var i = 0; i<listUserNotInDb.length; i++){
		 objUser = JSON.parse(openape.getUserById(listUserNotInDb[i]).responseText);
		 
		 $('#userGroupDataTable')
		 .DataTable()
		 .row.add( ["<span id='tdUsername_"+listUserNotInDb[i]+"'></span>"+objUser.username, "<button id='memberStatus_"+listUserNotInDb[i]+"' onClick='addStatusMember(this)' class='btn btn-md btn-default'><img src='img/user-filled-person-shape.png'> Member</button><button onClick='addStatusAdmin(this)'  id='adminStatus_"+listUserNotInDb[i]+"' class='btn btn-md btn-default'><img src='img/admin-with-cogwheels.png'> Admin</button>", 
			 "<button id='addUserToGroup_"+listUserNotInDb[i]+"' class='btn btn-md btn-default' onClick='addUserToGroup(this)' ><div class='glyphicon glyphicon-plus'></div> Add</button>"] )
		 .draw( false ); 
	 }
}

function removeGroupMember (event){
	var groupId = window.groupId;
	var userId = event.id.substring(event.id.indexOf("_")+1)
	
	if(deleteGroupMemberFromDB(groupId, userId) == 200){
		$('#deleteGroupMemberDataTable').DataTable().row( $(event).closest('tr') )
        .remove()
        .draw();
	}
}

function deleteGroupMember(event){
	var groupId = event.id;
	var objGroup = getGroupFromDB(groupId);
	var members = objGroup.members;
	window.groupId = groupId;
	$('#deleteGroupMemberModal').modal('show');
	$('#deleteGroupMemberModal').attr('aria-hidden', 'false');
	
	 for(var i = 0; i<members.length; i++){
		 objUser = JSON.parse(openape.getUserById(members[i].userId).responseText);
		 
		 $('#deleteGroupMemberDataTable')
		 .DataTable()
		 .row.add( [objUser.username, "<button id='deleteGroupMember_"+members[i].userId+"' class='btn btn-md btn-default' onClick='removeGroupMember(this)' ><div class='glyphicon glyphicon-trash'></div> Remove</button>"] )
		 .draw( false ); 
	 }
}

function addStatusAdmin (event){
	var statusId = event.id;
	var userId = event.id.substring(event.id.indexOf("_")+1);
	var isInArray = false;
	var arrayIndex = 0;
	
	$('#'+statusId).removeClass("btn-default");
	$('#memberStatus_'+userId).addClass("btn-default");
	
	objStatus = {};
	objStatus.status = "ADMIN";
	objStatus.userId = userId;

	for(var i=0; i<listObjMember.length; i++){
		if(listObjMember[i].userId == userId){
			arrayIndex = i;
			isInArray = true;
			break;
		} 
	}
	
	if(isInArray){
		listObjMember.splice(arrayIndex, 1);
		listObjMember.push(objStatus);
	} else {
		listObjMember.push(objStatus);
	}
	
	$('#tdUsername_'+userId).empty();
	
	for(var i = 0; i<lisErrNoStatus.length; i++){
		if(lisErrNoStatus[i] == userId){
			lisErrNoStatus.splice(i, 1);
		}	
	}
	
	if(lisErrNoStatus.length == 0){
		$('#addGroupMemberModalMainErrSection').empty();
	}
}

function addStatusMember(event) {
	var statusId = event.id;
	var userId = event.id.substring(event.id.indexOf("_")+1);
	var isInArray = false;
	var arrayIndex = 0;
	
	$('#adminStatus_'+userId).addClass("btn-default");
	$('#'+statusId).removeClass("btn-default");
	
	objStatus = {};
	objStatus.status = "MEMBER";
	objStatus.userId = userId;
	
	for(var i=0; i<listObjMember.length; i++){
		if(listObjMember[i].userId == userId){
			arrayIndex = i;
			isInArray = true;
			break;
		} 
	}
		
	if(isInArray){
		listObjMember.splice(arrayIndex, 1);
		listObjMember.push(objStatus);
	} else {
		listObjMember.push(objStatus);
	}
	
	$('#tdUsername_'+userId).empty();
	
	for(var i = 0; i<lisErrNoStatus.length; i++){
		if(lisErrNoStatus[i] == userId){
			lisErrNoStatus.splice(i, 1);
		}	
	}
	
	if(lisErrNoStatus.length == 0){
		$('#addGroupMemberModalMainErrSection').empty();
	}
	
}

function deleteGroup(event){
	$('#deleteGroupModal').modal('show');
	$('#deleteGroupModal').attr('aria-hidden', 'false');
	var id = event.id;
	window.eventId = id;
}

function editGroup(event){
	$('#editGroupModal').modal('show');
	$('#editGroupModal').attr('aria-hidden', 'false');
	var id = event.id;
	var objGroup = getGroupFromDB(id);
	$('#editGroupNameInput').val(objGroup.name);
	$('#editGroupDescriptionInput').val(objGroup.description);
	
	if(objGroup.openAccess){
		$('#cbEditOpenAcces').prop("checked", true);
	}
	window.groupId = id;
}

function validateFields(action, name, description){
	var isNameCorrect = true;
	var isDescriptionCorrect = true;
	
	if(name == ""){
		$("#"+action+"FormGoupName").addClass("has-error has-feedback");
		isNameCorrect = false
	} else {
		$("#"+action+"FormGoupName").removeClass("has-error has-feedback");
		isNameCorrect = true;
	}
	
	if(description == ""){
		$("#"+action+"FormGoupDescription").addClass("has-error has-feedback");
		isDescriptionCorrect = false;
	} else {
		$("#"+action+"FormGoupDescription").removeClass("has-error has-feedback");
		isDescriptionCorrect = true;
	}
	
	if(isNameCorrect && isDescriptionCorrect){
		$('#'+action+'GroupMainErrSection').empty();
		return true;
	} else {
		$('#'+action+'GroupMainErrSection').empty();
		$('#'+action+'GroupMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  Invalid inputs!");	
		return false;
	}
}

function removeGroupFromDB(groupId) {
	var status = "";
	$.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url: url+'/openape/groups/'+groupId,
        dataType: "json",
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
         status = jqXHR.status;
        },
        error: function(jqXHR, textStatus, errorThrown){
        	status = jqXHR.status;
        }
    });
	return status;
}


function updateGroupDB(groupId, group) {
	var status = 0;
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: url+'/openape/groups/'+groupId,
        dataType: "json",
        data: group,
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	status = jqXHR.status;
        },
        error: function(jqXHR, textStatus, errorThrown){
        	status = jqXHR.status;
        }
    });
	
	return status;
}

/**
 * get Group from server
 * @param groupId
 * @returns
 */
function getGroupFromDB(groupId) {
	var objGroup = {};
	$.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: url+'/openape/groups/'+groupId,
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	objGroup =  JSON.parse(jqXHR.responseText);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	console.log(jqXHR);
        }
    });
	
	return objGroup;
}

function getAllUsersFromDB() {
	var objGroup = {};
	$.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: url+'/openape/users',
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	objUser =  JSON.parse(jqXHR.responseText);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	console.log(jqXHR);
        }
    });
	
	return objUser;
}

function addGroupMemberToDB(groupId, userId, objUser) {
	var status = 0;
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: url+'/openape/'+groupId+"/members/"+userId,
        dataType: "json",
        data: JSON.stringify(objUser),
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
   		  console.log(jqXHR);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	
	        if(jqXHR.status != 200){
	        	$('#addGroupMemberMainErrSection').empty();
	        	$('#addGroupMemberMainErrSection').append(jqXHR.responseText);
	        } else {
	        	status = jqXHR.status;
	        }
        }
    });
	return status;
}

function deleteGroupMemberFromDB (groupId, userId){
	var status = 0;
	$.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url: url+'/openape/'+groupId+"/members/"+userId,
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        		status = jqXHR.status;
        },
        error: function(jqXHR, textStatus, errorThrown){
        	$('#deleteGroupUserModal').modal('hide');
        	 setTimeout(function(){ 
          		location.reload();
         		}, 1000);
        }
    });
	return status;
}


function addGroupToDB(group) {
	var status = "";
	$.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: url+'/openape/groups',
        dataType: "json",
        data: group,
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	status = jqXHR.status;
        },
        error: function(jqXHR, textStatus, errorThrown){
        	status = jqXHR.status;
        }
    });
	return status;
}