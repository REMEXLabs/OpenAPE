/**
 * 
 */
$(document).ready(function(){
	if(window.location.href.indexOf("#") == -1){
		window.location.hash = "#user-contexts"
		openCity(event, "user-contexts");
	} else {
		if(window.location.hash == "#task-contexts"){
			openCity(event, "task-contexts");
			$('#trTabTaskContexts').attr("style", "background-color:#e8e5e5");
		} else if(window.location.hash == "#equipment-contexts"){
			openCity(event, "equipment-contexts");
		} else if(window.location.hash == "#environment-contexts"){
			openCity(event, "environment-contexts");
		} else if(window.location.hash == "#user-contexts"){
			openCity(event, "user-contexts");
		}
	}
	
	var contexts = ["equipmentContext", "taskContext", "environmentContext", "userContext"];

	for(i=0; i<contexts.length;i++){
		var table = $('#'+contexts[i]+'DataTable').DataTable( {
			"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
	    } ); 
		// Hide a column
		table.column( 1 ).visible( false );
		
		
		$('#'+contexts[i]+'DataTable').find("th").each(function() { 
			if($(this).text() == "UserID"){
				var index = $(this).index();
				$('#'+contexts[i]+'DataTable').find("td").each(function() { 
					if($(this).index() == index){
						if($(this).text().indexOf(localStorage.getItem("userid")) == -1){
							$('#'+contexts[i]+'DataTable').DataTable().row( $(this).parents('tr') )
					        .remove()
					        .draw();
						}
					}
				})
			}
		})
	}
	
	//show modal by clicking the add button
    $('#btnAddUserContext').click(function(){ 
    	$('#addUserContextModal').modal('show');
    })
    
    $('#btnConfirmAddUserContext').click(function(){ 
    	var userContextJSON = $('#inputAdministrationAddUserContext').val();   	
    	validateContext(userContextJSON, openape.createUserContext(userContextJSON), "add", "UserContext") == true ? location.reload() : void 0;	
    })
	
    $('#btnConfirmDeleteTaskContext').click(function(){ 
    	openape.deleteTaskContext(localStorage.getItem("id")); 
    	$('#deleteTaskContextModal').modal('hide');
    	setTimeout(function(){ 
    		location.reload();
   		}, 1000);
    })
    
    $('#btnConfirmDeleteEquipmentContext').click(function(){ 
    	openape.deleteEquipmentContext(localStorage.getItem("id")); 
    	$('#deleteEquipmentContextModal').modal('hide');
    	setTimeout(function(){ 
    		location.reload();
   		}, 1000);
    })
    
    $('#btnConfirmDeleteEnvironmentContext').click(function(){ 
    	openape.deleteEnvironmentContext(localStorage.getItem("id")); 
    	$('#deleteEnvironmentContextModal').modal('hide');
    	setTimeout(function(){ 
    		location.reload();
   		}, 1000);
    })
    
    $('#btnConfirmDeleteUserContext').click(function(){ 
        openape.deleteUserContext(localStorage.getItem("id"));   	
    	setTimeout(function(){ 
    		location.reload();
   		}, 1000);
    })
  
    $('#btnConfirmEditEquipmentContext').click(function(){ 
    	var inputEquipmentContext = $('#inputAdministrationEditEquipmentContext').val();
      	validateContext(inputEquipmentContext, openape.updateEquipmentContext(localStorage.getItem("id"), inputEquipmentContext), "edit", "EquipmentContext") == true ? location.reload() : void 0;
    })
    
    $('#btnConfirmEditTaskContext').click(function(){ 
    	var inputTaskContext = $('#inputAdministrationEditTaskContext').val();
      	validateContext(inputTaskContext, openape.updateTaskContext(localStorage.getItem("id"), inputTaskContext), "edit", "TaskContext") == true ? location.reload() : void 0;
    }) 
   
    	//show modal by clicking the add button
    $('#btnAddEquipmentContext').click(function(){ 
    	$('#addEquipmentContextModal').modal('show');
    })
    
    //show modal by clicking the add button
    $('#btnAddEnvironmentContext').click(function(){ 
    	$('#addEnvironmentContextModal').modal('show');
    })
    
     $('#btnConfirmAddEnvironmentContext').click(function(){ 
    	var environmentContextJSON = $('#inputAdministrationAddEnvironmentContext').val();   	
    	validateContext(environmentContextJSON, openape.createEnvironmentContext(environmentContextJSON), "add", "EnvironmentContext") == true ? location.reload() : void 0;	
    })
    
    $('#btnAddTaskContext').click(function(){ 
    	$('#addTaskContextModal').modal('show');
    })
    
    $('#btnConfirmAddEquipmentContext').click(function(){ 
    	var equipmentContextJSON = $('#inputAdministrationAddEquipmentContext').val();   	
    	validateContext(equipmentContextJSON, openape.createEquipmentContext(equipmentContextJSON), "add", "EquipmentContext") == true ? location.reload() : void 0;	
    })
    
    $('#btnConfirmAddTaskContext').click(function(){ 
    	var taskContextJSON = $('#inputAdministrationAddTaskContext').val();   	
    	validateContext(taskContextJSON, openape.createTaskContext(taskContextJSON), "add", "TaskContext") == true ? window.location.reload() : void 0;	
    })
    
    $('#btnConfirmEditUserContext').click(function(){ 
    	var inputUserContext = $('#inputAdministrationEditUserContext').val();
      	validateContext(inputUserContext, openape.updateUserContext(localStorage.getItem("id"), inputUserContext), "edit", "UserContext") == true ? location.reload() : void 0;
    })
})


