//get the protocol and address of the location. If it´s running local, than the address should be http://localhost:4567
var protocol = location.protocol;

function saveData(){
	 var userContext = document.getElementById("dataInput").value;
	 
	 if(userContext==""){
		 $("#saveUserContextStatus").empty();
		 $("#saveUserContextStatus").append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a usercontext</font>");
	 } else {
		 objSaveUserContextResult = openape.setUserContext(userContext);
		 $("#saveUserContextStatus").empty();
		 if(objSaveUserContextResult.responseText.includes("Unexpected character") || objSaveUserContextResult.responseText.includes("Unrecognized token") ||
				 objSaveUserContextResult.responseText.includes("Can not construct instance") ||  objSaveUserContextResult.responseText.includes("Unexpected end-of-inputexpected")){
				$('#saveUserContextStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Wrong JSON-Format</font>");
		 } else {
			  $("#saveUserContextStatus").empty();
			  $('#saveUserContextStatus').append(" <font class='statusInfo'> Successfully added ContextId: "+objSaveUserContextResult.responseText+"</font>");
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
			var objUpdateStatus = openape.updateUserContext(userContextId, event.target.result);

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
		  //openape.setUserContext(jsonUserContext);
	}
}

function onChange(event) {
	  var file = event.target.files[0];
	  var reader = new FileReader();
	 
	  reader.onload = function(event) {
	    // Hier wird der Text der Datei ausgegeben
		  console.log(event.target.result);
		  
		  objSaveUserContextResult = openape.createUserContext(event.target.result);
		  $("#saveUserContextStatus").empty();
		  if(objSaveUserContextResult.responseText.includes("Unexpected character") || objSaveUserContextResult.responseText.includes("Unrecognized token") ||
					 objSaveUserContextResult.responseText.includes("Can not construct instance") ||  objSaveUserContextResult.responseText.includes("Unexpected end-of-inputexpected") ||
					 objSaveUserContextResult.responseText.includes("Unrecognized field")){
					$('#saveUserContextStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'> Wrong JSON-Format</font>");
		  } else {
				  $("#saveUserContextStatus").empty();
				  $('#saveUserContextStatus').append(" <font class='statusInfo'> Successfully added ContextId: "+objSaveUserContextResult.responseText+"</font>");
		  }
		  
		
	  };
	  
	  reader.readAsText(file);
	  //openape.createUserContext(jsonUserContext);
}

function loadData() {
	var userContextId =  $('#getUserContextIdInput').val();
	var objResponse = openape.getUserContext(userContextId);
	
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
		var objStatus = openape.deleteUserContext(userContextId);
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
	var userContext = document.getElementById("updateUserContextTextarea").value;
	
	var isUserContextIdCorrect = false;
	var isUserContextCorrect = false;
	
	if(userContextId==""){
		isUserContextIdCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a userContextId</font>");
	} else {
		isUserContextIdCorrect = true;
	}
	
	if(userContext==""){
		isUserContextCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append(" <img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'> Please enter a userContext</font>");
	} else {
		isUserContextCorrect = true;
	}
	
	if(isUserContextCorrect && isUserContextIdCorrect){
		var objUpdateStatus = openape.updateUserContext(userContextId, userContext);

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
