var selectedRoles = [];
var origin = window.origin;
$(document).ready(function() {
	if(window.location.hash == "#users"){
		$('#collapseTwo').removeClass("in");
		openCity(event, "users");
	}
	
	$("#edit_"+localStorage.getItem("userid")).attr("disabled", true);
	$("#delete_"+localStorage.getItem("userid")).attr("disabled", true);
	
	$("#user").find("td").each(function() {
		
		$("#"+localStorage.getItem("userid")).attr("disabled", true);
	})
	//add user
	$('#errUsername').hide();
	$('#errEmail').hide();
	$('#errPassword').hide();
	
	//edit user
	$('#editEmailErrIcon').hide();
	$('#editUsernameErrIcon').hide();
	
	//resetting error messages
	$('#createUserModal').on('hidden.bs.modal', function () {
		$('#formGroupEmail').removeClass("has-error has-feedback");
		$('#errorSection').empty();
		$('#formGroupUsername').removeClass("has-error has-feedback");
		$('#formGroupPassword').removeClass("has-error has-feedback");
		$('#errPassword').hide();
		$('#errEmail').hide();
		$('#errUsername').hide();
	});
	
	$('#editModal').on('hidden.bs.modal', function () {
		$('#editErrSection').empty();
		$('#editFormGroupUsername').removeClass("has-error has-feedback");
		$('#editFormGroupEmail').removeClass("has-error has-feedback");
		$('#editEmailErrIcon').hide();
		$('#editUsernameErrIcon').hide();
	});
	
	
	$('#user').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } ); 
	 
	
    $('#btnConfirmDeleteUser').click(function(){ 	
    	removeUser(localStorage.getItem("userId"));
    	
    	location.reload();
    })

     $('#btnConfirmEditUser').click(function(){ 
    	var username = $('#editUsernameInput').val();
    	var email = $('#editEmailInput').val();
    	var id = localStorage.getItem("id");
    	
    	var newObjUser = new Object();
    	
    	newObjUser.username = username;
    	newObjUser.email = email;
    	newObjUser.id = id;
    	newObjUser.roles = selectedRoles;
    	validateEditUserFields(newObjUser);
    	
    })
    
    $('#editRoleAdmin').click(function(){ 
    	if($.inArray("admin", selectedRoles) == -1) {
    		selectedRoles.push("admin");
    		$(this).addClass("selected");
        	if($.inArray("user", selectedRoles) != -1) {
        		selectedRoles.splice($.inArray("user", selectedRoles),1);
        		$('#editRoleUser').removeClass("selected");
        	}
    	} else {
    		selectedRoles.splice($.inArray("admin", selectedRoles),1);
    		$(this).removeClass("selected");
    	}	
    })
    
    $('#addRoleAdmin').click(function(){ 
    	if($.inArray("admin", selectedRoles) == -1) {
    		selectedRoles.push("admin");
    		$(this).addClass("selected");
    		$('#editRoleUser').removeClass("selected");
    		if($.inArray("user", selectedRoles) != -1) {
        		selectedRoles.splice($.inArray("user", selectedRoles),1);
        		$('#addRoleUser').removeClass("selected");
        	}		
    	} else {
    		selectedRoles.splice($.inArray("admin", selectedRoles),1);
    		$(this).removeClass("selected");
    		$('#editRoleUser').removeClass("selected");
    	}	
    })
    
    $('#editRoleUser').click(function(){   	
    	if($.inArray("user", selectedRoles) == -1) {
    		selectedRoles.push("user");
    		$(this).addClass("selected");
    		$('#editRoleAdmin').removeClass("selected");
        	if($.inArray("admin", selectedRoles) != -1) {
        		selectedRoles.splice($.inArray("admin", selectedRoles),1);
        		$('#editRoleAdmin').removeClass("selected");
        	}		
    	} else {
    		selectedRoles.splice($.inArray("user", selectedRoles),1);
    		$(this).removeClass("selected");
    	}	
    })
    
     $('#addRoleUser').click(function(){ 
    	if($.inArray("user", selectedRoles) == -1) {
    		selectedRoles.push("user");
    		$(this).addClass("selected");
        	if($.inArray("admin", selectedRoles) != -1) {
        		selectedRoles.splice($.inArray("admin", selectedRoles),1);
        		$('#addRoleAdmin').removeClass("selected");
        	}
    	} else {
    		selectedRoles.splice($.inArray("user", selectedRoles),1);
    		$(this).removeClass("selected");
    	}
    })
    
    $('#btnConfirmCreateUser').click(function(){ 
    	var objUser = new Object();
    	objUser.username = $('#addUsername').val();
    	objUser.email = $('#addEmail').val();
    	objUser.password = $('#addPassword').val();
    	objUser.roles = selectedRoles;
    	
    	validateAddUserFields(objUser);
    })
} );


