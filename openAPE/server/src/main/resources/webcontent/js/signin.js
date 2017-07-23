$(document).ready(function() {
	
	$('#signinSecQuestionErrIcon').hide();
	$('#signPasswordErrIcon').hide();
	$('#signinUsernameErrIcon').hide();
	$('#signin').click(function(){
		$('#signinModal').modal('show');
		//login();
	})
	
})


function login(){
	var username = $('#signinUsernameInput').val(); 
	var password = $('#signinPwInput').val(); 

	var isUsernameCorrect = true;
	var isPasswordCorrect = true;
	
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
	
	
	if(isUsernameCorrect  && isPasswordCorrect){
		var tokenData = openape.initializeLibrary(username, password, "/");
		
		if(tokenData.status==200){
			var userID = JSON.parse(openape.getUser().responseText).id;
			
			window.location = document.location.origin+"/myProfile";
			
			

		} else {
			console.log("hi");
		}
	}
}