function validateContext (contextInput, contextAction, contextActionName, contextName){
	var errSectionName =  "";
	
	contextActionName == "edit" ? errSectionName = "#edit"+contextName+"MainErrSection" : errSectionName = "#add"+contextName+"MainErrSection"; 
		
	if(contextInput != ""){
		var contextResponse = contextAction;
		
		if(contextResponse.status == 201 || contextResponse.status == 200){
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

function getContext(id, contextName){
	switch(contextName){
		case "taskContext" : return JSON.parse(openape.getTaskContext(id).responseText).propertys; break;
		case "equipmentContext" : return JSON.parse(openape.getEquipmentContext(id).responseText).propertys; break;
		case "environmentContext" : return JSON.parse(openape.getEnvironmentContext(id).responseText).propertys; break;
		case "userContext" : return JSON.parse(openape.getUserContext(id).responseText).contexts; break;
	}	
}

//EDIT
function editEquipmentContext(event){
	$('#editEquipmentContextModal').modal('show');
	var objEquipmentContext = new Object();
	objEquipmentContext.propertys = getContext(event.id, "equipmentContext");
	var equipmentContext = JSON.stringify(objEquipmentContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditEquipmentContext').val(equipmentContext);
}

function editTaskContext(event){
	$('#editTaskContextModal').modal('show');
	var objTaskContext = new Object();
	objTaskContext.propertys = getContext(event.id, "taskContext");
	var taskContext = JSON.stringify(objTaskContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditTaskContext').val(taskContext);
}

function editEnvironmentContext(event){
	$('#editEnvironmentContextModal').modal('show');
	var objEnvironmentContext = new Object();
	objEnvironmentContext.propertys = getContext(event.id, "environmentContext");
	var environmentContext = JSON.stringify(objEnvironmentContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditEnvironmentContext').val(environmentContext);
}

function editUserContext(event){
	$('#editUserContextModal').modal('show');
	var objUserContext = new Object();
	objUserContext.contexts = getContext(event.id, "userContext");
	var userContext = JSON.stringify(objUserContext);

	$('#inputAdministrationEditUserContext').val(userContext);
	localStorage.setItem("id", event.id);
}

//DELETE
function deleteEquipmentContext(event){
	$('#deleteEquipmentContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}
function deleteTaskContext(event){
	$('#deleteTaskContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

function deleteEnvironmentContext(event){
	$('#deleteEnvironmentContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

function deleteUserContext(event){
	$('#deleteUserContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

//COPY
function copyTaskContext(event){
	var objTaskContext = new Object();
	objTaskContext.propertys = getContext(event.id, "taskContext");
	var taskContext = JSON.stringify(objTaskContext);
	if(openape.createTaskContext(taskContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function copyEquipmentContext(event){
	var objEquipmentContext = new Object();
	objEquipmentContext.propertys = getContext(event.id, "equipmentContext");
	var equipmentContext = JSON.stringify(objEquipmentContext);
	if(openape.createEquipmentContext(equipmentContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function copyEnvironmentContext(event){
	var objEnvironmentContext = new Object();
	objEnvironmentContext.propertys = getContext(event.id, "environmentContext");
	var environmentContext = JSON.stringify(objEnvironmentContext);
	if(openape.createEnvironmentContext(environmentContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function copyUserContext(event){
	var objUserContext = new Object();
	objUserContext.contexts = getContext(event.id, "userContext");
	var userContext = JSON.stringify(objUserContext);
	if(openape.createUserContext(userContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}
