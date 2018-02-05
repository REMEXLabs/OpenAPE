$(document).ready(function() {

	var userdata = JSON.parse(openape.getUser(localStorage.getItem("token")).responseText);
	localStorage.setItem("username", userdata.username);
	localStorage.setItem("userid", userdata.id);
	if(userdata.roles.includes("admin")){
		localStorage.setItem("role", "admin");
	}
})

$.(btnPassword).click(function(){
	let pw1 = $.("btnPassword1").getValue();
	let pw2 = $.("btnPassword2").getValue();
	
});