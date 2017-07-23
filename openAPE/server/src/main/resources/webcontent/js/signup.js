$(document).ready(function() {
	
	$('#signupUsernameErrIcon').hide();
	$('#signupEmailErrIcon').hide();
	$('#signupPasswordErrIcon').hide();
	$('#signupSecQuestionErrIcon').hide();
	
	$('#signup').click(function(){
		$('#signupModal').modal('show');
	})
	
	$('#btnConfirmSignup').click(function(){
		setUserData();
	})
	
	
})


function setUserData(){
	var username = $("#inputSignupUsername").val();
	var email = $("#inputSignupEmail").val();
	var password = $("#inputSignupPassword").val();
	var securityQuestion = $("#inputSignupSecQuestion").val();
	
	var isUsernameCorrect = true;
	var isEmailCorrect = true;
	var isPasswordCorrect = true;
	var isSecurityQuestionCorrect = true;
	
	if(username!=""){
		isUsernameCorrect = true;
		$('#signupUsernameErrIcon').hide();
		$('#formGroupSignupUsername').removeClass( "has-error has-feedback" );
		$('#signupUsernameErrMessage').empty();
	} else {
		isUsernameCorrect = false;
		$('#signupUsernameErrIcon').show();
		$('#formGroupSignupUsername').addClass( "has-error has-feedback" );
		$('#signupUsernameErrMessage').empty();
		$('#signupUsernameErrMessage').append("The username can not be empty!");
	}
	
	if(email!=""){
		isEmailCorrect = true;
		$('#signupEmailErrIcon').hide();
		$('#signupEmailErrMessage').empty();
		$('#formGroupSignupEmail').removeClass( "has-error has-feedback" );
	} else {
		isEmailCorrect = false;
		$('#signupEmailErrIcon').show();
		$('#formGroupSignupEmail').addClass( "has-error has-feedback" );
		$('#signupEmailErrMessage').empty();
		$('#signupEmailErrMessage').append("The email can not be empty!");
	}
	
	if(password!=""){
		isPasswordCorrect = true;
		$('#signupPasswordErrIcon').hide();
		$('#formGroupSignupPassword').removeClass( "has-error has-feedback" );
		$('#signupPasswordErrMessage').empty();
	} else {
		isPasswordCorrect = false;
		$('#signupPasswordErrIcon').show();
		$('#signupPasswordErrMessage').empty();
		$('#signupPasswordErrMessage').append("The password can not be empty!");
		$('#formGroupSignupPassword').addClass( "has-error has-feedback" );
	}	
	
	if(securityQuestion!=""){
		isSecurityQuestionCorrect = true;
		$('#signupSecQuestionErrIcon').hide();
		$('#signupSecQuestionErrMessage').empty();
		$('#formGroupSignupSecQuestion').removeClass( "has-error has-feedback" );
	} else {
		isSecurityQuestionCorrect = false;
		$('#signupSecQuestionErrMessage').empty();
		$('#signupSecQuestionErrMessage').append("The security question can not be empty!");
		$('#signupSecQuestionErrIcon').show();
		$('#formGroupSignupSecQuestion').addClass( "has-error has-feedback" );
	}		

	if(isSecurityQuestionCorrect && isUsernameCorrect && isEmailCorrect && isPasswordCorrect){
		if(securityQuestion == 15){
			var objSenduserStatus = openape.createUser(username, email, password, "/");
			if(objSenduserStatus.status == 200){
				var tokenData = openape.initializeLibrary(username, password, "/");
				window.location = document.location.origin+"/myProfile";
				
			} else {
				$('#signupMainErrSection').empty();
				$('#signupMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> "+objSenduserStatus.statusText);
			}
		} else {
			$('#signupSecQuestionErrMessage').empty();
			$('#signupSecQuestionErrMessage').append("Wrong security question");
		}
	}
}