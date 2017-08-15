/**
 * 
 */
$(document).ready(function(){
	var currentUrl = window.location.protocol + "//"+window.location.host;
	
	
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
	
	if(window.location.href.indexOf("myContexts") != -1){
		if(window.location.href.indexOf("#") == -1){
			window.location.hash = "#user-contexts"
			openCity(event, "user-contexts");
		} else {
			if(window.location.hash ==	 "#task-contexts"){
				openCity(event, "task-contexts");
				$('#trTabTaskContexts').attr("style", "background-color:#e8e5e5");
				$('#linkMyContexts').addClass("active");
			} else if(window.location.hash == "#equipment-contexts"){
				openCity(event, "equipment-contexts");
				$('#linkMyContexts').addClass("active");
			} else if(window.location.hash == "#environment-contexts"){
				openCity(event, "environment-contexts");
				$('#linkMyContexts').addClass("active");
			} else if(window.location.hash == "#user-contexts"){
				openCity(event, "user-contexts");
				$('#linkMyContexts').addClass("active");
			}
		}
		
	} else if (window.location.href.indexOf("/context") != -1 ) {
		
		var hash = window.location.hash.substring(1, window.location.hash.length);
		$('button').each(function() {
			var buttonName = $.trim($(this).text());
			if(buttonName == "Copy link to Clipboard"){
				$(this).attr("data-clipboard-text", currentUrl+"/api/"+hash+"/"+$(this).attr("id"));
			}
		})
		
		if(window.location.href.indexOf("#") == -1){
			$('#divContext').addClass("active");
			window.location.hash =	 "#user-contexts";
			openCity(event, "user-contexts");
		} else {
			if(window.location.hash ==	 "#task-contexts"){
				openCity(event, "task-contexts");
				$('#trTabTaskContexts').attr("style", "background-color:#e8e5e5");
				$('#divContext').addClass("active");
			} else if(window.location.hash == "#equipment-contexts"){
				openCity(event, "equipment-contexts");
				$('#divContext').addClass("active");
			} else if(window.location.hash == "#environment-contexts"){
				openCity(event, "environment-contexts");
				$('#divContext').addClass("active");
			} else if(window.location.hash == "#user-contexts"){
				openCity(event, "user-contexts");
				$('#divContext').addClass("active");
			}
		}
	}
	 		
	
	else {
		if(window.location.hash ==	 "#task-contexts"){
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
		
		if(window.location.href.indexOf("myContexts") != -1){
			

			$('#'+contexts[i]+'DataTable').find("th").each(function() { 
				
		
				if($(this).text() == "Owner"){
					var index = $(this).index();
					$('#'+contexts[i]+'DataTable').find("td").each(function() { 
						if($(this).index() == 1){
							if($(this).attr("id") != localStorage.getItem("userid")){
								$('#'+contexts[i]+'DataTable').DataTable().row( $(this).parents('tr') )
						        .remove()
						        .draw();
							}
						}
						
					})
				}
			})
			table.column( 1 ).visible( false );
		} else if(window.location.href.indexOf("administration") == -1){
			
			$('#'+contexts[i]+'DataTable').find("td").each(function() {
				if($(this).index() == 3){
					if($(this).text() == "false"){
						if($(this).text().indexOf(localStorage.getItem("userid")) == -1){
							$('#'+contexts[i]+'DataTable').DataTable().row( $(this).parents('tr') )
					        .remove()
					        .draw();
						}
					}
				}
				
			})
			
			table.column( 3 ).visible( false );
		}	
		
	}
	
	//show modal by clicking the add button
    $('#btnAddUserContext').click(function(){ 
    	$('#addUserContextModal').modal('show');
    })
    
    $('#btnConfirmAddUserContext').click(function(){ 
    	var userContextJSON = $('#inputAdministrationAddUserContext').val();   
    	var isPublic = $("#cbAddUserContext").is(':checked');
    	var contentType = $('#addSelContentType option:selected').text();
    	var parsedUserContext = "";
    	
    	if(validateInput(userContextJSON, "add", "UserContext") == true){
    	
	    	if(isPublic == true){
	    		var objUserContext = JSON.parse(userContextJSON);
	    		objUserContext.public = true;
	    	} else {
	    		var objUserContext = JSON.parse(userContextJSON);
	    	}
	    	
	    	if(contentType == "JSON") {
	    		parsedUserContext = JSON.stringify(objUserContext);
	      	} else {
	      		var x2js = new X2JS();
	      		parsedUserContext = x2js.json2xml_str(objUserContext);
	    	}
	    	
	  		validateContext(openape.createUserContext(parsedUserContext, contentType), "add", "UserContext") == true ? location.reload() : void 0;
    	}
    })
    
    
    function objectToXml(obj) {
        var xml = '';

        for (var prop in obj) {
            if (!obj.hasOwnProperty(prop)) {
                continue;
            }

            if (obj[prop] == undefined)
                continue;

            xml += "<" + prop + ">";
            if (typeof obj[prop] == "object")
                xml += objectToXml(new Object(obj[prop]));
            else
                xml += obj[prop];

            xml += "<!--" + prop + "-->";
        }

        return xml;
    }
	
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
    	var isPublic = $("#cbEditTaskContext").is(':checked');
    	
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
		case "equipmentContext" : console.log(id);return JSON.parse(openape.getEquipmentContext(id).responseText); break;
		case "environmentContext" : return JSON.parse(openape.getEnvironmentContext(id).responseText); break;
		case "userContext" : return JSON.parse(openape.getUserContext(id).responseText); break;
	}	
}

//View
function viewContext(event){
	var contextName = event.attributes[2].value;
	var contextNameLowerCase = contextName.substring(0, 1).toLowerCase()+contextName.substring(1);
	$('#view'+contextName+'Modal').modal('show');
	var objContext = new Object();
	
	if(contextName == "UserContext"){
		objContext.contexts = getContext(event.id, contextNameLowerCase).contexts;
	} else {
		objContext.propertys = getContext(event.id, contextNameLowerCase).propertys;
	}
	
	var context = JSON.stringify(objContext, undefined, 2);
	$('#inputView'+contextName+'').val(context);
}

//Edit
function editContext(event){
	var contextName = event.attributes[2].value;
	var contextNameLowerCase = contextName.substring(0, 1).toLowerCase()+contextName.substring(1);
	var isPublic = getContext(event.id, contextNameLowerCase).public;
	var objContext = new Object();
	
	$('#edit'+contextName+'Modal').modal('show');
	
	if(contextName != "UserContext"){
		objContext.propertys = getContext(event.id, contextNameLowerCase).propertys;
	} else {
		objContext.contexts = getContext(event.id, "userContext").contexts;
	}
	
	if(isPublic == true){
		$("#cbEdit"+contextName).prop('checked', true);
	} else {
		$("#cbEdit"+contextName).prop('checked', false);
	}
	
	var context = JSON.stringify(objContext, undefined, 2);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEdit'+contextName).val(context);
}


//DELETE
function deleteContext(event){
	var contextName = event.attributes[2].value;
	$('#delete'+contextName+'Modal').modal('show');
	localStorage.setItem("id", event.id);	
}

//COPY
function copyContext(event){
	var contextName = event.attributes[2].value;
	var contextNameLowerCase = contextName.substring(0, 1).toLowerCase()+contextName.substring(1);
	
	var objContext = new Object();
	var isPublic = getContext(event.id, contextNameLowerCase).public;
	var copyResponse = "";
	 
	if(isPublic == true){
		objContext.public = true;
	} else {
		objContext.public = false;
	}

	if(contextName == "UserContext"){
		objContext.contexts = getContext(event.id, "userContext").contexts;
		var context = JSON.stringify(objContext);
		copyResponse = openape.createUserContext(context, "JSON").status;
	} else {
		var context = JSON.stringify(objContext);
		
		if(contextName == "TaskContext"){
			copyResponse = openape.createTaskContext(context).status;
		} else if(contextName == "EnvironmentContext"){
			copyResponse = openape.createEnvironmentContext(context).status;
		} else if(contextName == "EquipmentContext"){
			copyResponse = openape.createEquipmentContext(context).status;
		}
	}
	
	if(copyResponse == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}
