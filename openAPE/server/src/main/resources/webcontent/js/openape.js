(function(window){
	 function defineOpenape(){
	    var objOpenape = {};
	    
	    //get the protocol and adress of the location. If it´s running local, than the adress should be http://localhost:4567
	    var protocol = location.protocol;
	    
	    //
	    // FUNCTIONS FOR USER
	    //
	    
	    /** setUser
	     * 
	     * This function is used to set the user in the mongodb database with the given username, email and password
	     *
	     * @param  userName
	     * 	 The username of the user from the mongodb
	     * 
	     * @param  email
	     * 	 The email adress of the user from the mongodb
	     * 
	     * @param  password
	     * 	 The password of the user from the mongodb
	     * 
	     * @return      The function will send the user credentials to the function
	     * 				sendUserData and return a status as a boolean 
	     */
	    objOpenape.setUser = function (username, email, password) {
	   		var objUser = new Object();
	   		var arrRoles = [];
	   		
	   		var objErrResponse = new Object();
	   		var arrStatusTexts = [];
	   		
	   		var isUsernameCorrect = false;
	   		var isEmailCorrect = false;
	   		var isPasswordCorrect = false;
	    	
	   		arrRoles.push("admin");
	   		
	   		//check if username is correct
	   		if(username==""){
	   			isUsernameCorrect = false;
	   			arrStatusTexts.push("The username can not be empty");
	   		} else if(username === undefined){
	   			isUsernameCorrect = false;
	   			arrStatusTexts.push("Please enter a username");
	   		} else {
	   			objUser.username = username;
	   			isUsernameCorrect = true;
	   		}
	    		
	   		//check if email is correct
	   		if(email==""){  			
	   			isEmailCorrect=false;
	   			arrStatusTexts.push("The email can not be empty");
			} else if(email === undefined){
				isEmailCorrect = false;
	   			arrStatusTexts.push("Please enter a email");
			} else {
	   			if((validateEmail(email))==true){
	   	   			objUser.email = email; 			
	   	   			isEmailCorrect=true;
	   			} else {
	   				arrStatusTexts.push("Wrong email form");
	   			}
	  		}
	    		
	   		//check if password is correct
	   		if(password==""){
	   			isPasswordCorrect=false;
	   			arrStatusTexts.push("The password can not be empty");
	   		} else if(password === undefined){
	   			arrStatusTexts.push("Please enter a password");
	   		} else {
	   			objUser.password = password;
	   			isPasswordCorrect=true;
	   		}    		
	    		
	   		if(isPasswordCorrect==true && isEmailCorrect==true && isUsernameCorrect==true){
	   			objUser.roles = arrRoles;
	   			return sendUserData(objUser);
	   		} else {
	   			objErrResponse.status = 400;
	   			objErrResponse.statusText = arrStatusTexts;
	   			return objErrResponse;
	   		}
	    }

	    /** getUser
	    * 
	    * This function is used to get user informations by the given token
	    *
	    * @param  token
	    * 	 The authentification token to authorized the user
	    * 
	    * @return      
	    * 	 A javascript object with all found user information like username, email and roles
	    */
	    objOpenape.getUser = function (token) {
	    	var objUserProfile = {};
	    	var isTokenCorrect = true;
	    	
	    	if(token==""){
	    		objUserProfile.statusText = "Token can not be empty";
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		isTokenCorrect = false;
	    		objUserProfile.statusText = "Please enter a token";
	    	} 
	    	
	    	if(isTokenCorrect == true) {
	    		$.ajax({
			        type: 'GET',
			        async: false,
			        contentType: 'json',
			        headers: {
			            "Authorization": token,
			        },
			        url: protocol+"/profile",
			        dataType: "html",
			        success: function(data, textStatus, jqXHR){
			        	objUserProfile = jqXHR;
			        },
			        error: function(jqXHR, textStatus, errorThrown){
			        	objUserProfile = jqXHR;
			        }
		    	});
	    	} else {
	    		objUserProfile.status = 400;
	    	}
	    	
	    	return objUserProfile;
	    }
	    
	    /*
	    * TOKEN FUNCTIONS
	    */
	  
	    /** getToken
		* 
		* This function is used get the authorization token for the given grantTypem, username and password 
		*
		* @param  grant_type
		* 	 
	    * @param  userName
	    * 	 The username of the user from the mongodb
		* 			 
	    * @param  password
	    * 	 The password of the user from the mongodb
	    * 
		* @return      
		* 	 A javascript object with all token information
		*/
	    objOpenape.getToken = function (grant_type, username, password) {
	    	var objToken = {};
	    	var arrStatusText = [];
	    	
	    	var isPasswordCorret = true;
	    	var isUsernameCorrect = true;
	    	var isGrantTypeCorrect = true;
	    	
	    	if(grant_type==""){
	    		arrStatusText.push("Grant type can not be empty");
	    		isGrantTypeCorrect = false;
	    	} else if (grant_type === undefined){
	    		arrStatusText.push("Please enter a grant type");
	    		isGrantTypeCorrect = false;
	    	} else if (grant_type != "password"){
	    		arrStatusText.push("Grant type should be password");
	    		isGrantTypeCorrect = false;
	    	}
	    	
	    	if(username == ""){
	    		arrStatusText.push("Username can not be empty");
	    		isUsernameCorrect = false;
	    	} else if(username === undefined){
	    		arrStatusText.push("Please enter a username");
	    		isUsernameCorrect = false;
	    	}
	    	
	    	if(password==""){
	    		arrStatusText.push("Password can not be empty");
	    		isPasswordCorret = false;
	    	} else if(password === undefined){
	    		arrStatusText.push("Please enter a password");
	    		isUsernameCorrect = false;
	    	}
	    	
	    	if(isPasswordCorret == true && isUsernameCorrect == true && isGrantTypeCorrect == true){
	    		$.ajax({
	    	        type: 'POST',
	    	        async: false,
	    	        url: protocol+"/token?grant_type="+grant_type+"&username="+username+"&password="+password,
	    	        dataType: "html",
	    	        success: function(data, textStatus, jqXHR){
	    	        	objToken = jqXHR;
	    	        },
	    	        error: function(jqXHR, textStatus, errorThrown){
	    	           objToken = jqXHR;
	    	      }
	    		});
	    	} else {
	    		objToken.statusText = arrStatusText;
	    		objToken.status = 400;
	    	}
	    	return objToken;
	    }
	    
	    /*
	     * USER-CONTEXTS FUNCTION
	     */
	   
	    /** getUserContexts
		* 
		* This function is used get the authorization token for the given grantTypem, username and password 
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored userContextsId from mongodb
	    * 
		* @return      
		* 	 A javascript object with all user contexts information
		*/
	    objOpenape.getUserContexts = function (token, userContextId) {
	    	var objGetUserContext_Result = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		isTokenCorrect = false;
	    	}
	    	
	    	if(userContextId==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isuserContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isuserContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect==true && isuserContextIdCorrect == true){
		    	$.ajax({
		    	        type: 'GET',
		    	        async: false,
		    	        contentType: 'application/json',
		    	        headers: {
		    	        	 "Authorization": token,
		    	        },
		    	        url: protocol+"/api/user-contexts/"+userContextId,
		    	        success: function(data, textStatus, jqXHR){
		    	        	objGetUserContext_Result = jqXHR;
		  
		    	        },
		    	        error: function(jqXHR, textStatus, errorThrown){
		    	        	objGetUserContext_Result = jqXHR;
		    	      }
		    	 });
	    	} else {
	    		objGetUserContext_Result.status = 400;
	    		objGetUserContext_Result.statusText = arrStatusText;
	    	}
	    	
	    	return objGetUserContext_Result;
	    }
	    
	    /** setUserContexts
		* 
		* This function is used to set the user contexts  
		*
		* @param  userContexts
	    * 	 The given user contexts
		*
		* @param  token
		* 	 The authentification token to authorized the user
	    * 
		* @return      
		* 	 A javascript object with all status information of the set process
		*/	    
	    objOpenape.setUserContexts = function (token, userContexts) {
	    	var objSetUserContext_Result = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isUserContextCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		isTokenCorrect = false;
	    	}
	    	
	    	if(userContexts==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isUserContextCorrect = false;
	    	} else if(userContexts === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isUserContextCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect == true && isUserContextCorrect == true){
		    	$.ajax({
		    	        type: 'POST',
		    	        async: false,
		    	        contentType: 'application/json',
		    	        headers: {
		    	        	 "Authorization": token,
		    	        },
		    	        url: protocol+"/api/user-contexts",
		    	        data: userContexts,
		    	        success: function(data, textStatus, jqXHR){
		    	        	objSetUserContext_Result = jqXHR;
		    	        },
		    	        error: function(jqXHR, textStatus, errorThrown){
		    	        	objSetUserContext_Result = jqXHR;
		    	      }
		    	 });
	    	} else {
	    		objSetUserContext_Result.statusText = arrStatusText;
	    		objSetUserContext_Result.status = 400;
	    	}
	    	return objSetUserContext_Result;
	    }
	    
	    /** deleteUserContexts
		* 
		* This function is used to delete user contexts  
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored userContextsId from mongodb
	    * 
		* @return      
		* 	 A javascript object with all status information of the delete process
		*/	    	    
	    objOpenape.deleteUserContexts = function (token, userContextId) {
	    	var objDeleteUserContext_Result = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isTokenCorrect = false;
	    	}
	    	
	    	if(userContextId==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isuserContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isuserContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect==true && isuserContextIdCorrect == true){
	    		$.ajax({
	    	        type: 'DELETE',
	    	        async: false,
	    	        contentType: 'application/json',
	    	        headers: {
	    	        	 "Authorization": token,
	    	        },
	    	        url: protocol+"/api/user-contexts/"+userContextId,
	    	        success: function(data, textStatus, jqXHR){
	    	        	objDeleteUserContext_Result = jqXHR;
	    	        },
	    	        error: function(jqXHR, textStatus, errorThrown){
	    	        	objDeleteUserContext_Result = jqXHR;
	    	      }
	    	 });
	    	} else {
	    		objDeleteUserContext_Result.status = 400;
	    	}
	    	
	    	
	    	return objDeleteUserContext_Result;
	    }
	    
	    /** updateUserContexts
		* 
		* This function is used to update user contexts by the given usercontext id  
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored userContextsId from mongodb
	    * 
		* @param  userContexts
	    * 	 The given user contexts
	    * 
		* @return      
		* 	 A javascript object with all status information of the update process
		*/	
	    objOpenape.updateUserContexts = function (token, userContextId, userContexts) {
	    	var objUpdateUserContext_Result = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isUserContextCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		isTokenCorrect = false;
	    	}
	    	
	    	if(userContexts==""){
	    		arrStatusText.push("The usercontext can not be empty");
	    		isUserContextCorrect = false;
	    	} else if(userContexts === undefined){
	    		arrStatusText.push("Please enter a usercontext");
	    		isUserContextCorrect = false;
	    	}
	    	
	    	if(userContextId==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isuserContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isuserContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect == true && isUserContextCorrect == true && isuserContextIdCorrect == true){
	    		$.ajax({
	    	        type: 'PUT',
	    	        async: false,
	    	        contentType: 'application/json',
	    	        headers: {
	    	        	 "Authorization": token,
	    	        },
	    	        url: protocol+"/api/user-contexts/"+userContextId,
	    	        data: userContexts,
	    	        success: function(data, textStatus, jqXHR){
	    	        	objUpdateUserContext_Result = jqXHR;
	    	        },
	    	        error: function(jqXHR, textStatus, errorThrown){
	    	        	objUpdateUserContext_Result = jqXHR;
	    	      }
	    		});
	    	} else {
	    		objUpdateUserContext_Result.status = 400;
	    		objUpdateUserContext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdateUserContext_Result;
	    }     
	    
	    
	    function sendUserData(user){
	    	 objSendUserdata = {};
	    	 $.ajax({
	    	        type: 'POST',
	    	        async: false,
	    	        contentType: 'json',
	    	        url: protocol+"/users",
	    	        dataType: "html",
	    	        data: JSON.stringify(user),
	    	        success: function(data, textStatus, jqXHR){
	    				localStorage.setItem("token", data.substring(17, 41));
	    				objSendUserdata.status =  jqXHR.status;
	    	        },
	    	        error: function(jqXHR, textStatus, errorThrown){
	    	           objSendUserdata.status = jqXHR.status;
	    	           if(jqXHR.responseText.includes("username_1 dup key")==true){
	    	        	   objSendUserdata.statusText = "User with this username already exists";
	    		       } else  if(jqXHR.responseText.includes("email_1 dup key")==true){
	    		    	   objSendUserdata.statusText = "User with this email already exists";
	    		       }
	    	        }
	    	  });
	    	 return objSendUserdata;
	    }
	
	    function validateEmail(email) {
	        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	        return re.test(email);
	    }
	
	    
	    // We will add functions to our library here !
	    return objOpenape;
	  }
	
	  // We need that our library is globally accesible, then we save in the window
	  if(typeof(window.openape) === 'undefined'){
	    window.openape = defineOpenape();
	  }
})(window);
