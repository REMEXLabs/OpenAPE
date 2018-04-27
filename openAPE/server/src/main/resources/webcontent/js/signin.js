$(document).ready(function() {
	
	
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
	
	var isUsernameCorrect = true;
	var isPasswordCorrect = true;

	
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

	
	
	// lusm: 0
	if(isUsernameCorrect  && isPasswordCorrect){
		var tokenData = openape.initializeLibrary(username, password, "/");
						if(tokenData.status==200){
			var userID = JSON.parse(openape.getUser().responseText).id;
			
			
				if(userID != undefined){
					window.location = document.location.origin+"/myProfile";
				} else {
				
				}
			}
		
		} else {
			$('#signinMainErrSection').empty();
			$('#signinMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> "+"User not found");
		}
	
} // function