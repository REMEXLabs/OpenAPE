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
	   		arrRoles.push("admin");
	
	   		var isUsernameCorrect = false;
	   		var isEmailCorrect = false;
	   		var isPasswordCorrect = false;
	    		
	   		//check if username is correct
	   		if(username!=""){
	   			objUser.username = username;
	   			isUsernameCorrect = true;
	   		} else {
	   			isUsernameCorrect = false;
	   		}
	    		
	   		//check if email is correct
	   		if(email!=""){
	   			if((validateEmail(email))==true){
	   	   			objUser.email = email; 			
	   	   			isEmailCorrect=true;
	   			} else {
	   				alert("wrong email");
	   			}
			}else {
	   			isEmailCorrect=false;
	  		}
	    		
	   		//check if password is correct
	   		if(password!=""){
	   			objUser.password = password;
	   			isPasswordCorrect=true;
	   		} else {
	   			isPasswordCorrect=false;
	   		}    		
	    		
	   		if(isPasswordCorrect==true && isEmailCorrect==true && isUsernameCorrect==true){
	   			objUser.roles = arrRoles;
	   			return sendUserData(objUser);
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
		        	objUserProfile = jQuery.parseJSON(data);
		        },
		        error: function(jqXHR, textStatus, errorThrown){
		          console.log(jqXHR, textStatus, errorThrown);
		        }
	    	});
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
	    objOpenape.setUserContexts = function (userContexts, token) {
	    	var objSetUserContext_Result = {};
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
	    	        	objSetUserContext_Result.statusText = jqXHR.statusText;
	    	        	objSetUserContext_Result.userContextId = data;
	    	        },
	    	        error: function(jqXHR, textStatus, errorThrown){
	    	           console.log(jqXHR, textStatus, errorThrown);
	    	      }
	    	 });
	    	
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
