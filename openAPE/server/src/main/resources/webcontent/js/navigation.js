$(document).ready(function() {
	
	var token = localStorage.getItem("token");
	if(token === null){
		$('.subnav').hide();
	} else {
		$('.subnav').show();
		if(localStorage.getItem("role") === "admin" || localStorage.getItem("role") !== null){
			$('#linkAdministration').show();
		} else {
			$('#linkAdministration').hide();
		}
	}
	
	var href = document.location.href;
	var lastPathSegment = href.substr(href.lastIndexOf('/') + 1);
	
	if(lastPathSegment == "gettingStarted"){
    	$('#divGettingStarted').addClass("active");	
    	$('#divTutorials').removeClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divContext').removeClass("active");
    	$('#divContact').removeClass("active");
	} else if(lastPathSegment == "tutorials"){
		$('#divTutorials').addClass("active");
    	$('#divGettingStarted').removeClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divContext').removeClass("active");
    	$('#divContact').removeClass("active");
	} else if(lastPathSegment == "downloads"){
    	$('#divDownloads').addClass("active");
    	$('#divTutorials').removeClass("active");
    	$('#divGettingStarted').removeClass("active");
    	$('#divContext').removeClass("active");
    	$('#divContact').removeClass("active");
	}else if(lastPathSegment == "context"){
		$('#divContext').addClass("active");
		$('#divDownloads').removeClass("active");
		$('#divTutorials').removeClass("active");
		$('#divGettingStarted').removeClass("active");
		$('#divContact').removeClass("active");
	} else if(lastPathSegment == "contact"){
    	$('#divContact').addClass("active");
    	$('#divContext').removeClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divTutorials').removeClass("active");
    	$('#divGettingStarted').removeClass("active");
	} else if(lastPathSegment == "myProfile"){
		$('#divAdministration').removeClass("active");
		$('#divMyProfile').addClass("active");
		$('#divMyContexts').removeClass("active");
		$('#divMyResources').removeClass("active");
		$('#divMyGroups').removeClass("active");
	}else if(lastPathSegment == "myContexts"){
		$('#divAdministration').removeClass("active");
		$('#divMyContexts').addClass("active");
		$('#divMyProfile').removeClass("active");
		$('#divMyResources').removeClass("active");
		$('#divMyGroups').removeClass("active");
	} else if(lastPathSegment == "myResources"){
		 $('#divAdministration').removeClass("active");
		 	$('#divMyResources').addClass("active");
		 	$('#divMyContexts').removeClass("active");
		 	$('#divMyProfile').removeClass("active");
		 	$('#divMyGroups').removeClass("active");
	} else if(lastPathSegment == "administration"){
		$('#divAdministration').addClass("active");
		$('#divMyGroups').removeClass("active");
		$('#divMyContexts').removeClass("active");
		$('#divMyProfile').removeClass("active");
		$('#divMyResources').removeClass("active");
	} else if(lastPathSegment == "myGroups"){
		$('#divAdministration').removeClass("active");
    	$('#divMyGroups').addClass("active");
    	$('#divMyContexts').removeClass("active");
    	$('#divMyProfile').removeClass("active");
    	$('#divMyResources').removeClass("active");
	}
	
	

    //$('#nav_adminsectionUsers').click();
    $('#div_nav_adminsectionUsers').addClass("adminsectionNavActive");
    $('#users').show();

    $('#signin').click(function(){	
    	$('#div_signin').addClass("active");
    	$('#div_signup').removeClass("active");
    })
    
     $('#signup').click(function(){
    	$('#div_signup').addClass("active");
    	$('#div_signin').removeClass("active");
    })
    
    
    $('#nav_adminsectionUsers').click(function(){ 	
    	$('#div_nav_adminsectionUsers').addClass("adminsectionNavActive");
    	$('#div_nav_adminsectionGroups').removeClass("adminsectionNavActive");
    	$('#div_nav_adminsectionContexts').removeClass("adminsectionNavActive");
    })
    
    $('#nav_adminsectionGroups').click(function(){ 	
    	$('#div_nav_adminsectionGroups').addClass("adminsectionNavActive");
    	$('#div_nav_adminsectionUsers').removeClass("adminsectionNavActive");
    	$('#div_nav_adminsectionContexts').removeClass("adminsectionNavActive");
    })
    
    $('#nav_adminsectionContexts').click(function(){ 	
    	$('#div_nav_adminsectionContexts').addClass("adminsectionNavActive");
    	$('#div_nav_adminsectionUsers').removeClass("adminsectionNavActive");
    	$('#div_nav_adminsectionGroups').removeClass("adminsectionNavActive");
    })
    
     $('#linkGettingStarted').click(function(){ 	
    	window.location = "http://localhost:4567/gettingStarted";
    })
    
    $('#linkTutorials').click(function(){ 
    	window.location = "http://localhost:4567/tutorials";
    })
    
     $('#linkDownloads').click(function(){ 	
    	 window.location = "http://localhost:4567/downloads";
    })

    $('#linkContext').click(function(){ 
    	 window.location = "http://localhost:4567/context";
    })
    
    $('#linkContact').click(function(){ 
    	 window.location = "http://localhost:4567/contact";
    })
    
    
    $('#linkMyContexts').click(function(){ 	
    	 window.location = "http://localhost:4567/myContexts";
    })
    
    $('#linkMyProfile').click(function(){ 	
    	window.location = "http://localhost:4567/myProfile";
    })
    
     $('#linkMyResources').click(function(){ 	
    	 window.location = "http://localhost:4567/myResources";
    })
    
     $('#linkMyGroups').click(function(){ 	
    	 window.location = "http://localhost:4567/myGroups";
    })
    
    $('#linkAdministration').click(function(){ 	
    	window.location = "http://localhost:4567/administration";
    })
} );

function openCity(evt, cityName) {
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
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}