function deleteUser(event){
	$('#deleteUserModal').modal('show');
	$('#deleteUserModal').attr('aria-hidden', 'false');
	var id = event.id.substring(event.id.indexOf("_")+1, event.id.length);
	localStorage.setItem("userId", id);
}


function createUser(event){
	$('#createUserModal').modal('show');
	$('#createUserModal').attr('aria-hidden', 'false');
}

function editUser(event){
	var id = event.id.substring(event.id.indexOf("_")+1, event.id.length);
	$('#editModal').modal('show');
	$('#editModal').attr('aria-hidden', 'false');
	$('#editUsernameInput').val($("#tdUserName_"+id).html());
	$('#editEmailInput').val($("#tdEmail_"+id).html());
	
	if($("#tdRoles_"+id).html() == "admin")	{
		$('#editRoleAdmin').addClass("selected");
		selectedRoles.push("admin");
	} 
	
	if($("#tdRoles_"+id).html() == "user"){
		$('#editRoleUser').addClass("selected");
		selectedRoles.push("user");
	}
	
	localStorage.setItem("id", id);
}




function removeUser(userId) {
	$.ajax({
	    type: 'DELETE',
	    contentType: 'application/json',
	    url: origin+'/users?id='+userId,
	    dataType: "json",
	    success: function(data, textStatus, jqXHR){
	    	
	    },
	    error: function(jqXHR, textStatus, errorThrown){
	       // alert('Medicine information could be deleted');
	    }
	});
}


function updateUser(newObjUser) {
	$.ajax({
	    type: 'PUT',
	    contentType: 'application/json',
	    url: origin+'/users',
	    data: JSON.stringify(newObjUser),		
	    dataType: "json",
	    success: function(data, textStatus, jqXHR){
	    	
	    },
	    error: function(jqXHR, textStatus, errorThrown){
	    	var isUsernameCorrect = true;
	    	var isEmailCorrect = true;
	    	if(jqXHR.responseText.includes("username_1 dup key")){
	    		$('#editUsernameMessage').empty();
	    		$('#editUsernameMessage').append("Username already exists");	
	    		$('#editFormGroupUsername').addClass("has-error has-feedback");
	    		$('#editUsernameErrIcon').show();
	    		isUsernameCorrect=false;
	    	} else {
	    		$('#editUsernameMessage').empty();
	    	}
	    	
	    	if (jqXHR.responseText.includes("email_1 dup key")){
	    		$('#editEmailErrMessage').empty();
	    		$('#editEmailErrMessage').append("Email already exists");	
	    		$('#editFormGroupEmail').addClass("has-error has-feedback");
	    		$('#editEmailErrIcon').show();
	    		isEmailCorrect=false;
	    	} else {
	    		$('#editEmailErrMessage').empty();
	    	}

	    	if(isUsernameCorrect && isEmailCorrect){
	        	$('#createUserModal').modal('hide');
	        	
	        	setTimeout(function(){ 
	        		location.reload();
	       		}, 1000);
	    	}
	    }
	});
}


function createUserDB(userObj) {
	$.ajax({
	    type: 'POST',
	    contentType: 'application/json',
	    url: origin+'/users',
	    data: JSON.stringify(userObj),		
	    dataType: "json",
	    
	    success: function(data, textStatus, jqXHR){
	    	
	    },
	    error: function(jqXHR, textStatus, errorThrown){
	    	var isUsernameCorrect = true;
	    	var isEmailCorrect = true;
	    	if(jqXHR.responseText.includes("username_1 dup key")){
	    		$('#usernameError').empty();
	    		$('#usernameError').append("Username already exists");	
	    		$('#formGroupUsername').addClass("has-error has-feedback");
	    		$('#errUsername').show();
	    		isUsernameCorrect=false;
	    	} else {
	    		$('#usernameError').empty();
	    	}
	    	
	    	if (jqXHR.responseText.includes("email_1 dup key")){
	    		$('#emailError').empty();
	    		$('#emailError').append("Email already exists");	
	    		$('#editFormGroupEmail').addClass("has-error has-feedback");
	    		$('#errEmail').show();
	    		isEmailCorrect=false;
	    	} else {
	    		$('#emailError').empty();
	    	}

	    	if(isUsernameCorrect && isEmailCorrect){
	        	$('#createUserModal').modal('hide');
	        	
	        	setTimeout(function(){ 
	        		location.reload();
	       		}, 1000);
	    	}
	    	
	    }
	});
}


