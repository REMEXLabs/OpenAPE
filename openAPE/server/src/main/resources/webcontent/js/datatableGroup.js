var url = window.location.origin;
$(document).ready(function() {
	var resourceTable = $('#groupDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
		"responsive": true
    } ); 
	
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
		addGroupToDB(JSON.stringify(objGroup));		
	})
	
	$('#btnConfirmDeleteGroup').click(function () {
		
		window.eventId
	})
	
	
	    
})



function deleteGroup(event){
	$('#deleteGroupModal').modal('show');
	var id = event.id;
	window.eventId = id;
}

function removeGroupFromDB(groupId) {
	$.ajax({
        type: 'DELETE',
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