$(document).ready(function() {
	
	$('#signinSecQuestionErrIcon').hide();
	$('#signPasswordErrIcon').hide();
	$('#signinUsernameErrIcon').hide();
	$('#signin').click(function(){
				$('#signinModal').modal('show');
				$('#signinModal').attr('aria-hidden','false');
		$('inputSigninUsername').focus;
		//login();
	})
	
	$('#btnConfirmSignin').click(function(){
		getTokenForLogin();
	})
	
})

function getTokenForLogin(){
	var username = $('#inputSigninUsername').val();
	var password = $('#inputSigninPassword').val();
	var securityQuestion = $('#inputSigninSecQuestion').val();
	var isUsernameCorrect = true;
	var isPasswordCorrect = true;
	var isSecurityQuestionCorrect = true;
	
	if(username!=""){
		$('#signinUsernameErrIcon').hide();
		$('#formGroupSigninUsername').removeClass( "has-error has-feedback" );
		$('#signinMainErrSection').empty();
		$('#signinUsernameErrMessage').hide();
		isUsernameCorrect = true;
	} else {
		$('#signinUsernameErrIcon').show();
		$('#formGroupSigninUsername').addClass( "has-error has-feedback" );
		$('#signinMainErrSection').empty();
		$('#signinUsernameErrMessage').show();
		$('#signinUsernameErrMessage').empty();
		$('#signinUsernameErrMessage').append("The username can not be empty!");
		isUsernameCorrect = false;
	}
	
	if(password!=""){
		isPasswordCorrect = true;
		$('#signPasswordErrIcon').hide();
		$('#signinPasswordErrMessage').hide();
		$('#signinMainErrSection').empty();
		$('#formGroupSigninPassword').removeClass( "has-error has-feedback" );
	} else {
		isPasswordCorrect = false;
		$('#signPasswordErrIcon').show();
		$('#formGroupSigninPassword').addClass( "has-error has-feedback" );
		$('#signinMainErrSection').empty();
		$('#signinPasswordErrMessage').show();
		$('#signinPasswordErrMessage').empty();
		$('#signinPasswordErrMessage').append("The password can not be empty!");
	}

	if(securityQuestion!=""){
		$('#signinSecQuestionErrIcon').hide();
		$('#signinSecQuestionErrMessage').hide();
		$('#signinMainErrSection').empty();
		$('#formGroupSigninSecQuestion').removeClass( "has-error has-feedback" );
		isSecurityQuestionCorrect = true;
	} else {
		$('#signinSecQuestionErrIcon').show();
		$('#formGroupSigninSecQuestion').addClass( "has-error has-feedback" );
		$('#signinMainErrSection').empty();
		$('#signinSecQuestionErrMessage').show();
		$('#signinSecQuestionErrMessage').empty();
		$('#signinSecQuestionErrMessage').append("The security question can not be empty!");
		isSecurityQuestionCorrect = false;
	}
	
	
	if(isUsernameCorrect  && isPasswordCorrect && isSecurityQuestionCorrect){
		var tokenData = openape.initializeLibrary(username, password, "/");
		
		if(tokenData.status==200){
			var userID = JSON.parse(openape.getUser().responseText).id;
			
			if(securityQuestion == 15){
				if(userID != undefined){
					window.location = document.location.origin+"/myProfile";
				} else {
				
				}
			}  else {
				$('#signinSecQuestionErrMessage').show();
				$('#signinSecQuestionErrMessage').empty();
				$('#signinSecQuestionErrMessage').append("Wrong security question!");
			}
		} else {
			$('#signinMainErrSection').empty();
			$('#signinMainErrSection').append("User not found");
		}
	}
}