function validateAddUserFields (objUser) {	
	var isAddUsernameCorrect = true;
	var isAddEmailCorrect = true;
	var isAddPasswordCorrect = true;
	var isRoleCorrect = true;
	
	var addUsername = $('#addUsername').val();
	var addEmail = $('#addEmail').val();
	var addPassword = $('#addPassword').val();
	
	if(addUsername === ""){
		isAddUsernameCorrect = false;
		$('#formGroupUsername').addClass("has-error has-feedback");
		$('#errUsername').show();
	} else {
		$('#formGroupUsername').removeClass("has-error has-feedback");
		isAddUsernameCorrect = true;
		$('#errUsername').hide();
	}
	
	if(addEmail	 === ""){
		isAddEmailCorrect = false;
		$('#formGroupEmail').addClass("has-error has-feedback");
		$('#errEmail').show();
	} else {
		if(validateEmail(addEmail)){
			isAddEmailCorrect = true;
			$('#formGroupEmail').removeClass("has-error has-feedback");
			$('#errEmail').hide();
			$('#emailError').empty();
		} else  {
			isAddEmailCorrect = false;
			$('#formGroupEmail').addClass("has-error has-feedback");
			$('#errEmail').show();
			$('#emailError').empty();
    		$('#emailError').append("Wrong email form");	
		}
	}

	if(addPassword	=== ""){
		isAddPasswordCorrect = false;
		$('#formGroupPassword').addClass("has-error has-feedback");
		$('#errPassword').show();
	} else {
		isAddPasswordCorrect = true;
		$('#formGroupPassword').removeClass("has-error has-feedback");
		$('#errPassword').hide();
	}
	
	if(objUser.roles.length > 0) {
		$('#errorSection').empty();
		isRoleCorrect = true;
	} else {
		isRoleCorrect = false;
		$('#errorSection').empty();
		$('#errorSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please define a role!");	
	}
	
	if(isAddUsernameCorrect && isAddEmailCorrect && isAddPasswordCorrect && isRoleCorrect){
		createUserDB(objUser);
	}
}



function validateEditUserFields (objUser) {
	var isEditUsernameCorrect = true;
	var isEditEmailCorrect = true;
	var isEditRoleCorrect = true;
	
	var editUsername = $('#editUsernameInput').val();
	var editEmail = $('#editEmailInput').val();
	
	if(editUsername === ""){
		isEditUsernameCorrect = false;
		$('#editFormGroupUsername').addClass("has-error has-feedback");
		$('#editUsernameErrIcon').show();
	} else {
		$('#editFormGroupUsername').removeClass("has-error has-feedback");
		isEditUsernameCorrect = true;
		$('#editUsernameErrIcon').hide();
	}
	
	if(editEmail === ""){
		isAddEmailCorrect = false;
		$('#editFormGroupEmail').addClass("has-error has-feedback");
		$('#editEmailErrIcon').show();
	} else {
		if(validateEmail(editEmail)){
			isEditEmailCorrect = true;
			$('#editFormGroupEmail').removeClass("has-error has-feedback");
			$('#editEmailErrIcon').hide();
			$('#editEmailErrMessage').empty();
		} else  {
			isEditEmailCorrect = false;
			$('#editFormGroupEmail').addClass("has-error has-feedback");
			$('#editEmailErrIcon').show();
			$('#editEmailErrMessage').empty();
    		$('#editEmailErrMessage').append("Wrong email form");	
		}
		
	}
	
	if(objUser.roles.length > 0) {
		$('#editErrSection').empty();
		isEditRoleCorrect = true;
	} else {
		isEditRoleCorrect = false;
		$('#editErrSection').empty();
		$('#editErrSection').append("<img width='20px' height='20px' src='img/attention_icon.png'>  Please define a role!");	
	}
	
	if(isEditUsernameCorrect && isEditEmailCorrect && isEditRoleCorrect){
		updateUser(objUser);
	}
}
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}