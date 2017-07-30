var selectedRoles = [];
$(document).ready(function() {
	//resetting errormessages if closing the modal
	$('#editEquipmentContextModal').on('hidden.bs.modal', function () {
		$('#editEquipmentContextMainErrSection').empty();
	});
	
	$('#addEquipmentContextModal').on('hidden.bs.modal', function () {
		$('#addEquipmentContextMainErrSection').empty();
	});
	
	
	//initialising the datatable
	$('#equipmentContextDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } ); 
	
	//show modal by clicking the add button
    $('#btnAddEquipmentContext').click(function(){ 
    	$('#addEquipmentContextModal').modal('show');
    	
    })
    
    //usercontext actions
    $('#btnConfirmDeleteEquipmentContext').click(function(){ 
    	openape.deleteEquipmentContext(localStorage.getItem("id")); 
    	$('#deleteEquipmentContextModal').modal('hide');
    	e.preventDefault();
    	setTimeout(function(){ 
    		
    		location.reload();
   		}, 1000);
    })
    
    $('#btnConfirmAddEquipmentContext').click(function(){ 
    	var equipmentContextJSON = $('#inputAdministrationAddEquipmentContext').val();   	
    	validateEquipmentContext(equipmentContextJSON, openape.createEquipmentContext(equipmentContextJSON), "add") == true ? location.reload() : void 0;	
    })
    
    $('#btnConfirmEditEquipmentContext').click(function(){ 
    	var inputEquipmentContext = $('#inputAdministrationEditEquipmentContext').val();
      	validateEquipmentContext(inputEquipmentContext, openape.updateEquipmentContext(localStorage.getItem("id"), inputEquipmentContext), "edit") == true ? location.reload() : void 0;
    })    
} );


function copyEquipmentContext(event){
	var objEquipmentContext = new Object();
	objEquipmentContext.propertys = getEquipmentContext(event.id);
	var equipmentContext = JSON.stringify(objEquipmentContext);
	if(openape.createEquipmentContext(equipmentContext).status == 201){
		location.reload();
	} else {
		alert("error occured");
	}
}

function deleteEquipmentContext(event){
	$('#deleteEquipmentContextModal').modal('show');
	localStorage.setItem("id", event.id);	
}

function editEquipmentContext(event){
	$('#editEquipmentContextModal').modal('show');
	var objEquipmentContext = new Object();
	objEquipmentContext.propertys = getEquipmentContext(event.id);
	var equipmentContext = JSON.stringify(objEquipmentContext);
	localStorage.setItem("id", event.id);
	$('#inputAdministrationEditEquipmentContext').val(equipmentContext);
}

function getEquipmentContext(id){
	return JSON.parse(openape.getEquipmentContext(id).responseText).propertys;
}

function validateEquipmentContext (equipmentContextInput, equipmentContextAction, equipmentContextActionName){
	var errSectionName =  "";
	equipmentContextActionName == "edit" ? errSectionName = "#editEquipmentContextMainErrSection" : errSectionName = "#addEquipmentContextMainErrSection";
	
	if(equipmentContextInput != ""){
		var equipmentContextResponse = equipmentContextAction;
		
		if(equipmentContextResponse.status == 201 || equipmentContextResponse.status == 200){
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


function removeEquipmentContext(equipmentContextId) {
	$.ajax({
	    type: 'DELETE',
	    contentType: 'application/json',
	    url: 'http://localhost:4567/api/equipment-contexts/'+equipmentContextId,
	    dataType: "json"
	});
}
