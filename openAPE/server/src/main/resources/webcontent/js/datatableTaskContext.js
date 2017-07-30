var selectedRoles = [];
$(document).ready(function() {
	//resetting errormessages if closing the modal
	$('#editTaskContextModal').on('hidden.bs.modal', function () {
		$('#editTaskContextMainErrSection').empty();
	});
	
	$('#addTaskContextModal').on('hidden.bs.modal', function () {
		$('#addTaskContextMainErrSection').empty();
	});
	
	
	//initialising the datatable
	$('#taskContextDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } ); 
	
	//show modal by clicking the add button
    $('#btnAddTaskContext').click(function(){ 
    	$('#addTaskContextModal').modal('show');
    	
    })
    
    //usercontext actions
    $('#btnConfirmDeleteTaskContext').click(function(){ 
    	openape.deleteTaskContext(localStorage.getItem("id")); 
    	$('#deleteTaskContextModal').modal('hide');
    	setTimeout(function(){ 
    		$('#taskContextDataTable').DataTable().ajax.reload();
   		}, 1000);
    })
    
    $('#btnConfirmAddTaskContext').click(function(){ 
    	var taskContextJSON = $('#inputAdministrationAddTaskContext').val();   	
    	validateTaskContext(taskContextJSON, openape.createTaskContext(taskContextJSON), "add") == true ? location.reload() : void 0;	
    })
    
    $('#btnConfirmEditTaskContext').click(function(){ 
    	var inputTaskContext = $('#inputAdministrationEditTaskContext').val();
      	validateTaskContext(inputTaskContext, openape.updateTaskContext(localStorage.getItem("id"), inputTaskContext), "edit") == true ? location.reload() : void 0;
    })    
} );


function copyTaskContext(event){
	var objTaskContext = new Object();
	objTaskContext.propertys = getTaskContext(event.id);
	var taskContext = JSON.stringify(objTaskContext);
	if(openape.createTaskContext(taskContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function deleteTaskContext(event){
	$('#deleteTaskContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

function editTaskContext(event){
	$('#editTaskContextModal').modal('show');
	var objTaskContext = new Object();
	objTaskContext.propertys = getTaskContext(event.id);
	var taskContext = JSON.stringify(objTaskContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditTaskContext').val(taskContext);
}

function getTaskContext(id){
	return JSON.parse(openape.getTaskContext(id).responseText).propertys;
}

function validateTaskContext (taskContextInput, taskContextAction, taskContextActionName){
	var errSectionName =  "";
	taskContextActionName == "edit" ? errSectionName = "#editTaskContextMainErrSection" : errSectionName = "#addTaskContextMainErrSection";
	
	if(taskContextInput != ""){
		var taskContextResponse = taskContextAction;
		
		if(taskContextResponse.status == 201 || taskContextResponse.status == 200){
			$(errSectionName).empty();
			return true; 
		} else {
			$(errSectionName).empty();
			$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'> Wrong JSON-Format");
			return false; 
		}
	} else {
		$(errSectionName).empty();
		$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please add a task context");
		return false; 
	}
}


function removeTaskContext(taskContextId) {
	$.ajax({
	    type: 'DELETE',
	    contentType: 'application/json',
	    url: 'http://localhost:4567/api/task-contexts/'+taskContextId,
	    dataType: "json",
	    
	    success: function(data, textStatus, jqXHR){
	    	
	    },
	    error: function(jqXHR, textStatus, errorThrown){
	       // alert('Medicine information could be deleted');
	    }
	});
}
