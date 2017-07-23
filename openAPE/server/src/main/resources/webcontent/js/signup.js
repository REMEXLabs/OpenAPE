$(document).ready(function() {
	
	$('#signupUsernameErrIcon').hide();
	$('#signupEmailErrIcon').hide();
	$('#signupPasswordErrIcon').hide();
	$('#signupSecQuestionErrIcon').hide();
	
	$('#div_signup').click(function(){
		$('#signupModal').modal('show');
	})
	
})