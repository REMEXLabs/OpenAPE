$(document).ready(function() {

	var userdata = JSON.parse(openape.getUser(localStorage.getItem("token")).responseText);
	localStorage.setItem("username", userdata.username);
	localStorage.setItem("userid", userdata.id);
	if(userdata.roles.includes("admin")){
		localStorage.setItem("role", "admin");
	}
})