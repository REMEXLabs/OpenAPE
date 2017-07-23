$(document).ready(function() {
	
	$('#signupUsernameErrIcon').hide();
	$('#signupEmailErrIcon').hide();
	$('#signupPasswordErrIcon').hide();
	$('#signupSecQuestionErrIcon').hide();
	
	$('#signup').click(function(){
		$('#signupModal').modal('show');
	})
	
})