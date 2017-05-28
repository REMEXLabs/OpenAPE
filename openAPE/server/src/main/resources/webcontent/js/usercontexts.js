//get the protocol and adress of the location. If it´s running local, than the adress should be http://localhost:4567
var protocol = location.protocol;

function saveData(){
	 var userContext = document.getElementById("dataInput").value;
	 
	 if(userContext==""){
		 $("#saveUserContextsStatus").empty();
		 $("#saveUserContextsStatus").append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Please enter a usercontext");
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

		if(userContextId != ""){
			if(objUpdateStatus.status == 200){
				$('#updateStatus').empty();
				$('#updateStatus').append("<font class='statusInfo'>updated </font>");
			}
			
			if(objUpdateStatus.status == 400){
				$('#updateStatus').empty();
				$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> <font class='statusError'>"+objUpdateStatus.responseText+"</font>");
			}
		} else {
			$('#updateStatus').empty(); 
			$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font class='statusError'>Please enter a usercontextId</font>");
		}
	}
}


function Logout() {
	window.location = protocol+"/start.html";
}

