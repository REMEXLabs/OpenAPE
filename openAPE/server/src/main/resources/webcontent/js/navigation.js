


$(document).ready(function(){
	var token = localStorage.getItem("token");
	var href = document.location.href;
	var lastPathSegment = href.substr(href.lastIndexOf('/') + 1);
	

	if(token === null){
		$("#subnav").attr("hidden", true );
		$("#headerBottom").removeAttr( "hidden");
		$('#subnavigatons').empty();
		$('#subnavigatons').append("<div class='headerBottom' id='headerBottom'>&nbsp</div>");
		$('#divLogin').empty();
		$('#divLogin').append("<a href='start.html' id='linkLogin'>Login</a>");

	} else {
		$('#subnavigatons').empty();
		
		if( lastPathSegment != "ressourceUpload.html"){
			$('#subnavigatons').append("" +
					"<div class='subnav' id='subnav'>" +
					"<a href='ressourceUpload.html' id='linkUser-contexts'>User-Contexts</a>" +
					"</div>"
			);
		} else {
			
			$('#subnavigatons').append("" +
					"<div class='subnav' id='subnav'>" +
					"<a href='#' id='linkUser-contexts'>User-Contexts</a>" +
					"</div>" +
					"<div class='subsubnav' id='subsubnav'>" +
					"<a href='#' id='linkOverview'>Overview</a>" +
					"<a href='#' id='linkAdd'>Add</a>" + 
					"<a href='#' id='linkUpdate'>Update</a>" +
					"<a href='#' id='linkDelete'>Delete</a>" +
					"</div>"
			);
			$('#linkUser-contexts').addClass("subnav-active");
		}
		
		
		$('#divLogin').empty();
		$('#divLogin').append("<a href='#' id='linkLogin'>Logout</a>");
		
	}
	
	if(lastPathSegment == "workflow.html"){
		$('#linkTutorial').addClass("topnav-active");
	} else if (lastPathSegment == "index.html"){
		$('#linkHome').addClass("topnav-active");
	} else if(lastPathSegment == "start.html"){
		 $('#linkLogin').addClass("topnav-active");
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

