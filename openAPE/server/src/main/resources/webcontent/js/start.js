$(document).ready(function(){
	$('#register').click(function(){
		setUserData();
	})
	
	$('#login').click(function(){
		getTokenForLogin();
	})
})


function setUserData(){
	var username = $("#regUsername").val();
	var email = $("#email").val();
	var password = $("#regPassword").val();
	var regSecurityQuestion = $("#regSecurityQuestion").val();
	
	if(regSecurityQuestion == 15){
		if(openape.setUser(username, email, password)==true){
			window.location = "http://localhost:4567/ressourceUpload.html";
		}
	} else {
		alert("Wrong security question answer!");
	}
}

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


function getTokenForLogin(){
	var username = $("#username").val();
	var password = $("#password").val();
	var token = openape.getToken("password", username, password).token;
	
	localStorage.setItem("token", token);
	var userID = openape.getUser(token).id;
	var securityQuestion = $("#securityQuestion").val();
	
	if(securityQuestion == 15){
		if(userID != undefined){
			window.location = "http://localhost:4567/ressourceUpload.html";
		} else {
			alert("user not found");
		}
	}  else {
		alert("Wrong security question answer!");
	}
}