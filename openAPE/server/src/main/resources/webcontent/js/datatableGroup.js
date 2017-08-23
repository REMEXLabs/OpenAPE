var url = window.location.origin;
$(document).ready(function() {
	var resourceTable = $('#groupDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
		"responsive": true
    } ); 
	
	
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
	
	var resourceTable = $('#userGroupDataTable').DataTable( {
		"lengthMenu": [[3], [3]],
		 "bLengthChange": false,
		 "bInfo": false,
    } ); 
	
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
	
	
	$('#btnConfirmDeleteGroupMember').click(function () {
		var groupId = window.groupId;
		var userId = $('#deleteGroupMemberNameInput').val();
		
		deleteGroupMemberFromDB(groupId, userId);
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
})


function addGroupMember (event){
	var id = event.id;
	window.groupId = id;
	
	$('#addGroupUserModal').modal('show');
}

function deleteGroupMember(event){
	var id = event.id;
	window.groupId = id;
	
	$('#deleteGroupUserModal').modal('show');
}

function deleteGroup(event){
	$('#deleteGroupModal').modal('show');
	var id = event.id;
	window.eventId = id;
}

function editGroup(event){
	$('#editGroupModal').modal('show');
	var id = event.id;
	getGroupFromDB(id);
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
	$.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: url+'/openape/groups/'+groupId,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	var objGroup = JSON.parse(jqXHR.responseText);
    		$('#editGroupNameInput').val(objGroup.name);
    		$('#editGroupDescriptionInput').val(objGroup.description);
        	
        },
        error: function(jqXHR, textStatus, errorThrown){
        	console.log(jqXHR);
        }
    });
}

function addGroupMemberToDB(groupId, userId) {
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: url+'/openape/'+groupId+"/members/"+userId,
        dataType: "json",
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
	        	$('#addGroupUserModal').modal('hide');
	        	setTimeout(function(){ 
	         		location.reload();
	        		}, 1000);
	        }
        }
    });
}

function deleteGroupMemberFromDB (groupId, userId){
	$.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url: url+'/openape/'+groupId+"/members/"+userId,
        dataType: "json",
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	$('#deleteGroupUserModal').modal('hide');
        	 setTimeout(function(){ 
          		location.reload();
         		}, 1000);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	$('#deleteGroupUserModal').modal('hide');
        	 setTimeout(function(){ 
          		location.reload();
         		}, 1000);
        }
    });
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