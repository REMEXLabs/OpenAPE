//get the protocol and address of the location. If it´s running local, than the address should be http://localhost:4567
var protocol = location.protocol;

function saveData(){
	 var userContext = document.getElementById("dataInput").value;
	 
	 if(userContext==""){
		 $("#saveUserContextsStatus").empty();
		 $("#saveUserContextsStatus").append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a usercontext</font>");
	 } else {
		 objSaveUserContextsResult = openape.setUserContexts(userContext);
		 $("#saveUserContextsStatus").empty();
		 if(objSaveUserContextsResult.responseText.includes("Unexpected character") || objSaveUserContextsResult.responseText.includes("Unrecognized token") ||
				 objSaveUserContextsResult.responseText.includes("Can not construct instance") ||  objSaveUserContextsResult.responseText.includes("Unexpected end-of-inputexpected")){
				$('#saveUserContextsStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Wrong JSON-Format</font>");
		 } else {
			  $("#saveUserContextsStatus").empty();
			  $('#saveUserContextsStatus').append(" <font class='statusInfo'> Successfully added ContextId: "+objSaveUserContextsResult.responseText+"</font>");
		 }
	 }
	
}

function onChangeUpdate(event) {
	var userContextId =  $('#updateUserContextIdInput').val();
	
	var isUserContextIdCorrect = false;
	
	if(userContextId==""){
		isUserContextIdCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a userContextId</font>");
	} else {
		isUserContextIdCorrect = true;
	}
	
	
	
	if(isUserContextIdCorrect){	
		  var file = event.target.files[0];
		  var reader = new FileReader();
		 
		  reader.onload = function(event) {
			var objUpdateStatus = openape.updateUserContexts(userContextId, event.target.result);

			if(objUpdateStatus.responseText.includes("Unexpected character") || objUpdateStatus.responseText.includes("Unrecognized token") ||
				objUpdateStatus.responseText.includes("Can not construct instance") ||  objUpdateStatus.responseText.includes("Unexpected end-of-inputexpected") ||
				objUpdateStatus.responseText.includes("Unrecognized field")){
			 	$('#updateStatus').empty();
			 	$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Wrong JSON-Format</font>");
			} else {
				 if(objUpdateStatus.status == 200){
						$('#updateStatus').empty();
						$('#updateStatus').append("<font class='statusInfo'>updated </font>");
				}			
				if(objUpdateStatus.status == 400){
						$('#updateStatus').empty();
						$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'>"+objUpdateStatus.responseText+"</font>");
				}
			}
		  };
		  
		  reader.readAsText(file);
		  //openape.setUserContexts(jsonUserContext);
	}
}

function onChange(event) {
	  var file = event.target.files[0];
	  var reader = new FileReader();
	 
	  reader.onload = function(event) {
	    // Hier wird der Text der Datei ausgegeben
		  console.log(event.target.result);
		  
		  objSaveUserContextsResult = openape.setUserContexts(event.target.result);
		  $("#saveUserContextsStatus").empty();
		  if(objSaveUserContextsResult.responseText.includes("Unexpected character") || objSaveUserContextsResult.responseText.includes("Unrecognized token") ||
					 objSaveUserContextsResult.responseText.includes("Can not construct instance") ||  objSaveUserContextsResult.responseText.includes("Unexpected end-of-inputexpected") ||
					 objSaveUserContextsResult.responseText.includes("Unrecognized field")){
					$('#saveUserContextsStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Wrong JSON-Format</font>");
		  } else {
				  $("#saveUserContextsStatus").empty();
				  $('#saveUserContextsStatus').append(" <font class='statusInfo'> Successfully added ContextId: "+objSaveUserContextsResult.responseText+"</font>");
		  }
		  
		
	  };
	  
	  reader.readAsText(file);
	  //openape.setUserContexts(jsonUserContext);
}

function loadData() {
	var userContextId =  $('#getUserContextIdInput').val();
	var objResponse = openape.getUserContexts(userContextId);
	
	if(userContextId != ""){
		if(objResponse.status == 200){
			$('#loadStatus').empty(); 
			$('#getUserContextTextarea').val(objResponse.responseText);
		} else {
			$('#getUserContextTextarea').val();
			$('#loadStatus').empty(); 
			$('#loadStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'>"+objResponse.responseText+"</font>");
		}
	} else {
		$('#loadStatus').empty(); 
		$('#loadStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a usercontextId</font>");
	}
	
}

function deleteData() {
	var userContextId =  $('#deleteUserContextIdInput').val();
	
	if(userContextId!=""){
		var objStatus = openape.deleteUserContexts(userContextId);
		if(objStatus.status == 204){
			$('#deleteStatus').empty();
			$('#deleteStatus').append("<font class='statusInfo '>Successfully deleted</font>");
		} 
		if(objStatus.status == 404){
			$('#deleteStatus').empty();
			$('#deleteStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Not Found </font>");
		}
	} else {
		$('#deleteStatus').empty();
		$('#deleteStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'>  Please enter a usercontextId </font>");
	}
	
}

function updateData() {
	var userContextId =  $('#updateUserContextIdInput').val();
	var userContexts = document.getElementById("updateUserContextTextarea").value;
	
	var isUserContextIdCorrect = false;
	var isUserContextCorrect = false;
	
	if(userContextId==""){
		isUserContextIdCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a userContextId</font>");
	} else {
		isUserContextIdCorrect = true;
	}
	
	if(userContexts==""){
		isUserContextCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append(" <img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a userContext</font>");
	} else {
		isUserContextCorrect = true;
	}
	
	if(isUserContextCorrect && isUserContextIdCorrect){
		var objUpdateStatus = openape.updateUserContexts(userContextId, userContexts);

		 if(objUpdateStatus.responseText.includes("Unexpected character") || objUpdateStatus.responseText.includes("Unrecognized token") ||
				 objUpdateStatus.responseText.includes("Can not construct instance") ||  objUpdateStatus.responseText.includes("Unexpected end-of-inputexpected") ||
				 objUpdateStatus.responseText.includes("Unrecognized field")){
			 	$('#updateStatus').empty();
			 	$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Wrong JSON-Format</font>");
		 } else {
			 if(objUpdateStatus.status == 200){
					$('#updateStatus').empty();
					$('#updateStatus').append("<font class='statusInfo'>updated </font>");
				}
				
				if(objUpdateStatus.status == 400){
					$('#updateStatus').empty();
					$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'>"+objUpdateStatus.responseText+"</font>");
				}
		}
	}
}


function Logout() {
	window.location = protocol+"/loginRegistration.html";
}
