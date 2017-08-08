var selectedRoles = [];
$(document).ready(function() {
	if(window.location.hash == "#user-contexts"){
		$('#collapseTwo').addClass("in");
		openCity(event, "user-contexts");
		$('#trTabUserContexts').attr("style", "background-color:#e8e5e5");
	}
	
	//resetting errormessages if closing the modal
	$('#editUserContextModal').on('hidden.bs.modal', function () {
		$('#editUserContextMainErrSection').empty();
	});
	
	$('#addUserContextModal').on('hidden.bs.modal', function () {
		$('#addUserContextMainErrSection').empty();
	});
	
	
	//initialising the datatable
	$('#userContextDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } ); 
	
	//show modal by clicking the add button
    $('#btnAddUserContext').click(function(){ 
    	$('#addUserContextModal').modal('show');
    	
    })
    
    //usercontext actions
    $('#btnConfirmDeleteUserContext').click(function(){ 
    	openape.deleteUserContext(localStorage.getItem("id"));   	
    	setTimeout(function(){ 
    		location.reload();
   		}, 1000);
    })
    
    $('#btnConfirmAddUserContext').click(function(){ 
    	var userContextJSON = $('#inputAdministrationAddUserContext').val();   	
    	validateUserContext(userContextJSON, openape.createUserContext(userContextJSON), "add") == true ? location.reload() : void 0;	
    })
    
    $('#btnConfirmEditUserContext').click(function(){ 
    	var inputUserContext = $('#inputAdministrationEditUserContext').val();
      	validateUserContext(inputUserContext, openape.updateUserContext(localStorage.getItem("id"), inputUserContext), "edit") == true ? location.reload() : void 0;
    })    
} );


function copyUserContext(event){
	var objUserContext = new Object();
	objUserContext.contexts = getUserContext(event.id);
	var userContext = JSON.stringify(objUserContext);
	if(openape.createUserContext(userContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function deleteUserContext(event){
	$('#deleteUserContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

function editUserContext(event){
	$('#editUserContextModal').modal('show');
	var objUserContext = new Object();
	objUserContext.contexts = getUserContext(event.id);
	var userContext = JSON.stringify(objUserContext);

	$('#inputAdministrationEditUserContext').val(userContext);
	localStorage.setItem("id", event.id);
}

function getUserContext(id){
	console.log(openape.getUserContext(id).responseText);
	return JSON.parse(openape.getUserContext(id).responseText).contexts;
}

function validateUserContext (userContextInput, userContextAction, userContextActionName){
	var errSectionName =  "";
	userContextActionName == "edit" ? errSectionName = "#editUserContextMainErrSection" : errSectionName = "#addUserContextMainErrSection";
	
	if(userContextInput != ""){
		var addUserContextResponse = userContextAction;
		
		if(addUserContextResponse.status == 201 || addUserContextResponse.status == 200){
			$(errSectionName).empty();
			return true; 
		} else {
			$(errSectionName).empty();
			$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'> Wrong JSON-Format");
			return false; 
		}
	} else {
		$(errSectionName).empty();
		$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please add a user context");
		return false; 
	}
}


function removeUserContext(userContextId) {
	$.ajax({
	    type: 'DELETE',
	    contentType: 'application/json',
	    url: 'http://localhost:4567/api/user-contexts/'+userContextId,
	    dataType: "json",
	});
}
