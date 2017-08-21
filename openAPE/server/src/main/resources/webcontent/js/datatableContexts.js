/**
 * 
 */
$(document).ready(function(){
	var currentUrl = window.location.protocol + "//"+window.location.host;
	
	$('#viewEnvironmentContextOutputSelContentType').on('change', function() {
		var outputType = $("#viewEnvironmentContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("contextId");
		var objEnvironmentContext = {};
		
		if(outputType == "JSON"){
			$('#inputViewEnvironmentContext').val("");
			objEnvironmentContext.propertys = JSON.parse(openape.getEnvironmentContext(id, "JSON").responseText).propertys; 
			$('#inputViewEnvironmentContext').val(JSON.stringify(objEnvironmentContext, undefined, 2));
		} else {
			$('#inputViewEnvironmentContext').val("");
			var responseXML = openape.getEnvironmentContext(id, "XML").responseText;
			var formatedXML = "<environment-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			$('#inputViewEnvironmentContext').val(prettyXML);
		}
	})
	
	$('#viewTaskContextOutputSelContentType').on('change', function() {
		var outputType = $("#viewTaskContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("contextId");
		var objTaskContext = {};
		
		if(outputType == "JSON"){
			$('#inputViewTaskContext').val("");
			objTaskContext.propertys = JSON.parse(openape.getTaskContext(id, "JSON").responseText).propertys; 
			$('#inputViewTaskContext').val(JSON.stringify(objTaskContext, undefined, 2));
		} else {
			var responseXML = openape.getTaskContext(id, "XML").responseText;
			var formatedXML = "<task-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputViewTaskContext').val("");
			$('#inputViewTaskContext').val(prettyXML);
		}
	})
	
	
	$('#viewEquipmentContextOutputSelContentType').on('change', function() {
		var outputType = $("#viewEquipmentContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("contextId");
		var objEquipmentContext = {};
		
		if(outputType == "JSON"){
			$('#inputViewEquipmentContext').val("");
			objEquipmentContext.propertys = JSON.parse(openape.getEquipmentContext(id, "JSON").responseText).propertys; 
			$('#inputViewEquipmentContext').val(JSON.stringify(objEquipmentContext, undefined, 2));
		} else {
			var responseXML = openape.getEquipmentContext(id, "XML").responseText;
			var formatedXML = "<equipment-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputViewEquipmentContext').val("");
			$('#inputViewEquipmentContext').val(prettyXML);
		}
	})
	
	
	$('#viewUserContextOutputSelContentType').on('change', function() {
		var outputType = $("#viewUserContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("contextId");
		
		if(outputType == "JSON"){
			$('#inputViewUserContext').val("");
			var json = JSON.parse(openape.getUserContext(id, "JSON").responseText); 
			delete json.public;
			$('#inputViewUserContext').val(JSON.stringify(json, undefined, 2));
		} else {
			var responseXML = openape.getUserContext(id, "XML").responseText;
			var formatedXML = "<user-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputViewUserContext').val("");
			$('#inputViewUserContext').val(prettyXML);
		}
	})
	

	$('#editUserContextOutputSelContentType').on('change', function() {
		var outputType = $("#editUserContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("id");
		
		if(outputType == "JSON"){
			$('#inputAdministrationEditUserContext').val("");
			var json = JSON.parse(openape.getUserContext(id, "JSON").responseText); 
			$('#inputAdministrationEditUserContext').val(JSON.stringify(json, undefined, 2));
		} else {
			var responseXML = openape.getUserContext(id, "XML").responseText;
			var formatedXML = "<user-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputAdministrationEditUserContext').val("");
			$('#inputAdministrationEditUserContext').val(prettyXML);
		}
	})
	
	
	
	$('#editEnvironmentContextOutputSelContentType').on('change', function() {
		var outputType = $("#editEnvironmentContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("id");
		
		if(outputType == "JSON"){
			$('#inputAdministrationEditEnvironmentContext').val("");
			var json = JSON.parse(openape.getEnvironmentContext(id, "JSON").responseText); 
			$('#inputAdministrationEditEnvironmentContext').val(JSON.stringify(json, undefined, 2));
		} else {
			var responseXML = openape.getEnvironmentContext(id, "XML").responseText;
			var formatedXML = "<environment-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputAdministrationEditEnvironmentContext').val("");
			$('#inputAdministrationEditEnvironmentContext').val(prettyXML);
		}
	})
	

	$('#editTaskContextOutputSelContentType').on('change', function() {
		var outputType = $("#editTaskContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("id");
		
		if(outputType == "JSON"){
			$('#inputAdministrationEditTaskContext').val("");
			var json = JSON.parse(openape.getTaskContext(id, "JSON").responseText); 
			$('#inputAdministrationEditTaskContext').val(JSON.stringify(json, undefined, 2));
		} else {
			var responseXML = openape.getTaskContext(id, "XML").responseText;
			var formatedXML = "<task-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputAdministrationEditTaskContext').val("");
			$('#inputAdministrationEditTaskContext').val(prettyXML);
		}
	})
	
	$('#editEquipmentContextOutputSelContentType').on('change', function() {
		var outputType = $("#editEquipmentContextOutputSelContentType option:selected").text();
		var id = localStorage.getItem("id");
		
		if(outputType == "JSON"){
			$('#inputAdministrationEditEquipmentContext').val("");
			var json = JSON.parse(openape.getEquipmentContext(id, "JSON").responseText); 
			$('#inputAdministrationEditEquipmentContext').val(JSON.stringify(json, undefined, 2));
		} else {
			var responseXML = openape.getEquipmentContext(id, "XML").responseText;
			var formatedXML = "<equipment-context>"+responseXML.substring(responseXML.indexOf("</id>")+5);
			var prettyXML = formatXml(formatedXML);
			
			$('#inputAdministrationEditEquipmentContext').val("");
			$('#inputAdministrationEditEquipmentContext').val(prettyXML);
		}
	})
	
	
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
   
//------ADD CONTEXT MODAL INITIAL----------------------------------------------------------------------------
    $('#btnAddEquipmentContext').click(function(){ 
    	$('#addEquipmentContextOutputSelContentType').css("display", "none");
    	$('#addEquipmentContextlbOutput').css("display", "none");
    	$('#addEquipmentContextdivPublicCb').css("margin-right", "19em"); 
    	
    	$('#addEquipmentContextModal').modal('show');
    })
    
    //show modal by clicking the add button
    $('#btnAddEnvironmentContext').click(function(){ 
    	$('#addEnvironmentContextOutputSelContentType').css("display", "none");
    	$('#addEnvironmentContextlbOutput').css("display", "none");
    	$('#addEnvironmentContextdivPublicCb').css("margin-right", "19em"); 
    	
    	$('#addEnvironmentContextModal').modal('show');
    })
    
    	
	//show modal by clicking the add button
    $('#btnAddUserContext').click(function(){ 
    	$('#addUserContextOutputSelContentType').css("display", "none");
    	$('#addUserContextlbOutput').css("display", "none");
    	$('#addUserContextdivPublicCb').css("margin-right", "19em"); 
    	
    	$('#addUserContextModal').modal('show');
    })
    
    $('#btnAddTaskContext').click(function(){ 
    	$('#addTaskContextOutputSelContentType').css("display", "none");
    	$('#addTaskContextlbOutput').css("display", "none");
    	$('#addTaskContextdivPublicCb').css("margin-right", "19em"); 
    	
    	$('#addTaskContextModal').modal('show');
    })
    
    
//-----ADD CONTEXT FUNCTIONS--------------------------------------------------------------------------------------
    $('#btnConfirmAddUserContext').click(function(){ 
    	var userContext = $('#inputAdministrationAddUserContext').val();   
    	var isPublic = $("#cbAddUserContext").is(':checked');
    	var contentType = $('#addUserContextSelContentType option:selected').text();
    	var parsedUserContext = "";   	
    	var objUserContext = {};
    	var isFormatCorrect = true;
    	
    	if(validateInput(userContext, "add", "UserContext") == true){
	    	if(contentType == "JSON") {
	    		if(isJSON(userContext)){
		    		objUserContext = JSON.parse(userContext);
	    			
		    		if(isPublic){
	    				objUserContext.public = true;
	    			}
		    		
		    		parsedUserContext = JSON.stringify(objUserContext);
		    		isFormatCorrect = true;
	    		} else {
	    			isFormatCorrect = false;
	    			$('#addUserContextMainErrSection').empty();
          			$('#addUserContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No JSON found. Wrong Content-Type!");
	    		}
	    		
	      	} else {
	      		if(isXML(userContext)){
	      			var x2js = new X2JS();
		      		objUserContext = x2js.xml_str2json(userContext);
		      		
		      		if(isPublic){
        				var setPublicAttribute = userContext.replace("<user-context>", "<user-context public='true'>");
        				parsedUserContext = setPublicAttribute;
		      		} else {
		      			parsedUserContext = userContext;
		      		}
		      		isFormatCorrect = true;
	      		} else {
	      			isFormatCorrect = false;
	    			$('#addUserContextMainErrSection').empty();
          			$('#addUserContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No XML found. Wrong Content-Type!");
	      		}
	    	}
	    	
      		if(isPublic == true){
	    		objUserContext.public = true;
	    	} else {
	    		objUserContext.public = false;
	    	}
	    	
      		if(isFormatCorrect){
      			validateContext(openape.createUserContext(parsedUserContext, contentType), "add", "UserContext") == true ? location.reload() : void 0;
      		}
      	}
    })
    
     $('#btnConfirmAddEnvironmentContext').click(function(){ 
    	var environmentContext = $('#inputAdministrationAddEnvironmentContext').val();   
    	var isPublic = $("#cbAddEnvironmentContext").is(':checked');
    	var contentType = $('#addEnvironmentContextSelContentType option:selected').text();
    	var objEnvironmentContext = {};
    	var parsedEnvironmentContext = "";
    	var isFormatCorrect = true;
    	
    	if(validateInput(environmentContext, "add", "EnvironmentContext") == true){
        	if(contentType == "JSON") {
        		if(isJSON(environmentContext)){
        			objEnvironmentContext = JSON.parse(environmentContext);
		    		
        			if(isPublic){
        				objEnvironmentContext.public = true;
	    			}
        			
            		parsedEnvironmentContext = JSON.stringify(objEnvironmentContext);
            		isFormatCorrect = true;
        		} else {
        			isFormatCorrect = false;
        			$('#addEnvironmentContextMainErrSection').empty();
          			$('#addEnvironmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No JSON found. Wrong Content-Type!");
        		}
        		
          	} else {
          		if(isXML(environmentContext)){
          			if(isPublic){
        				var setPublicAttribute = environmentContext.replace("<environment-context>", "<environment-context public='true'>");
        				parsedEnvironmentContext = setPublicAttribute;
          			} else {
          				parsedEnvironmentContext  = environmentContext ;
          			}
              		isFormatCorrect = true;
          		} else {
          			isFormatCorrect = false;		
          			$('#addEnvironmentContextMainErrSection').empty();
          			$('#addEnvironmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No XML found. Wrong Content-Type!"); 	
          		}
        	}
    		
    		if(isFormatCorrect){
    			validateContext(openape.createEnvironmentContext(parsedEnvironmentContext, contentType), "add", "EnvironmentContext") == true ? location.reload() : void 0;	
    		}
    	}	
    })
    
    $('#btnConfirmAddEquipmentContext').click(function(){ 
    	var equipmentContext = $('#inputAdministrationAddEquipmentContext').val();  
    	var isPublic = $("#cbAddEquipmentContext").is(':checked');
    	var contentType = $('#addEquipmentContextSelContentType option:selected').text();
    	var objEquipmentContext = {};
    	var parsedEquipmentContext = "";
    	var isFormatCorrect = true;
    	
    	if(validateInput(equipmentContext, "add", "EquipmentContext") == true){
        	if(contentType == "JSON") {
        		if(isJSON(equipmentContext)){
	        		objEquipmentContext = JSON.parse(equipmentContext);
        			
	        		if(isPublic){
	        			objEquipmentContext.public = true;
	    			}
        			
	        		parsedEquipmentContext = JSON.stringify(objEquipmentContext);
	        		isFormatCorrect = true;
        		} else {
        			isFormatCorrect = false;
        			$('#addEquipmentContextMainErrSection').empty();
          			$('#addEquipmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No JSON found. Wrong Content-Type!"); 	
        		}
          	} else {
          		if(isXML(equipmentContext)){
          			if(isPublic){
        				var setPublicAttribute = equipmentContext.replace("<equipment-context>", "<equipment-context public='true'>");
        				parsedEquipmentContext = setPublicAttribute;
          			} else {
          				parsedEquipmentContext  = equipmentContext ;
          			}
          			isFormatCorrect = true;
          		} else {
          			isFormatCorrect = false;
           			$('#addEquipmentContextMainErrSection').empty();
          			$('#addEquipmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No XML found. Wrong Content-Type!"); 	
          		}
        	}
        	
        	if(isFormatCorrect){
        		validateContext(openape.createEquipmentContext(parsedEquipmentContext, contentType), "add", "EquipmentContext") == true ? location.reload() : void 0;	
        	}
    	}
    })
    
    $('#btnConfirmAddTaskContext').click(function(){ 
    	var taskContext = $('#inputAdministrationAddTaskContext').val(); 
    	var isPublic = $("#cbAddTaskContext").is(':checked');
    	var contentType = $('#addTaskContextSelContentType option:selected').text();
    	var objTaskContext = {};
    	var parsedTaskContext = "";
    	var isFormatCorrect = true;
    	
    	if(validateInput(taskContext, "add", "TaskContext") == true){
        	if(contentType == "JSON") {
        		if(isJSON(taskContext)){
	        		objTaskContext = JSON.parse(taskContext);
	        		
	        		if(isPublic){
	        			objTaskContext.public = true;
	    			}
        			
	        		parsedTaskContext = JSON.stringify(objTaskContext);
	        		isFormatCorrect = true;
        		} else {
        			isFormatCorrect = false;
           			$('#addTaskContextMainErrSection').empty();
          			$('#addTaskContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No JSON found. Wrong Content-Type!"); 	
        		}
          	} else {
          		if(isXML(taskContext)){
          			isFormatCorrect = true;
          			
          			if(isPublic){
        				var setPublicAttribute = taskContext.replace("<task-context>", "<task-context public='true'>");
        				parsedTaskContext = setPublicAttribute;
          			} else {
          				parsedTaskContext  = taskContext ;
          			}
          			
          		} else {
          			isFormatCorrect = false;
           			$('#addTaskContextMainErrSection').empty();
          			$('#addTaskContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'> No XML found. Wrong Content-Type!"); 	
          		}
        	}
        	
        	if(isFormatCorrect){
        		validateContext(openape.createTaskContext(parsedTaskContext, contentType), "add", "TaskContext") == true ? window.location.reload() : void 0;	
        	}
    	}
    })
    
    //-----EDIT CONTEXT FUNCTIONS---------------------------------------------------------------------
    
    $('#btnConfirmEditUserContext').click(function(){ 
    	var userContext = $('#inputAdministrationEditUserContext').val();
    	var contentType = $('#editUserContextSelContentType option:selected').text();	
    	var isPublic = $("#cbEditUserContext").is(':checked');
    	var objUserContext = {};
    	var parsedUserContext = "";
    	var isFormatCorrect = true;
    	
    	if(validateInput(userContext, "edit", "UserContext") == true){
        	if(contentType == "JSON") {
        		if(isJSON(userContext)){
	        		objUserContext = JSON.parse(userContext);
	        		       			
        			if(isPublic){
        				objUserContext.public = true;
	    			}		
	        		
	        		parsedUserContext = JSON.stringify(objUserContext);
	        		isFormatCorrect = true;
        		} else {
        			$('#editUserContextMainErrSection').empty();
        			$('#editUserContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No JSON found. Wrong Content-Type!");
          			isFormatCorrect = false;
        		}
          	} else {
          		if(isXML(userContext)){
        			if(isPublic){
        				var setPublicAttribute = userContext.replace("<user-context>", "<user-context public='true'>");
        				parsedUserContext = setPublicAttribute;
	    			} else {
	    				parsedUserContext = userContext;
	    			}
        			        			
          			isFormatCorrect = true;
          		} else {
          			$('#editUserContextMainErrSection').empty();
          			$('#editUserContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No XML found. Wrong Content-Type!");
          			isFormatCorrect = false;
          		}
        	}
 	
        	if(isFormatCorrect){
        		validateContext(openape.updateUserContext(localStorage.getItem("id"), parsedUserContext, contentType), "edit", "UserContext") == true ? location.reload() : void 0;
        	}
    	}
    })
    
     $('#btnConfirmEditEquipmentContext').click(function(){ 
    	var equipmentContext = $('#inputAdministrationEditEquipmentContext').val();
    	var isPublic = $("#cbEditEquipmentContext").is(':checked');
    	var isFormatCorrect = true;
    	var parsedEquipmentContext = "";
    	var contentType = $('#editEquipmentContextSelContentType option:selected').text();
    	var objEquipmentContext = {};
    	
    	if(validateInput(equipmentContext, "edit", "EquipmentContext") == true){
        	if(contentType == "JSON") {
        		if(isJSON(equipmentContext)){
        			objEquipmentContext = JSON.parse(equipmentContext);
        			
        			if(isPublic){
        				objEquipmentContext.public = true;
	    			}
        			
        			parsedEquipmentContext = JSON.stringify(objEquipmentContext);
	        		isFormatCorrect = true;
        		} else {
        			$('#editEquipmentContextMainErrSection').empty();
        			$('#editEquipmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No JSON found. Wrong Content-Type!");
          			isFormatCorrect = false;
        		}
          	} else {
          		if(isXML(equipmentContext)){
    				
          			if(isPublic){
        				var setPublicAttribute = equipmentContext.replace("<equipment-context>", "<equipment-context public='true'>");
        				parsedEquipmentContext = setPublicAttribute;
          			} else {
          				parsedEquipmentContext = equipmentContext;
          			}
          			
          			
          			isFormatCorrect = true;
          		} else {
          			$('#editEquipmentContextMainErrSection').empty();
          			$('#editEquipmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No XML found. Wrong Content-Type!");
          			isFormatCorrect = false;
          		}
        	}
	    	
	    	if(isFormatCorrect){
	    		validateContext(openape.updateEquipmentContext(localStorage.getItem("id"), parsedEquipmentContext, contentType), "edit", "EquipmentContext") == true ? location.reload() : void 0;
	    	}
	    }
    })
    
    $('#btnConfirmEditTaskContext').click(function(){ 
    	var taskContext = $('#inputAdministrationEditTaskContext').val();
    	var isPublic = $("#cbEditTaskContext").is(':checked');
    	var isFormatCorrect = true;
    	var parsedTaskContext = "";
    	var contentType = $('#editTaskContextSelContentType option:selected').text();
    	var objTaskContext = {};
    	
	    if(validateInput(taskContext, "edit", "TaskContext") == true){
        	if(contentType == "JSON") {
        		if(isJSON(taskContext)){
        			objTaskContext = JSON.parse(taskContext);
	        		
        			if(isPublic){
	        			objTaskContext.public = true;
	    			}
        			
        			parsedTaskContext = JSON.stringify(objTaskContext);
	        		isFormatCorrect = true;
        		} else {
        			$('#editTaskContextMainErrSection').empty();
        			$('#editTaskContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No JSON found. Wrong Content-Type!");
          			isFormatCorrect = false;
        		}
          	} else {
          		if(isXML(taskContext)){       			
          			if(isPublic){
        				var setPublicAttribute = taskContext.replace("<task-context>", "<task-context public='true'>");
        				parsedTaskContext = setPublicAttribute;
          			} else {
          				parsedTaskContext  = taskContext ;
          			}
          			
          			isFormatCorrect = true;
          		} else {
          			$('#editTaskContextMainErrSection').empty();
          			$('#editTaskContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No XML found. Wrong Content-Type!");
          			isFormatCorrect = false;
          		}
        	}
	    	
        	if(isFormatCorrect){
        		validateContext(openape.updateTaskContext(localStorage.getItem("id"), parsedTaskContext, contentType), "edit", "TaskContext") == true ? location.reload() : void 0;
        	}
        }
	 }) 
	 
    $('#btnConfirmEditEnvironmentContext').click(function(){ 
    	var environmentContext = $('#inputAdministrationEditEnvironmentContext').val();
    	var isFormatCorrect = true;
    	var isPublic = $("#cbEditEnvironmentContext").is(':checked');
    	var parsedEnvironmentContext = "";
    	var objEnvironmentContext = {};
    	var contentType = $('#editEnvironmentContextSelContentType option:selected').text();	
    	
    	if(validateInput(environmentContext, "edit", "EnvironmentContext") == true){
    		if(contentType == "JSON") {
        		if(isJSON(environmentContext)){
        			objEnvironmentContext = JSON.parse(environmentContext);
        			
        			if(isPublic){
        				objEnvironmentContext.public = true;
	    			}
        			
	        		parsedEnvironmentContext = JSON.stringify(objEnvironmentContext);
	        		isFormatCorrect = true;
        		} else {
        			$('#editEnvironmentContextMainErrSection').empty();
        			$('#editEnvironmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No JSON found. Wrong Content-Type!");
          			isFormatCorrect = false;
        		}
          	} else {
          		if(isXML(environmentContext)){
          			
          			if(isPublic){
        				var setPublicAttribute = environmentContext.replace("<environment-context>", "<environment-context public='true'>");
        				parsedEnvironmentContext = setPublicAttribute;
          			} else {
          				parsedEnvironmentContext  = environmentContext ;
          			}
          			
          			isFormatCorrect = true;
          		} else {
          			$('#editEnvironmentContextMainErrSection').empty();
          			$('#editEnvironmentContextMainErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  No XML found. Wrong Content-Type!");
          			isFormatCorrect = false;
          		}
        	}
        	
    		if(isFormatCorrect){
    			validateContext(openape.updateEnvironmentContext(localStorage.getItem("id"), parsedEnvironmentContext, contentType), "edit", "EnvironmentContext") == true ? location.reload() : void 0;
    		}
    	}
     })  
})


function isXML(xml){
    try {
        xmlDoc = $.parseXML(xml); //is valid XML
        return true;
    } catch (err) {
        // was not XML
        return false;
    }
}

function isJSON(json){
    try {
        jsonDoc = $.parseJSON(json); //is valid JSON
        return true;
    } catch (err) {
        // was not JSON
        return false;
    }
}

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
		$(errSectionName).append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please add a "+contextName);
		return false; 
	} else {
		return true;
	}
}

function getContext(id, contextName){
	switch(contextName){
		case "taskContext" : return JSON.parse(openape.getTaskContext(id, "JSON").responseText); break;
		case "equipmentContext" : return JSON.parse(openape.getEquipmentContext(id, "JSON").responseText); break;
		case "environmentContext" : return JSON.parse(openape.getEnvironmentContext(id, "JSON").responseText); break;
		case "userContext" : return JSON.parse(openape.getUserContext(id, "JSON").responseText); break;
	}	
}


//View
function viewContext(event){
	var contextName = event.attributes[2].value;
	var contextNameLowerCase = contextName.substring(0, 1).toLowerCase()+contextName.substring(1);
	$('#view'+contextName+'Modal').modal('show');
	var objContext = new Object();
	
	localStorage.setItem("contextId", event.id);
	
	if(contextName == "UserContext"){
		objContext = getContext(event.id, contextNameLowerCase);
	} else {
		objContext.propertys = getContext(event.id, contextNameLowerCase).propertys;
	}
	delete objContext.public;
	
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
		objContext = getContext(event.id, "userContext");
	}
		
	if(isPublic == true){
		$("#cbEdit"+contextName).prop('checked', true);
	} else {
		$("#cbEdit"+contextName).prop('checked', false);
	}
	
	delete objContext.public;
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
		objContext = getContext(event.id, "userContext");
		var context = JSON.stringify(objContext);
		copyResponse = openape.createUserContext(context, "JSON").status;
	} else {
		if(contextName == "TaskContext"){
			objContext = getContext(event.id, "taskContext");
			var context = JSON.stringify(objContext);
			copyResponse = openape.createTaskContext(context, "JSON").status;
		} else if(contextName == "EnvironmentContext"){
			objContext = getContext(event.id, "environmentContext");
			var context = JSON.stringify(objContext);
			copyResponse = openape.createEnvironmentContext(context, "JSON").status;
		} else if(contextName == "EquipmentContext"){
			objContext = getContext(event.id, "equipmentContext");
			var context = JSON.stringify(objContext);
			copyResponse = openape.createEquipmentContext(context, "JSON").status;
		}
	}
	
	if(copyResponse == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}


function formatXml(xml) {
    var formatted = '';
    var reg = /(>)(<)(\/*)/g;
    xml = xml.replace(reg, '$1\r\n$2$3');
    var pad = 0;
    jQuery.each(xml.split('\r\n'), function(index, node) {
        var indent = 0;
        if (node.match( /.+<\/\w[^>]*>$/ )) {
            indent = 0;
        } else if (node.match( /^<\/\w/ )) {
            if (pad != 0) {
                pad -= 1;
            }
        } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
            indent = 1;
        } else {
            indent = 0;
        }

        var padding = '';
        for (var i = 0; i < pad; i++) {
            padding += '  ';
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
}

