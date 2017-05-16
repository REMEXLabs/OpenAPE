function saveData(){
	 var token = localStorage.getItem("token");	
	 objSaveUserContextsResult = openape.setUserContexts(document.getElementById("dataInput").value, token);
	 $("#saveUserContextsStatus").empty();
	 $("#saveUserContextsStatus").append("<b>UserContextId: </b>"+ objSaveUserContextsResult.userContextId);
}

function loadData() {
	var userContextId =  $('#getUserContextIdInput').val();
	var token = localStorage.getItem("token");
	var objResponse = openape.getUserContexts(token, userContextId);
	
	if(userContextId != ""){
		if(objResponse.status == 200){
			$('#loadStatus').empty(); 
			$('#getUserContextTextarea').val(objResponse.responseText);
		} else {
			$('#getUserContextTextarea').val();
			$('#loadStatus').empty(); 
			$('#loadStatus').append(objResponse.responseText);
		}
	} else {
		$('#loadStatus').empty(); 
		$('#loadStatus').append("Please enter a usercontextId");
	}
	
}

function deleteData() {
	var userContextId =  $('#deleteUserContextIdInput').val();
	var token = localStorage.getItem("token");
	
	var objStatus = openape.deleteUserContexts(token, userContextId);
	if(objStatus.status == 204){
		$('#deleteStatus').empty();
		$('#deleteStatus').append("<font style='color:green'>Successfully deleted</font>");
	} 
	if(objStatus.status == 404){
		$('#deleteStatus').empty();
		$('#deleteStatus').append("<font style='color:red'>Not Found</font>");
	}
}

function updateData() {
	var token = localStorage.getItem("token");
	var userContextId =  $('#updateUserContextIdInput').val();
	var userContexts = document.getElementById("updateUserContextTextarea").value;
	var objUpdateStatus = openape.updateUserContexts(token, userContextId, userContexts);
		
	if(userContextId != ""){
		if(objUpdateStatus.status == 200){
			$('#updateStatus').empty();
			$('#updateStatus').append("updated");
		}
		
		if(objUpdateStatus.status == 400){
			$('#updateStatus').empty();
			$('#updateStatus').append("update failed");
		}
	} else {
		$('#updateStatus').empty(); 
		$('#updateStatus').append("Please enter a usercontextId");
	}
	
}


function Logout() {
	window.location = "http://localhost:4567/start.html";
}

