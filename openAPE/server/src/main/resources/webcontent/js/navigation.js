$(document).ready(function() {
	function deleteUser(){
		alert("fsdfd"+this.value());
	}
	
	$('#example').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } );
    
    //$('#nav_adminsectionUsers').click();
    $('#div_nav_adminsectionUsers').addClass("adminsectionNavActive");
    $('#users').show();

    $('#signin').click(function(){
    	
    	$('#div_signin').addClass("active");
    	$('#div_signup').removeClass("active");
    	$('#userSectionLoggedOut').show();
    	 $('#userSectionSignIn').hide();
    })
    
     $('#signup').click(function(){
    	$('#div_signup').addClass("active");
    	$('#div_signin').removeClass("active");
    	$('#userSectionLoggedOut').show();
    	$('#userSectionSignIn').hide();
    })
    
    $('#signup').click(function(){
    
    	$('#userSectionSignIn').show();
    	$('#userSectionLoggedOut').hide();
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
    	$('#divGettingStarted').addClass("active");
    	
    	$('#divTutorials').removeClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divContext').removeClass("active");
    	$('#divContact').removeClass("active");
    })
    
    $('#linkTutorials').click(function(){ 	
    	$('#divTutorials').addClass("active");
    	$('#divGettingStarted').removeClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divContext').removeClass("active");
    	$('#divContact').removeClass("active");
    })
    
     $('#linkDownloads').click(function(){ 	
    	$('#divDownloads').addClass("active");
    	$('#divTutorials').removeClass("active");
    	$('#divGettingStarted').removeClass("active");
    	$('#divContext').removeClass("active");
    	$('#divContact').removeClass("active");
    })

    $('#linkContext').click(function(){ 	
    	$('#divContext').addClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divTutorials').removeClass("active");
    	$('#divGettingStarted').removeClass("active");
    	$('#divContact').removeClass("active");
    })
    
    $('#linkContact').click(function(){ 	
    	$('#divContact').addClass("active");
    	$('#divContext').removeClass("active");
    	$('#divDownloads').removeClass("active");
    	$('#divTutorials').removeClass("active");
    	$('#divGettingStarted').removeClass("active");
    })
    
    
    $('#linkMyContexts').click(function(){ 	
    	$('#divAdministration').removeClass("active");
    	$('#divMyContexts').addClass("active");
    	$('#divMyProfile').removeClass("active");
    	$('#divMyResources').removeClass("active");
    	$('#divMyGroups').removeClass("active");
    })
    
    $('#linkMyProfile').click(function(){ 	
    	$('#divAdministration').removeClass("active");
    	$('#divMyProfile').addClass("active");
    	$('#divMyContexts').removeClass("active");
    	$('#divMyResources').removeClass("active");
    	$('#divMyGroups').removeClass("active");
    })
    
     $('#linkMyResources').click(function(){ 	
    	 $('#divAdministration').removeClass("active");
    	$('#divMyResources').addClass("active");
    	$('#divMyContexts').removeClass("active");
    	$('#divMyProfile').removeClass("active");
    	$('#divMyGroups').removeClass("active");
    })
    
     $('#linkMyGroups').click(function(){ 	
    	$('#divAdministration').removeClass("active");
    	$('#divMyGroups').addClass("active");
    	$('#divMyContexts').removeClass("active");
    	$('#divMyProfile').removeClass("active");
    	$('#divMyResources').removeClass("active");
    })
    
    $('#linkAdministration').click(function(){ 	
    	$('#divAdministration').addClass("active");
    	$('#divMyGroups').removeClass("active");
    	$('#divMyContexts').removeClass("active");
    	$('#divMyProfile').removeClass("active");
    	$('#divMyResources').removeClass("active");
    })
    
    
    
} );


function deleteUser(event){
	removeUser(event.id);
	location.reload();
}





function removeUser(userId) {
	$.ajax({
	    type: 'DELETE',
	    contentType: 'application/json',
	    url: 'http://localhost:4567/te?id='+userId,
	    dataType: "json",
	    success: function(data, textStatus, jqXHR){
	    	
	    },
	    error: function(jqXHR, textStatus, errorThrown){
	       // alert('Medicine information could be deleted');
	    }
	});
}
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

