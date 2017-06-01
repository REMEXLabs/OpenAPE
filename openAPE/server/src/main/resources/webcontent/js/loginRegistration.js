//get the protocol and address of the location. If it´s running local, than the address should be http://localhost:4567
var protocol = location.protocol;

$(document).ready(function(){
	$('#errUsername').hide();
	$('#errPassword').hide();
	$('#errSecQuestion').hide();
	$('#errRegUsername').hide();
	$('#errRegEmail').hide();
	$('#errRegPassword').hide();
	$('#errRegSecQuestion').hide();
	
	$('#register').click(function(){
		setUserData();
	})
	
	$('#login').click(function(){
		getTokenForLogin();
	})
})


function setUserData(){
	var username = $("#regUsername").val();
	var email = $("#email").val();
	var password = $("#regPassword").val();
	var regSecurityQuestion = $("#regSecurityQuestion").val();
	
	var isUsernameCorrect = true;
	var isEmailCorrect = true;
	var isPasswordCorrect = true;
	var isRegSecurityQuestionCorrect = true;
	
	if(username!=""){
		isUsernameCorrect = true;
		$('#errRegUsername').hide();
		$('#formGroupRegUsername').removeClass( "has-error has-feedback" );
	} else {
		isUsernameCorrect = false;
		$('#errRegUsername').show();
		$('#formGroupRegUsername').addClass( "has-error has-feedback" );
	}
	
	if(email!=""){
		isEmailCorrect = true;
		$('#errRegEmail').hide();
		$('#formGroupRegEmail').removeClass( "has-error has-feedback" );
	} else {
		isEmailCorrect = false;
		$('#errRegEmail').show();
		$('#formGroupRegEmail').addClass( "has-error has-feedback" );
	}
	
	if(password!=""){
		isPasswordCorrect = true;
		$('#errRegPassword').hide();
		$('#formGroupRegPassword').removeClass( "has-error has-feedback" );
	} else {
		isPasswordCorrect = false;
		$('#errRegPassword').show();
		$('#formGroupRegPassword').addClass( "has-error has-feedback" );
	}	
	
	if(regSecurityQuestion!=""){
		isRegSecurityQuestionCorrect = true;
		$('#errRegSecQuestion').hide();
		$('#formGroupRegSecQuestion').removeClass( "has-error has-feedback" );
	} else {
		isRegSecurityQuestionCorrect = false;
		$('#errRegSecQuestion').show();
		$('#formGroupRegSecQuestion').addClass( "has-error has-feedback" );
	}		

	if(isRegSecurityQuestionCorrect && isUsernameCorrect && isEmailCorrect && isPasswordCorrect){
		if(regSecurityQuestion == 15){
			var objSenduserStatus = openape.setUser(username, email, password);
			if(objSenduserStatus.status == 200){
				var tokenData = openape.initializeLibrary(username, password);
				window.location = protocol+"/usercontexts.html";
				$('#registrationErrorMsg').empty();
			} else {
				$('#registrationErrorMsg').empty();
				$('#registrationErrorMsg').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> "+objSenduserStatus.statusText);
			}
		} else {
			$('#registrationErrorMsg').empty();
			$('#registrationErrorMsg').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Wrong security question");
		}
	}
}

function getTokenForLogin(){
	var username = $("#username").val();
	var password = $("#password").val();
	var securityQuestion = $("#securityQuestion").val();
	var isUsernameCorrect = true;
	var isPasswordCorrect = true;
	var isSecurityQuestionCorrect = true;
	
	if(username!=""){
		$('#errUsername').hide();
		$('#forGroupUsername').removeClass( "has-error has-feedback" );
		isUsernameCorrect = true;
	} else {
		$('#errUsername').show();
		$('#forGroupUsername').addClass( "has-error has-feedback" );
		isUsernameCorrect = false;
	}
	
	if(password!=""){
		isPasswordCorrect = true;
		$('#errPassword').hide();
		$('#formGroupPassword').removeClass( "has-error has-feedback" );
	} else {
		isPasswordCorrect = false;
		$('#errPassword').show();
		$('#formGroupPassword').addClass( "has-error has-feedback" );
	}

	if(securityQuestion!=""){
		$('#errSecQuestion').hide();
		$('#formGroupSecQuestion').removeClass( "has-error has-feedback" );
		isSecurityQuestionCorrect = true;
	} else {
		$('#errSecQuestion').show();
		$('#formGroupSecQuestion').addClass( "has-error has-feedback" );
		isSecurityQuestionCorrect = false;
	}
	
	
	if(isUsernameCorrect  && isPasswordCorrect && isSecurityQuestionCorrect){
		var tokenData = openape.initializeLibrary(username, password);
		
		if(tokenData.status==200){
			var userID = JSON.parse(openape.getUser().responseText).id;
			var securityQuestion = $("#securityQuestion").val();
			
			if(securityQuestion == 15){
				if(userID != undefined){
					window.location = protocol+"/usercontexts.html";
				} else {
					$('#loginErrorMsg').empty();
					$('#loginErrorMsg').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'>  user not found");
				}
			}  else {
				$('#loginErrorMsg').empty();
				$('#loginErrorMsg').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> Wrong security question answer!");
			}
		} else {
			$('#loginErrorMsg').empty();
			$('#loginErrorMsg').append("<img src='img/Attention-SZ-icon.png' width='20' height='20'> User not found");
		}
	}
}