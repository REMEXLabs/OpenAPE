/**
 * 
 */
$(document).ready(function(){
	$('#editEnvironmentContextModal').on('hidden.bs.modal', function () {
		$('#editEnvironmentContextMainErrSection').empty();
	});
	
	$('#addEnvironmentContextModal').on('hidden.bs.modal', function () {
		$('#addEnvironmentContextMainErrSection').empty();
	});
	
	$('#editEquipmentContextModal').on('hidden.bs.modal', function () {
		$('#editEquipmentContextMainErrSection').empty();
	});
	
	$('#addEquipmentContextModal').on('hidden.bs.modal', function () {
		$('#addEquipmentContextMainErrSection').empty();
	});
	
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
    	var isPublic = $("#cbAddUserContext").is(':checked');
    	
    	if(validateInput(userContextJSON, "add", "UserContext") == true){
    	
	    	if(isPublic == true){
	    		var objUserContext = JSON.parse(userContextJSON);
	    		objUserContext.public = true;
	    	} else {
	    		var objUserContext = JSON.parse(userContextJSON);
	    	}
	    	
	    	validateContext(openape.createUserContext(JSON.stringify(objUserContext)), "add", "UserContext") == true ? location.reload() : void 0;
    	}
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
    	var isPublic = $("#cbEditEquipmentContext").is(':checked');
    	
    	if(validateInput(inputEquipmentContext, "edit", "EquipmentContext") == true){
	    	if(isPublic == true){
	    		var objEquipmentContext = JSON.parse(inputEquipmentContext);
	    		objEquipmentContext.public = true;
	    	} else {
	    		var objEquipmentContext = JSON.parse(inputEquipmentContext);
	    	}
	    	
	      	validateContext(openape.updateEquipmentContext(localStorage.getItem("id"), JSON.stringify(objEquipmentContext)), "edit", "EquipmentContext") == true ? location.reload() : void 0;
    	}
    })
    
    $('#btnConfirmEditTaskContext').click(function(){ 
    	var inputTaskContext = $('#inputAdministrationEditTaskContext').val();
    	var isPublic = $("#cbTaskContext").is(':checked');
    	
	    if(validateInput(inputTaskContext, "edit", "TaskContext") == true){
	    	if(isPublic == true){
	    		var objTaskContext = JSON.parse(inputTaskContext);
	    		objTaskContext.public = true;
	    	} else {
	    		var objTaskContext = JSON.parse(inputTaskContext);
	    	}
	    	
	      	validateContext(openape.updateTaskContext(localStorage.getItem("id"), JSON.stringify(objTaskContext)), "edit", "TaskContext") == true ? location.reload() : void 0;
	    }
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
    	var isPublic = $("#cbAddEnvironmentContext").is(':checked');
    	
    	
    	if(validateInput(environmentContextJSON, "add", "EnvironmentContext") == true){
    		if(isPublic == true){
        		var objEnvironmentContext = JSON.parse(environmentContextJSON);
        		objEnvironmentContext.public = true;
        	} else {
        		var objEnvironmentContext = JSON.parse(environmentContextJSON);
        	}
    		
    		validateContext(openape.createEnvironmentContext(JSON.stringify(objEnvironmentContext)), "add", "EnvironmentContext") == true ? location.reload() : void 0;	
    	}
    	
    })
    
    $('#btnAddTaskContext').click(function(){ 
    	$('#addTaskContextModal').modal('show');
    })
    
    $('#btnConfirmAddEquipmentContext').click(function(){ 
    	var equipmentContextJSON = $('#inputAdministrationAddEquipmentContext').val();  
    	var isPublic = $("#cbAddEquipmentContext").is(':checked');
    	
    	if(validateInput(equipmentContextJSON, "add", "EquipmentContext") == true){
    		if(isPublic == true){
        		var objEquipmentContext = JSON.parse(equipmentContextJSON);
        		objEquipmentContext.public = true;
        	} else {
        		var objEquipmentContext = JSON.parse(equipmentContextJSON);
        	}
    	}
    	
    	validateContext(openape.createEquipmentContext(JSON.stringify(objEquipmentContext)), "add", "EquipmentContext") == true ? location.reload() : void 0;	
    })
    
    $('#btnConfirmAddTaskContext').click(function(){ 
    	var taskContextJSON = $('#inputAdministrationAddTaskContext').val(); 
    	var isPublic = $("#cbAddTaskContext").is(':checked');
    	
    	if(validateInput(taskContextJSON, "add", "TaskContext") == true){
	    	if(isPublic == true){
	    		var objTaskContext = JSON.parse(taskContextJSON);
	    		objTaskContext.public = true;
	    	} else {
	    		var objTaskContext = JSON.parse(taskContextJSON);
	    	}
    	}
    	validateContext(openape.createTaskContext(JSON.stringify(objTaskContext)), "add", "TaskContext") == true ? window.location.reload() : void 0;	
    })
    
    $('#btnConfirmEditUserContext').click(function(){ 
    	var inputUserContext = $('#inputAdministrationEditUserContext').val();
    	
    	var isPublic = $("#cbEditUserContext").is(':checked');
    	
    	if(validateInput(inputUserContext, "edit", "UserContext") == true){
	    	if(isPublic == true){
	    		var objUserContext = JSON.parse(inputUserContext);
	    		objUserContext.public = true;
	    	} else {
	    		var objUserContext = JSON.parse(inputUserContext);
	    	}
 	
	    	validateContext(openape.updateUserContext(localStorage.getItem("id"), JSON.stringify(objUserContext)), "edit", "UserContext") == true ? location.reload() : void 0;
    	}
    })
    $('#btnConfirmEditEnvironmentContext').click(function(){ 
    	var inputEnvironmentContext = $('#inputAdministrationEditEnvironmentContext').val();
    	
    	var isPublic = $("#cbEditEnvironmentContext").is(':checked');
    	
    	if(validateInput(inputEnvironmentContext, "edit", "EnvironmentContext") == true){
    		if(isPublic == true){
        		var objEnvironmentContext = JSON.parse(inputEnvironmentContext);
        		objEnvironmentContext.public = true;
        	} else {
        		var objEnvironmentContext = JSON.parse(inputEnvironmentContext);
        	}
        	
          	validateContext(openape.updateEnvironmentContext(localStorage.getItem("id"), JSON.stringify(objEnvironmentContext)), "edit", "EnvironmentContext") == true ? location.reload() : void 0;
    	}
     })  
})


