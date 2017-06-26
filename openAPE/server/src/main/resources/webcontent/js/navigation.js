


$(document).ready(function(){
	var token = localStorage.getItem("token");
	var href = document.location.href;
	var lastPathSegment = href.substr(href.lastIndexOf('/') + 1);
	var protocol = location.protocol;
	
	//set the top navigation
	$('#mainnavigationlinks').append('<a href="index.html" id="linkHome">Home</a><a href="tutorial.html" id="linkTutorial">Tutorials</a><a href="download.html" id="linkDownload">Downloads</a>');
	$('#tutoriallinks').append('<a href="workflow.html" id="linkWorkflow">Workflow</a>');
	
	//if token === null than the use is not loggedin, else the user is loggedin and a token was created
	if(token === null){
		$("#subnav").attr("hidden", true );
		$("#headerBottom").removeAttr( "hidden");
		$('#subnavigatons').empty();
		$('#subnavigatons').append("<div class='headerBottom' id='headerBottom'>&nbsp</div>");
		$('#divLogin').empty();
		$('#divLogin').append("<a href='loginRegistration.html' id='linkLogin'>Login</a>");

	} else {
		$('#subnavigatons').empty();
		if( lastPathSegment != "usercontexts.html"){
			$('#subnavigatons').append("" +
					"<div class='subnav' id='subnav'>" +
					"<a href='usercontexts.html' id='linkUser-contexts'>User-Contexts</a>" +
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
		$('#linkworkflow').addClass("topnav-active");
		$('#linkWorkflow').addClass("subnav-active");
		$('#linkTutorial').addClass("topnav-active");
	} else if (lastPathSegment == "index.html"){
		$('#linkHome').addClass("topnav-active");
	} else if(lastPathSegment == "loginRegistration.html#"){
		 $('#linkLogin').addClass("topnav-active");
	} else if(lastPathSegment == "usercontexts.html"){
		$('#linkOverview').addClass("active");
	} else if(lastPathSegment == "tutorial.html"){
		$('#linkTutorial').addClass("topnav-active");
	} else if(lastPathSegment == "download.html"){
		$('#linkDownload').addClass("topnav-active");
	}
	
	
	$("#linkLogin").click(function(){
		localStorage.clear();
		window.location=window.location;
		if(lastPathSegment == "usercontexts.html"){
			window.location = protocol+"/loginRegistration.html";
		}
	})
	
	

	$("#linkHome").click(function(){
		$("#tutoriallinks").hide();
		
	})
	
	$("#linkUser-contexts").click(function(){
		$("#subsubnav").show();
		$('#linkHome').removeClass("topnav-active");
		$('#linkUser-contexts').addClass("subnav-active");
		
	})
	
	$("#linkTutorial").click(function(){
		$('#linkTutorial').addClass("topnav-active");
		$('#linkLogin').removeClass("topnav-active");
		$('#linkHome').removeClass("topnav-active");
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

function openSection(evt, sectionName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(sectionName).style.display = "block";
    evt.currentTarget.className += " active";
}

