var url = window.location.origin;
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
	
	
	$('#deleteGroupMemberDataTable').css("width", "10% !important");
	
	if(window.location.pathname == "/myGroups"){
		groupTable.column( 5 ).visible( false );
		groupTable.column( 4 ).visible( false );
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
		var objGroup = {};
		
		objGroup.description = groupDescription;
		objGroup.groupname = groupName;
		
		validateFields("add", groupName, groupDescription) == true ? addGroupToDB(JSON.stringify(objGroup)) : void 0;

	})
	
	$('#btnConfirmDeleteGroup').click(function () {
		removeGroupFromDB(window.eventId);
	})
	
	$('#btnConfirmAddGroupMember').click(function () {
		var groupId = window.groupId;
		var userId = $('#addGroupMemberNameInput').val();
		
		addGroupMemberToDB(groupId, userId);
	})
	
	$('#btnConfirmEditGroup').click(function () {
		var groupName = $('#editGroupNameInput').val();
		var groupDescription = $('#editGroupDescriptionInput').val();
		var objGroup = {};
		
		objGroup.description = groupDescription;
		objGroup.name = groupName;
		
		validateFields("edit", groupName, groupDescription) == true ? 
				updateGroupDB(window.groupId, JSON.stringify(objGroup)) : void 0;
	
	})
	
	
	$('#btnConfirmCloseAddGroupMemberModal').click(function () {
        setTimeout(function(){ 
     		location.reload();
    		}, 500);
	})
	
	$('#btnConfirmCloseRemoveGroupMemberModal').click(function () {
        setTimeout(function(){ 
     		location.reload();
    		}, 500);
	})
	
})


function addUserToGroup (event){
	var userId = event.attributes[0].value.substring(event.attributes[0].value.indexOf("_")+1);
	var groupId = window.groupId;
	
	var objGroup = getGroupFromDB(userId);
	
	if(addGroupMemberToDB(groupId, userId) == 200){
		$('#userGroupDataTable').DataTable().row( $(event).closest('tr') )
        .remove()
        .draw();
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
		 .row.add( [objUser.username, "<button id='addUserToGroup_"+listUserNotInDb[i]+"' class='btn btn-md btn-default' onClick='addUserToGroup(this)' ><div class='glyphicon glyphicon-plus'></div> Add</button>"] )
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
	
	 for(var i = 0; i<members.length; i++){
		 objUser = JSON.parse(openape.getUserById(members[i].userId).responseText);
		 
		 $('#deleteGroupMemberDataTable')
		 .DataTable()
		 .row.add( [objUser.username, "<button id='deleteGroupMember_"+members[i].userId+"' class='btn btn-md btn-default' onClick='removeGroupMember(this)' ><div class='glyphicon glyphicon-trash'></div> Remove</button>"] )
		 .draw( false ); 
	 }
	 

	
}

function deleteGroup(event){
	$('#deleteGroupModal').modal('show');
	var id = event.id;
	window.eventId = id;
}

function editGroup(event){
	$('#editGroupModal').modal('show');
	var id = event.id;
	var objGroup = getGroupFromDB(id);
	$('#editGroupNameInput').val(objGroup.name);
	$('#editGroupDescriptionInput').val(objGroup.description);
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
	$.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url: url+'/openape/groups/'+groupId,
        dataType: "json",
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
   		 $('#deleteGroupModal').modal('hide');
         setTimeout(function(){ 
     		location.reload();
    		}, 1000);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	$('#deleteGroupModal').modal('hide');
            setTimeout(function(){ 
        		location.reload();
       		}, 1000);
        }
    });
}


function updateGroupDB(groupId, group) {
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: url+'/openape/groups/'+groupId,
        dataType: "json",
        data: group,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
   		 $('#addGroupModal').modal('hide');
         setTimeout(function(){ 
     		location.reload();
    		}, 1000);
        },
        error: function(jqXHR, textStatus, errorThrown){
      		 $('#addGroupModal').modal('hide');
             setTimeout(function(){ 
         		location.reload();
        		}, 1000);
        }
    });
}


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

function addGroupMemberToDB(groupId, userId) {
	var status = 0;
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: url+'/openape/'+groupId+"/members/"+userId,
        dataType: "json",
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
	$.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: url+'/openape/groups',
        dataType: "json",
        data: group,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
   		 $('#addGroupModal').modal('hide');
         setTimeout(function(){ 
     		location.reload();
    		}, 1000);
        },
        error: function(jqXHR, textStatus, errorThrown){
      		 $('#addGroupModal').modal('hide');
             setTimeout(function(){ 
         		location.reload();
        		}, 1000);
        }
    });
}