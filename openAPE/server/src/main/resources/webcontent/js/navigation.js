$(document).ready(function(){
	var token = localStorage.getItem("token");
	var href = document.location.href;
	var lastPathSegment = href.substr(href.lastIndexOf('/') + 1);
	
	if(lastPathSegment == "workflow.html"){
		$('#linkTutorial').addClass("topnav-active");
	} else if (lastPathSegment == "index.html"){
		$('#linkHome').addClass("topnav-active");
	} else if(lastPathSegment == "start.html"){
		 $('#linkLogin').addClass("topnav-active");
	}
	
	if(token === null){
		$("#subnav").hide();
		$("#headerBottom").show();
		$('#linkLogin').empty();
		$('#linkLogin').append("login");

	} else {
		$("#subnav").show();
		$("#headerBottom").hide();
		$('#linkLogin').empty();
		$('#linkLogin').append("logout");
		$("#linkLogin").removeAttr("href");
		$("#linkLogin").attr("href", "#");
	}
	
	$("#linkLogin").click(function(){
		localStorage.clear();
		location.reload();
		if(lastPathSegment == "ressourceUpload.html"){
			window.location = "http://localhost:4567/start.html";
		}
	})
	
	$("#linkUser-contexts").click(function(){
		$("#subsubnav").show();
		$('#linkUser-contexts').addClass("subnav-active");
	})	
	
	$("#linkOverview").click(function(){
		$('#linkOverview').addClass("active");
		$('#linkUpdate').removeClass("active");
		$('#linkAdd').removeClass("active");
		$('#linkDelete').removeClass("active");
		$("#add").hide();
		$("#overview").show();
		$("#update").hide();
		$("#delete").hide();
	})	
	

	$("#linkAdd").click(function(){
		$('#linkAdd').addClass("active");
		$('#linkUpdate').removeClass("active");
		$('#linkOverview').removeClass("active");
		$('#linkDelete').removeClass("active");
		$("#add").show();
		$("#overview").hide();
		$("#update").hide();
		$("#delete").hide();
	})	
	
	$("#linkUpdate").click(function(){
		$('#linkUpdate').addClass("active");
		$('#linkAdd').removeClass("active");
		$('#linkOverview').removeClass("active");
		$('#linkDelete').removeClass("active");
		$("#update").show();
		$("#overview").hide();
		$("#add").hide();
		$("#delete").hide();
	})	

	$("#linkDelete").click(function(){
		$('#linkDelete').addClass("active");
		$('#linkAdd').removeClass("active");
		$('#linkOverview').removeClass("active");
		$('#linkUpdate').removeClass("active");
		$("#delete").show();
		$("#overview").hide();
		$("#add").hide();
		$("#update").hide();
	})	
})