function validateContext (contextAction, contextActionName, contextName){
	var errSectionName =  "";
	
	contextActionName == "edit" ? errSectionName = "#edit"+contextName+"MainErrSection" : errSectionName = "#add"+contextName+"MainErrSection"; 
		
	var contextResponse = contextAction;
	
	if(contextResponse.status == 201 || contextResponse.status == 200){
		$(errSectionName).empty();
		return true; 
	} else {
		$(errSectionName).empty();
		$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'> Wrong JSON-Format");
		return false; 
	}
}

function validateInput (contextInput, contextActionName, contextName){
	var errSectionName =  "";
	contextActionName == "edit" ? errSectionName = "#edit"+contextName+"MainErrSection" : errSectionName = "#add"+contextName+"MainErrSection"; 
		
	if(contextInput == ""){
		$(errSectionName).empty();
		$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please add a task context");
		return false; 
	} else {
		return true;
	}
}

function getContext(id, contextName){
	switch(contextName){
		case "taskContext" : return JSON.parse(openape.getTaskContext(id).responseText); break;
		case "equipmentContext" : return JSON.parse(openape.getEquipmentContext(id).responseText); break;
		case "environmentContext" : return JSON.parse(openape.getEnvironmentContext(id).responseText); break;
		case "userContext" : return JSON.parse(openape.getUserContext(id).responseText); break;
	}	
}

