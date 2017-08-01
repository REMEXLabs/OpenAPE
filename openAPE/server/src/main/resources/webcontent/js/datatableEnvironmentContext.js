var selectedRoles = [];
$(document).ready(function() {
	//resetting errormessages if closing the modal
	$('#editEnvironmentContextModal').on('hidden.bs.modal', function () {
		$('#editEnvironmentContextMainErrSection').empty();
	});
	
	$('#addEnvironmentContextModal').on('hidden.bs.modal', function () {
		$('#addEnvironmentContextMainErrSection').empty();
	});
	
	if(window.location.hash == "#environment-contexts"){
		openCity(event, "environment-contexts");
	}
	
	
	
	//initialising the datatable
	$('#environmentContextDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } ); 
	
	//show modal by clicking the add button
    $('#btnAddEnvironmentContext').click(function(){ 
    	$('#addEnvironmentContextModal').modal('show');
    	
    })
    
    //usercontext actions
    $('#btnConfirmDeleteEnvironmentContext').click(function(){ 
    	openape.deleteEnvironmentContext(localStorage.getItem("id")); 
    	$('#deleteEnvironmentContextModal').modal('hide');
    	setTimeout(function(){ 
    		localStorage.setItem("location", "environment-contexts");
    		location.reload();
   		}, 1000);
    })
    
    $('#btnConfirmAddEnvironmentContext').click(function(){ 
    	var environmentContextJSON = $('#inputAdministrationAddEnvironmentContext').val();   	
    	validateEnvironmentContext(environmentContextJSON, openape.createEnvironmentContext(environmentContextJSON), "add") == true ? location.reload() : void 0;	
    })
    
    $('#btnConfirmEditEnvironmentContext').click(function(){ 
    	var inputEnvironmentContext = $('#inputAdministrationEditEnvironmentContext').val();
      	validateEnvironmentContext(inputEnvironmentContext, openape.updateEnvironmentContext(localStorage.getItem("id"), inputEnvironmentContext), "edit") == true ? location.reload() : void 0;
    })    
} );


function copyEnvironmentContext(event){
	var objEnvironmentContext = new Object();
	objEnvironmentContext.propertys = getEnvironmentContext(event.id);
	var environmentContext = JSON.stringify(objEnvironmentContext);
	if(openape.createEnvironmentContext(environmentContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function deleteEnvironmentContext(event){
	$('#deleteEnvironmentContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

function editEnvironmentContext(event){
	$('#editEnvironmentContextModal').modal('show');
	var objEnvironmentContext = new Object();
	objEnvironmentContext.propertys = getEnvironmentContext(event.id);
	var environmentContext = JSON.stringify(objEnvironmentContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditEnvironmentContext').val(environmentContext);
}

function getEnvironmentContext(id){
	return JSON.parse(openape.getEnvironmentContext(id).responseText).propertys;
}

function validateEnvironmentContext (environmentContextInput, environmentContextAction, environmentContextActionName){
	var errSectionName =  "";
	environmentContextActionName == "edit" ? errSectionName = "#editEnvironmentContextMainErrSection" : errSectionName = "#addEnvironmentContextMainErrSection";
	
	if(environmentContextInput != ""){
		var environmentContextResponse = environmentContextAction;
		
		if(environmentContextResponse.status == 201 || environmentContextResponse.status == 200){
			$(errSectionName).empty();
			return true; 
		} else {
			$(errSectionName).empty();
			$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'> Wrong JSON-Format");
			return false; 
		}
	} else {
		$(errSectionName).empty();
		$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please add a environment-context");
		return false; 
	}
}


function removeEnvironmentContext(environmentContextId) {
	$.ajax({
	    type: 'DELETE',
	    contentType: 'application/json',
	    url: 'http://localhost:4567/api/environment-contexts/'+environmentContextId,
	    dataType: "json",
	});
}
