

function saveData(){
	 var token = localStorage.getItem("token");	
	 var userContext = document.getElementById("dataInput").value;
	 
	 if(userContext==""){
		 $("#saveUserContextsStatus").empty();
		 $("#saveUserContextsStatus").append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Please enter a usercontext");
	 } else {
		 objSaveUserContextsResult = openape.setUserContexts(token, userContext);
		 $("#saveUserContextsStatus").empty();
		 $("#saveUserContextsStatus").append("<b>UserContextId: </b>"+ objSaveUserContextsResult.responseText);
	 }
	
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
		$('#loadStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Please enter a usercontextId");
	}
	
}

function deleteData() {
	var userContextId =  $('#deleteUserContextIdInput').val();
	
	if(userContextId!=""){
		var token = localStorage.getItem("token");
		
		var objStatus = openape.deleteUserContexts(token, userContextId);
		if(objStatus.status == 204){
			$('#deleteStatus').empty();
			$('#deleteStatus').append("Successfully deleted");
		} 
		if(objStatus.status == 404){
			$('#deleteStatus').empty();
			$('#deleteStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Not Found");
		}
	} else {
		$('#deleteStatus').empty();
		$('#deleteStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Please enter a usercontextId");
	}
	
}

function updateData() {
	var token = localStorage.getItem("token");
	var userContextId =  $('#updateUserContextIdInput').val();
	var userContexts = document.getElementById("updateUserContextTextarea").value;
	
	var isUserContextIdCorrect = false;
	var isUserContextCorrect = false;
	
	
	if(userContextId==""){
		isUserContextIdCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Please enter a userContextId");
	} else {
		isUserContextIdCorrect = true;
	}
	
	if(userContexts==""){
		isUserContextCorrect = false;
		$('#updateStatus').empty();
		$('#updateStatus').append(" <img src='img/Attention-SZ-icon.png' width='20' height='20'> Please enter a userContext");
	} else {
		isUserContextCorrect = true;
	}
	
	if(isUserContextCorrect == true && isUserContextIdCorrect == true){
		var objUpdateStatus = openape.updateUserContexts(token, userContextId, userContexts);

		if(userContextId != ""){
			if(objUpdateStatus.status == 200){
				$('#updateStatus').empty();
				$('#updateStatus').append("updated");
			}
			
			if(objUpdateStatus.status == 400){
				$('#updateStatus').empty();
				$('#updateStatus').append(objUpdateStatus.responseText);
			}
		} else {
			$('#updateStatus').empty(); 
			$('#updateStatus').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'><font style='color:red'> Please enter a usercontextId</font>");
		}
	}
	
	
}


function Logout() {
	window.location = "http://localhost:4567/start.html";
}