//EDIT
function editEquipmentContext(event){
	$('#editEquipmentContextModal').modal('show');
	var isPublic = getContext(event.id, "equipmentContext").public;
	
	var objEquipmentContext = new Object();
	objEquipmentContext.propertys = getContext(event.id, "equipmentContext").propertys;
	
	if(isPublic == true){
		$("#cbEditEquipmentContext").prop('checked', true);
	} else {
		$("#cbEditEquipmentContext").prop('checked', false);
	}
	
	var equipmentContext = JSON.stringify(objEquipmentContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditEquipmentContext').val(equipmentContext);
}

function editTaskContext(event){
	$('#editTaskContextModal').modal('show');
	var isPublic = getContext(event.id, "taskContext").public;
	
	var objTaskContext = new Object();
	objTaskContext.propertys = getContext(event.id, "taskContext").propertys;
	
	if(isPublic == true){
		$("#cbEditTaskContext").prop('checked', true);
	} else {
		$("#cbEditTaskContext").prop('checked', false);
	}

	var taskContext = JSON.stringify(objTaskContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditTaskContext').val(taskContext);
}

function editEnvironmentContext(event){
	$('#editEnvironmentContextModal').modal('show');
	var isPublic = getContext(event.id, "environmentContext").public;
	
	var objEnvironmentContext = new Object();
	objEnvironmentContext.propertys = getContext(event.id, "environmentContext").propertys;

	if(isPublic == true){
		$("#cbEditEnvironmentContext").prop('checked', true);
	} else {
		$("#cbEditEnvironmentContext").prop('checked', false);
	}
	
	var environmentContext = JSON.stringify(objEnvironmentContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditEnvironmentContext').val(environmentContext);
}

function editUserContext(event){
	$('#editUserContextModal').modal('show');
	var isPublic = getContext(event.id, "userContext").public;
	
	var objUserContext = new Object();
	objUserContext.contexts = getContext(event.id, "userContext").contexts;
	
	if(isPublic == true){
		$("#cbEditUserContext").prop('checked', true);
	} else {
		$("#cbEditUserContext").prop('checked', false);
	}
	
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
	var isPublic = getContext(event.id, "taskContext").public;
	
	
	if(isPublic == true){
		objTaskContext.propertys = getContext(event.id, "taskContext").propertys;
		objTaskContext.public = true;
	} else {
		objTaskContext.propertys = getContext(event.id, "taskContext").propertys;
		objTaskContext.public = false;
	}

	var taskContext = JSON.stringify(objTaskContext);
	if(openape.createTaskContext(taskContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function copyEquipmentContext(event){
	var objEquipmentContext = new Object();
	var isPublic = getContext(event.id, "equipmentContext").public;
	
	if(isPublic == true){
		objEquipmentContext.propertys = getContext(event.id, "equipmentContext").propertys;
		objEquipmentContext.public = true;
	} else {
		objEquipmentContext.propertys = getContext(event.id, "equipmentContext").propertys;
		objEquipmentContext.public = false;
	}
	
	var equipmentContext = JSON.stringify(objEquipmentContext);
	if(openape.createEquipmentContext(equipmentContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function copyEnvironmentContext(event){
	var objEnvironmentContext = new Object();
	var isPublic = getContext(event.id, "environmentContext").public;
	
	if(isPublic == true){
		objEnvironmentContext.propertys = getContext(event.id, "environmentContext").propertys;
		objEnvironmentContext.public = true;
	} else {
		objEnvironmentContext.propertys = getContext(event.id, "environmentContext").propertys;
		objEnvironmentContext.public = false;
	}
	
	var environmentContext = JSON.stringify(objEnvironmentContext);
	if(openape.createEnvironmentContext(environmentContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function copyUserContext(event){
	var objUserContext = new Object();
	var isPublic = getContext(event.id, "userContext").public;
	
	if(isPublic == true){
		objUserContext.contexts = getContext(event.id, "userContext").contexts;
		objUserContext.public = true;
	} else {
		objUserContext.contexts = getContext(event.id, "userContext").contexts;
		objUserContext.public = false;
	}
	
	var userContext = JSON.stringify(objUserContext);
	if(openape.createUserContext(userContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}
