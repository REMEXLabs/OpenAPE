$(document).ready(function() {
	
	
	var token = localStorage.getItem("token");
	var username = localStorage.getItem("username");
	if(token === null || token === "undefined"){
		$('#userSection').show();
		$('#userLoggedin').removeClass("active");
	} else {
		$('#userSection').show();
		$('#userSection').empty();
		$('#userSection').append("<div id='userLoggedin' style='padding:1em;height:100%'><span style='float:left'><img width='30px' height='30px' src='img/userIcon.png'> "+username+"  </span><span style='float:right'><button class='btn btn-sm btn-danger' id='btnLoggout'> <span class='glyphicon glyphicon-log-out'></span> logout</button></span></div>");
		$('#userLoggedin').addClass("active");
	}
	
	
	$("#btnLoggout").click(function(){
		localStorage.clear();
		window.location = document.location.origin+"/index";
	})
})

